package revolhope.splanes.com.domain.repositoryimpl

import com.google.gson.Gson
import revolhope.splanes.com.data.datasource.FirebaseDataSource
import revolhope.splanes.com.domain.cache.EntryCacheDataSourceImpl
import revolhope.splanes.com.domain.mapper.EntryMapper
import revolhope.splanes.com.domain.model.EntryDirectoryModel
import revolhope.splanes.com.domain.model.EntryKeyModel
import revolhope.splanes.com.domain.model.EntryModel
import revolhope.splanes.com.domain.repository.EntryRepository
import revolhope.splanes.com.domain.util.cryptographic.CryptographyUtils
import javax.inject.Inject

class EntryRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource,
    private val cryptographyUtils: CryptographyUtils
) : EntryRepository {

    override suspend fun fetchEntries(parentId: String?): Pair<EntryDirectoryModel, List<EntryModel>>? =
        if (EntryCacheDataSourceImpl.isCacheEmpty()) {
            firebaseDataSource.fetchData()?.map {
                cryptographyUtils.decrypt(it.first.let(EntryMapper::fromCryptoResponseToModel)).run {
                    Gson().fromJson(
                        this,
                        if (it.second) EntryDirectoryModel::class.java else EntryKeyModel::class.java
                    )
                }
            }?.let {
                EntryCacheDataSourceImpl.insertCache(it)
                EntryCacheDataSourceImpl.fetch(parentId)
            }
        } else {
            EntryCacheDataSourceImpl.fetch(parentId)
        }


    override suspend fun insertEntry(entry: EntryModel): Boolean =
        firebaseDataSource.insertData(
            id = entry.id,
            response = cryptographyUtils.encrypt(Gson().toJson(entry))
                .let(EntryMapper::fromCryptoModelToResponse),
            isDirectory = entry is EntryDirectoryModel
        ).also { if (it) EntryCacheDataSourceImpl.insertEntry(entry) }

    override suspend fun fetchHierarchy(dirId: String): List<EntryDirectoryModel> {
        if (EntryCacheDataSourceImpl.isCacheEmpty()) {
            fetchEntries(EntryDirectoryModel.Root.id)
        }
        return EntryCacheDataSourceImpl.fetchHierarchy(dirId)
    }
}
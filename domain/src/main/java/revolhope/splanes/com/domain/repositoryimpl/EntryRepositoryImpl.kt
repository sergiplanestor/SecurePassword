package revolhope.splanes.com.domain.repositoryimpl

import com.google.gson.Gson
import revolhope.splanes.com.data.datasource.FirebaseDataSource
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

    override suspend fun fetchEntries(): EntryDirectoryModel =
        firebaseDataSource.fetchData()?.map {
            cryptographyUtils.decrypt(it.first.let(EntryMapper::fromCryptoResponseToModel)).run {
                Gson().fromJson(
                    this,
                    if (it.second) EntryDirectoryModel::class.java else EntryKeyModel::class.java
                )
            }
        }?.let { buildHierarchy(EntryDirectoryModel.Root, it) } ?: EntryDirectoryModel.Root

    private fun buildHierarchy(dir: EntryDirectoryModel, list: List<EntryModel>): EntryDirectoryModel {
        dir.content.addAll(list.filter { it.parentDirectoryModel?.id == dir.id })
        dir.content.forEach {
            it.parentDirectoryModel = dir
            if (it is EntryDirectoryModel) {
                buildHierarchy(it, list)
            }
        }
        return dir
    }

    override suspend fun insertEntry(entry: EntryModel): Boolean =
        firebaseDataSource.insertData(
            id = entry.id,
            response = cryptographyUtils.encrypt(Gson().toJson(entry)).let(EntryMapper::fromCryptoModelToResponse),
            isDirectory = entry is EntryDirectoryModel
        )
}
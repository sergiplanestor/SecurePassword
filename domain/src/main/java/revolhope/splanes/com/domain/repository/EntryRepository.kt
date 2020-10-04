package revolhope.splanes.com.domain.repository

import revolhope.splanes.com.domain.model.EntryDirectoryModel
import revolhope.splanes.com.domain.model.EntryModel

interface EntryRepository {

    suspend fun fetchEntries(parentId: String?): Pair<EntryDirectoryModel, List<EntryModel>>?

    suspend fun insertEntry(entry: EntryModel): Boolean

    suspend fun fetchHierarchy(dirId: String): List<EntryDirectoryModel>
}
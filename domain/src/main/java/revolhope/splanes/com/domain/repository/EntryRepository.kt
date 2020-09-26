package revolhope.splanes.com.domain.repository

import revolhope.splanes.com.domain.model.EntryDirectoryModel
import revolhope.splanes.com.domain.model.EntryModel

interface EntryRepository {

    suspend fun fetchEntries(): EntryDirectoryModel

    suspend fun insertEntry(entry: EntryModel): Boolean
}
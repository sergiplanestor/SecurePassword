package revolhope.splanes.com.domain.usecase.entry

import revolhope.splanes.com.domain.model.EntryDirectoryModel
import revolhope.splanes.com.domain.repository.EntryRepository
import revolhope.splanes.com.domain.usecase.BaseUseCase
import javax.inject.Inject

class FetchEntriesUseCase @Inject constructor(
    private val entryRepository: EntryRepository
) : BaseUseCase<FetchEntriesUseCase.Request, EntryDirectoryModel>() {

    override suspend fun execute(request: Request): EntryDirectoryModel =
        entryRepository.fetchEntries()

    object Request
}
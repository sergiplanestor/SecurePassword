package revolhope.splanes.com.domain.usecase.entry

import revolhope.splanes.com.domain.model.EntryDirectoryModel
import revolhope.splanes.com.domain.model.EntryModel
import revolhope.splanes.com.domain.repository.EntryRepository
import revolhope.splanes.com.domain.usecase.BaseUseCase
import javax.inject.Inject

class FetchEntriesUseCase @Inject constructor(
    private val entryRepository: EntryRepository
) : BaseUseCase<FetchEntriesUseCase.Request, Pair<EntryDirectoryModel, List<EntryModel>>?>() {

    override suspend fun execute(request: Request): Pair<EntryDirectoryModel, List<EntryModel>>? =
        entryRepository.fetchEntries(request.parentId)

    data class Request(val parentId: String)
}
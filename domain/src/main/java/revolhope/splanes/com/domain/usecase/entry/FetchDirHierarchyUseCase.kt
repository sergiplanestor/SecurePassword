package revolhope.splanes.com.domain.usecase.entry

import revolhope.splanes.com.domain.model.EntryDirectoryModel
import revolhope.splanes.com.domain.repository.EntryRepository
import revolhope.splanes.com.domain.usecase.BaseUseCase
import javax.inject.Inject

class FetchDirHierarchyUseCase @Inject constructor(
    private val entryRepository: EntryRepository
) : BaseUseCase<FetchDirHierarchyUseCase.Request, List<EntryDirectoryModel>>() {

    override suspend fun execute(request: Request): List<EntryDirectoryModel> =
        entryRepository.fetchHierarchy(request.dirId)

    data class Request(val dirId: String)
}
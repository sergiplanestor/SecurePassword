package revolhope.splanes.com.domain.usecase.entry

import revolhope.splanes.com.domain.model.EntryModel
import revolhope.splanes.com.domain.repository.EntryRepository
import revolhope.splanes.com.domain.usecase.BaseUseCase
import javax.inject.Inject

class DeleteEntryUseCase @Inject constructor(
    private val entryRepository: EntryRepository
) : BaseUseCase<DeleteEntryUseCase.Request, Boolean>() {

    override suspend fun execute(request: Request): Boolean =
        entryRepository.deleteEntry(request.model)

    data class Request(val model: EntryModel)
}
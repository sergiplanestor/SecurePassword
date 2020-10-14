package revolhope.splanes.com.domain.usecase.entry

import revolhope.splanes.com.domain.model.EntryModel
import revolhope.splanes.com.domain.repository.EntryRepository
import revolhope.splanes.com.domain.usecase.BaseUseCase
import javax.inject.Inject

class UpdateEntryUseCase @Inject constructor(
    private val entryRepository: EntryRepository
) : BaseUseCase<UpdateEntryUseCase.Request, Boolean>() {

    override suspend fun execute(request: Request): Boolean =
        entryRepository.updateEntry(request.model)

    data class Request(val model: EntryModel)
}
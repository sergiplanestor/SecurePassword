package revolhope.splanes.com.domain.usecase.entry

import revolhope.splanes.com.domain.model.EntryModel
import revolhope.splanes.com.domain.repository.EntryRepository
import revolhope.splanes.com.domain.usecase.BaseUseCase
import javax.inject.Inject

class InsertEntryUseCase @Inject constructor(
    private val entryRepository: EntryRepository
) : BaseUseCase<InsertEntryUseCase.Request, Boolean>() {

    override suspend fun execute(request: Request): Boolean =
        entryRepository.insertEntry(request.data)

    data class Request(val data: EntryModel)
}
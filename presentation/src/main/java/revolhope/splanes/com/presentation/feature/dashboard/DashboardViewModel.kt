package revolhope.splanes.com.presentation.feature.dashboard

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import revolhope.splanes.com.domain.model.EntryDirectoryModel
import revolhope.splanes.com.domain.usecase.entry.FetchEntriesUseCase
import revolhope.splanes.com.domain.usecase.entry.InsertEntryUseCase
import revolhope.splanes.com.presentation.common.base.BaseViewModel
import java.util.*

class DashboardViewModel @ViewModelInject constructor(
    private val fetchEntriesUseCase: FetchEntriesUseCase,
    private val insertEntryUseCase: InsertEntryUseCase,
    @Assisted private val state: SavedStateHandle
) : BaseViewModel() {

    val entry: LiveData<EntryDirectoryModel> get() = _entry
    private var _entry = MutableLiveData<EntryDirectoryModel>()

    val insertEntryState: LiveData<Boolean> get() = _insertEntryState
    private var _insertEntryState = MutableLiveData<Boolean>()

    fun fetchEntries() =
        launchAsync {
            handleResponse(
                responseState = fetchEntriesUseCase.invoke(FetchEntriesUseCase.Request)
            )?.let(_entry::postValue)
        }

    fun insertDir(name: String, parentDir: EntryDirectoryModel, oldModel: EntryDirectoryModel? = null) {
        val entry = EntryDirectoryModel(
            id = getId(oldModel),
            name = name,
            parentDirectoryModel = parentDir,
            isDirectory = true
        )
        launchAsync {
            handleResponse(
                responseState = insertEntryUseCase.invoke(InsertEntryUseCase.Request(entry))
            ).let { _insertEntryState.postValue(it ?: false) }
        }
    }

    private fun getId(oldModel: EntryDirectoryModel?): String =
        oldModel?.id ?: UUID.randomUUID().toString().replace("-", "")

}
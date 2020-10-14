package revolhope.splanes.com.presentation.feature.dashboard

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import revolhope.splanes.com.domain.model.EntryDirectoryModel
import revolhope.splanes.com.domain.model.EntryModel
import revolhope.splanes.com.domain.usecase.entry.FetchDirHierarchyUseCase
import revolhope.splanes.com.domain.usecase.entry.FetchEntriesUseCase
import revolhope.splanes.com.domain.usecase.entry.InsertEntryUseCase
import revolhope.splanes.com.presentation.common.base.BaseViewModel
import java.util.UUID

class DashboardViewModel @ViewModelInject constructor(
    private val fetchEntriesUseCase: FetchEntriesUseCase,
    private val fetchDirHierarchyUseCase: FetchDirHierarchyUseCase,
    private val insertEntryUseCase: InsertEntryUseCase,
    @Assisted private val state: SavedStateHandle
) : BaseViewModel() {

    val entries: LiveData<Pair<EntryDirectoryModel, List<EntryModel>>?> get() = _entries
    private var _entries = MutableLiveData<Pair<EntryDirectoryModel, List<EntryModel>>?>()

    val dirHierarchy: LiveData<List<EntryDirectoryModel>> get() = _dirHierarchy
    private val _dirHierarchy = MutableLiveData<List<EntryDirectoryModel>>()

    val insertEntryState: LiveData<Boolean> get() = _insertEntryState
    private var _insertEntryState = MutableLiveData<Boolean>()

    fun fetchEntries(parentId: String) =
        launchAsync {
            handleResponse(
                responseState = fetchEntriesUseCase.invoke(FetchEntriesUseCase.Request(parentId))
            ).let(_entries::postValue)
        }

    fun fetchDirHierarchy(dirId: String) {
        launchAsync(showLoader = false) {
            handleResponse(
                responseState = fetchDirHierarchyUseCase.invoke(FetchDirHierarchyUseCase.Request(dirId))
            )?.let(_dirHierarchy::postValue)
        }
    }

    fun insertDir(name: String, parentDir: EntryDirectoryModel, oldModel: EntryDirectoryModel? = null) {
        val entry = EntryDirectoryModel(
            id = getId(oldModel),
            name = name,
            parentId = parentDir.id,
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
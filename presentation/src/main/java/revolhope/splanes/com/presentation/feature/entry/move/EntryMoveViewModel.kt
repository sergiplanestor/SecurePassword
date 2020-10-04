package revolhope.splanes.com.presentation.feature.entry.move

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import revolhope.splanes.com.domain.model.EntryDirectoryModel
import revolhope.splanes.com.domain.model.EntryModel
import revolhope.splanes.com.domain.usecase.entry.FetchDirHierarchyUseCase
import revolhope.splanes.com.domain.usecase.entry.FetchEntriesUseCase
import revolhope.splanes.com.presentation.common.base.BaseViewModel

class EntryMoveViewModel @ViewModelInject constructor(
    private val fetchDirHierarchyUseCase: FetchDirHierarchyUseCase,
    private val fetchEntriesUseCase: FetchEntriesUseCase,
    @Assisted private val state: SavedStateHandle
) : BaseViewModel() {

    val entries: LiveData<Pair<EntryDirectoryModel, List<EntryModel>>?> get() = _entries
    private var _entries = MutableLiveData<Pair<EntryDirectoryModel, List<EntryModel>>?>()

    val dirHierarchy: LiveData<List<EntryDirectoryModel>> get() = _dirHierarchy
    private val _dirHierarchy = MutableLiveData<List<EntryDirectoryModel>>()

    fun fetchDirectories(parentId: String) {
        launchAsync {
            handleResponse(
                responseState = fetchEntriesUseCase.invoke(FetchEntriesUseCase.Request(parentId))
            )?.let(_entries::postValue)
        }
    }

    fun fetchDirHierarchy(dirId: String) {
        launchAsync {
            handleResponse(
                responseState = fetchDirHierarchyUseCase.invoke(FetchDirHierarchyUseCase.Request(dirId))
            )?.let(_dirHierarchy::postValue)
        }
    }
}
package revolhope.splanes.com.presentation.feature.entry.options

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import revolhope.splanes.com.domain.model.EntryDirectoryModel
import revolhope.splanes.com.domain.model.EntryModel
import revolhope.splanes.com.domain.usecase.entry.DeleteEntryUseCase
import revolhope.splanes.com.domain.usecase.entry.InsertEntryUseCase
import revolhope.splanes.com.domain.usecase.entry.UpdateEntryUseCase
import revolhope.splanes.com.presentation.common.base.BaseViewModel

class EntryOptionsViewModel @ViewModelInject constructor(
    private val updateEntryUseCase: UpdateEntryUseCase,
    private val deleteEntryUseCase: DeleteEntryUseCase,
    @Assisted private val state: SavedStateHandle
) : BaseViewModel() {

    val renameResult: LiveData<Boolean> get() = _renameResult
    private var _renameResult = MutableLiveData<Boolean>()

    val moveResult: LiveData<Boolean> get() = _moveResult
    private var _moveResult = MutableLiveData<Boolean>()

    val deleteResult: LiveData<Boolean> get() = _deleteResult
    private var _deleteResult = MutableLiveData<Boolean>()

    fun renameDirectory(entry: EntryModel, newName: String) {
        entry.name = newName
        launchAsync {
            handleResponse(
                responseState = updateEntryUseCase.invoke(UpdateEntryUseCase.Request(entry))
            )?.let(_renameResult::postValue)
        }
    }

    fun moveEntry(entry: EntryModel, destination: EntryDirectoryModel) {
        entry.parentId = destination.id
        launchAsync {
            handleResponse(
                responseState = updateEntryUseCase.invoke(UpdateEntryUseCase.Request(entry))
            )?.let(_moveResult::postValue)
        }
    }

    fun deleteEntry(entry: EntryModel) {
        launchAsync {
            handleResponse(
                responseState = deleteEntryUseCase.invoke(DeleteEntryUseCase.Request(entry))
            )?.let(_deleteResult::postValue)
        }
    }

}
package revolhope.splanes.com.presentation.feature.entry.key

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import revolhope.splanes.com.domain.model.EntryDirectoryModel
import revolhope.splanes.com.domain.model.EntryKeyComplexity
import revolhope.splanes.com.domain.model.EntryKeyLength
import revolhope.splanes.com.domain.model.EntryKeyModel
import revolhope.splanes.com.domain.usecase.entry.InsertEntryUseCase
import revolhope.splanes.com.presentation.common.base.BaseViewModel
import java.util.*

class EntryKeyViewModel @ViewModelInject constructor(
    private val insertEntryUseCase: InsertEntryUseCase,
    @Assisted private val state: SavedStateHandle
) : BaseViewModel() {

    val formNameState: LiveData<Boolean> get() = _formNameState
    private var _formNameState = MutableLiveData<Boolean>()

    val formKeyState: LiveData<Boolean> get() = _formNameState
    private var _formKeyState = MutableLiveData<Boolean>()

    val entryKeyState: LiveData<Boolean> get() = _entryKeyState
    private var _entryKeyState = MutableLiveData<Boolean>()

    fun insertEntry(
        name: String,
        key: String,
        keyLength: EntryKeyLength?,
        keyComplexity: EntryKeyComplexity?,
        extraInfo: String,
        parentId: String?,
        oldModel: EntryKeyModel? = null
    ) {
        var hasErrors = false
        if (name.isBlank()) {
            _formNameState.value = false
            hasErrors = true
        }
        if (key.isBlank()) {
            _formKeyState.value = false
            hasErrors = true
        }
        if (keyLength == null || keyComplexity == null || parentId == null) {
            hasErrors = true
        }
        if (hasErrors) {
            _entryKeyState.value = false
            return
        }

        _formNameState.value = true
        _formKeyState.value = true

        val newModel = EntryKeyModel(
            id = getId(oldModel),
            name = name,
            key = key,
            keyLength = keyLength!!,
            keyComplexity = keyComplexity!!,
            extraInfo = extraInfo,
            parentId = parentId,
            isDirectory = false,
            dateCreation = System.currentTimeMillis(),
            dateUpdate = System.currentTimeMillis()
        )

        launchAsync {
            handleResponse(
                responseState = insertEntryUseCase.invoke(InsertEntryUseCase.Request(newModel))
            )?.let(_entryKeyState::postValue)
        }
    }

    private fun getId(oldModel: EntryKeyModel?): String =
        oldModel?.id ?: UUID.randomUUID().toString().replace("-", "")
}
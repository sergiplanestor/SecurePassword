package revolhope.splanes.com.presentation.feature.login.authfragment.register.existingaccount

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import revolhope.splanes.com.domain.model.LoginData
import revolhope.splanes.com.domain.usecase.user.FetchUserRemoteUseCase
import revolhope.splanes.com.domain.usecase.user.InsertLoginDataUseCase
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.common.base.BaseViewModel

class AlreadyHaveAccountViewModel @ViewModelInject constructor(
    private val fetchUserRemoteUseCase: FetchUserRemoteUseCase,
    private val insertLoginDataUseCase: InsertLoginDataUseCase,
    @Assisted private val state: SavedStateHandle
) : BaseViewModel() {

    val emailFormState: LiveData<Pair<Boolean, Int>> get() = _emailFormState
    private var _emailFormState = MutableLiveData<Pair<Boolean, Int>>()

    val pwdFormState: LiveData<Pair<Boolean, Int>> get() = _pwdFormState
    private var _pwdFormState = MutableLiveData<Pair<Boolean, Int>>()

    val isFormValidated: LiveData<Boolean> get() = _isFormValidated
    private var _isFormValidated = MutableLiveData<Boolean>()

    val onCredentialsStored: LiveData<Boolean> get() = _onCredentialsStored
    private var _onCredentialsStored = MutableLiveData<Boolean>()

    fun fetchUserRemote(email: String, pwd: String) {
        launchAsync {
            handleResponse(
                responseState = fetchUserRemoteUseCase.invoke(
                    FetchUserRemoteUseCase.Request(
                        email = email,
                        pwd = pwd
                    )
                )
            )?.let {
                launchAsync {
                    handleResponse(
                        responseState = insertLoginDataUseCase.invoke(
                            InsertLoginDataUseCase.Request(
                                it
                            )
                        )
                    )?.let(_onCredentialsStored::postValue) ?: _onCredentialsStored.postValue(false)
                }
            } ?: _onCredentialsStored.postValue(false)
        }
    }

    fun validateFields(email: String?, pwd: String?) {
        var hasErrors = false
        when {
            email.isNullOrBlank() -> {
                _emailFormState.value = false to R.string.error_blank_field
                hasErrors = true
            }
            !email.matches(Regex(".*@.*(\\.com|\\.es)")) -> {
                _emailFormState.value = false to R.string.error_email_pattern
            }
            else -> {
                _emailFormState.value = true to -1
            }
        }
        when {
            pwd.isNullOrBlank() -> {
                _pwdFormState.value = false to R.string.error_blank_field
                hasErrors = true
            }
            pwd.length < 8 -> {
                _pwdFormState.value = false to R.string.error_password_length
                hasErrors = true
            }
            else -> {
                _pwdFormState.value = true to -1
            }
        }
        _isFormValidated.value = hasErrors.not()
    }

}
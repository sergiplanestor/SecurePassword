package revolhope.splanes.com.presentation.feature.login

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import revolhope.splanes.com.domain.model.LoginData
import revolhope.splanes.com.domain.usecase.user.DoLoginUseCase
import revolhope.splanes.com.domain.usecase.user.GetLoginDataUseCase
import revolhope.splanes.com.domain.usecase.user.InsertLoginDataUseCase
import revolhope.splanes.com.domain.usecase.user.RegisterUserUseCase
import revolhope.splanes.com.presentation.common.base.BaseViewModel

class LoginViewModel @ViewModelInject constructor(
    private val getLoginDataUseCase: GetLoginDataUseCase,
    private val insertLoginDataUseCase: InsertLoginDataUseCase,
    private val doLoginUseCase: DoLoginUseCase,
    @Assisted private val state: SavedStateHandle
) : BaseViewModel() {

    val loginData: LiveData<LoginData?> get() = _loginData
    private val _loginData = MutableLiveData<LoginData?>()

    val loginResponse: LiveData<Boolean> get() = _loginResponse
    private val _loginResponse = MutableLiveData<Boolean>()

    fun fetchLoginData() {
        launchAsync {
            handleResponse(
                responseState = getLoginDataUseCase.invoke(GetLoginDataUseCase.Request),
                shouldPostError = false
            ).run(_loginData::postValue)
        }
    }

    fun changeAuthMode(authMode: Int) {
        launchAsync {
            handleResponse(
                responseState = getLoginDataUseCase.invoke(GetLoginDataUseCase.Request),
                shouldPostError = false
            )?.run {
                handleResponse(
                    responseState = insertLoginDataUseCase.invoke(
                        InsertLoginDataUseCase.Request(
                            copy(defaultAuthMethod = authMode)
                        )
                    ),
                    shouldPostError = false
                )?.let { if (it) fetchLoginData() }
            }
        }
    }

    fun doLogin() {
        launchAsync {
            handleResponse(
                responseState = getLoginDataUseCase.invoke(GetLoginDataUseCase.Request),
                shouldPostError = false
            )?.run {
                launchAsync {
                    handleResponse(
                        responseState = doLoginUseCase.invoke(DoLoginUseCase.Request(this)),
                    )?.run(_loginResponse::postValue)
                }
            }
        }
    }
}
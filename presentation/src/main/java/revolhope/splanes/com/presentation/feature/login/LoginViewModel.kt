package revolhope.splanes.com.presentation.feature.login

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import revolhope.splanes.com.domain.model.LoginData
import revolhope.splanes.com.domain.usecase.DoLoginUseCase
import revolhope.splanes.com.domain.usecase.GetLoginDataUseCase
import revolhope.splanes.com.presentation.feature.common.base.BaseViewModel

class LoginViewModel @ViewModelInject constructor(
    private val getLoginDataUseCase: GetLoginDataUseCase,
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

    fun doLogin(data: LoginData) {
        launchAsync {
            handleResponse(
                responseState = doLoginUseCase.invoke(DoLoginUseCase.Request(data)),
            )?.run(_loginResponse::postValue)
        }
    }
}
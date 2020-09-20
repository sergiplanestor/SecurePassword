package revolhope.splanes.com.presentation.feature.login.authfragment.password

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import revolhope.splanes.com.domain.model.LoginData
import revolhope.splanes.com.domain.util.sha256
import revolhope.splanes.com.presentation.common.base.BaseViewModel

class LoginPasswordViewModel @ViewModelInject constructor(
    @Assisted private val state: SavedStateHandle
) : BaseViewModel() {

    val checkPasswordState: LiveData<Boolean> get() = _checkPasswordState
    private var _checkPasswordState = MutableLiveData<Boolean>()

    fun checkPassword(pwd: String, loginData: LoginData) {
        _checkPasswordState.value = loginData.pwd == sha256(pwd)
    }


}
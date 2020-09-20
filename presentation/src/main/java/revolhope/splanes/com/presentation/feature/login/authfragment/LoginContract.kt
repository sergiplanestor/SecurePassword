package revolhope.splanes.com.presentation.feature.login.authfragment

import revolhope.splanes.com.domain.model.LoginData

interface LoginContract {
    fun setLoginData(loginData: LoginData)
    fun setOnCredentialsValidated(callback: () -> Unit)
}
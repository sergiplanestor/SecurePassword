package revolhope.splanes.com.presentation.feature.login

import android.app.Activity
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import revolhope.splanes.com.domain.model.AuthenticationMethod
import revolhope.splanes.com.domain.model.LoginData
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.databinding.ActivityLoginBinding
import revolhope.splanes.com.presentation.extensions.*
import revolhope.splanes.com.presentation.common.base.BaseActivity
import revolhope.splanes.com.presentation.feature.login.authfragment.biometric.LoginBiometricFragment
import revolhope.splanes.com.presentation.feature.login.authfragment.password.LoginPasswordFragment
import revolhope.splanes.com.presentation.feature.login.authfragment.pattern.LoginPatternFragment
import revolhope.splanes.com.presentation.feature.login.authfragment.register.LoginRegisterFragment
import revolhope.splanes.com.presentation.feature.register.RegisterActivity

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    override val layoutResource: Int
        get() = R.layout.activity_login

    override fun initObservers() {
        super.initObservers()
        observe(viewModel.loginData, ::updateUI)
        observe(viewModel.loginResponse) {
            // TODO(navigate or fetch data)
        }
    }

    override fun loadData() {
        super.loadData()
        viewModel.fetchLoginData()
    }

    fun refreshUI() = loadData()

    private fun updateUI(loginData: LoginData?) {
        val contract = loginData?.let {
             when(it.defaultAuthMethod) {
                AuthenticationMethod.PASSWORD -> {
                    LoginPasswordFragment()
                }
                AuthenticationMethod.PATTERN -> {
                    LoginPatternFragment()
                }
                else /*AuthenticationMethod.BIOMETRIC*/ -> {
                    LoginBiometricFragment()
                }
            }.also { contract ->
                 contract.setLoginData(it)
                 contract.setOnCredentialsValidated(::onCredentialsValidated)
             }
        } ?: LoginRegisterFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.authContainer, contract)
            .commitAllowingStateLoss()
    }

    private fun onCredentialsValidated() {

    }
}
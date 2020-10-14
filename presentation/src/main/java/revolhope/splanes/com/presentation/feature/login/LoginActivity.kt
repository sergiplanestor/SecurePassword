package revolhope.splanes.com.presentation.feature.login

import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import revolhope.splanes.com.domain.model.AuthenticationMethod
import revolhope.splanes.com.domain.model.LoginData
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.databinding.ActivityLoginBinding
import revolhope.splanes.com.presentation.common.extensions.*
import revolhope.splanes.com.presentation.common.base.BaseActivity
import revolhope.splanes.com.presentation.common.dialog.DialogModel
import revolhope.splanes.com.presentation.common.dialog.showPickerDialog
import revolhope.splanes.com.presentation.common.dialog.showToast
import revolhope.splanes.com.presentation.feature.dashboard.DashboardActivity
import revolhope.splanes.com.presentation.feature.login.authfragment.biometric.LoginBiometricFragment
import revolhope.splanes.com.presentation.feature.login.authfragment.password.LoginPasswordFragment
import revolhope.splanes.com.presentation.feature.login.authfragment.pattern.LoginPatternFragment
import revolhope.splanes.com.presentation.feature.login.authfragment.register.LoginRegisterFragment
import revolhope.splanes.com.presentation.feature.login.authfragment.register.existingaccount.AlreadyHaveAccountBottomSheet

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    override val layoutResource: Int
        get() = R.layout.activity_login

    override fun initViews() {
        super.initViews()
        binding.alreadyHaveAccountButton.setOnClickListener {
            AlreadyHaveAccountBottomSheet(callback = viewModel::doLogin).show(supportFragmentManager)
        }
        binding.changeAuthButton.setOnClickListener {
            showPickerDialog(
                DialogModel.Picker(
                    title = getString(R.string.pick_auth_mode_title),
                    message = getString(R.string.pick_auth_mode_message),
                    positiveText = getString(R.string.pick),
                    items = listOf(
                        getString(R.string.password),
                        getString(R.string.biometrics),
                        getString(R.string.pattern)
                    ),
                    onItemSelected = {
                        val authMode = when (it) {
                            getString(R.string.password) -> {
                                AuthenticationMethod.PASSWORD
                            }
                            getString(R.string.biometrics) -> {
                                AuthenticationMethod.BIOMETRIC
                            }
                            getString(R.string.pattern) -> {
                                AuthenticationMethod.PATTERN
                            }
                            else -> AuthenticationMethod.PASSWORD
                        }
                        viewModel.changeAuthMode(authMode)
                    }
                )
            )
        }
    }

    override fun initObservers() {
        super.initObservers()
        observe(viewModel.loaderState) { if (it) showLoader() else hideLoader() }
        observe(viewModel.loginData, ::updateUI)
        observe(viewModel.loginResponse) {
            if (it) DashboardActivity.start(this) else showToast(R.string.error_generic)
        }
    }

    override fun loadData() {
        super.loadData()
        viewModel.fetchLoginData()
    }

    fun refreshUI() = loadData()

    private fun updateUI(loginData: LoginData?) {
        val contract = loginData?.let {
            binding.changeAuthButton.visible()
            when (it.defaultAuthMethod) {
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
                contract.setOnCredentialsValidated(viewModel::doLogin)
            }
        } ?: LoginRegisterFragment()
        addFragment(R.id.authContainer, contract)
    }
}
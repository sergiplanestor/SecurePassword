package revolhope.splanes.com.presentation.feature.login.authfragment.password

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import revolhope.splanes.com.domain.model.LoginData
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.common.base.BaseFragment
import revolhope.splanes.com.presentation.databinding.FragmentLoginPasswordBinding
import revolhope.splanes.com.presentation.extensions.observe
import revolhope.splanes.com.presentation.feature.login.authfragment.LoginContract

@AndroidEntryPoint
class LoginPasswordFragment : BaseFragment<FragmentLoginPasswordBinding>(), LoginContract {

    private val viewModel: LoginPasswordViewModel by viewModels()
    private lateinit var loginData: LoginData
    private lateinit var onCredentialsValidated: () -> Unit
    override val layoutResource: Int
        get() = R.layout.fragment_login_password

    override fun initViews() {
        super.initViews()
        binding.buttonLogin.setOnClickListener {
            binding.pwdInputLayout.error = null
            viewModel.checkPassword(
                pwd = binding.pwdTextInput.text.toString(),
                loginData = loginData
            )
        }
    }

    override fun initObservers() {
        super.initObservers()
        observe(viewModel.checkPasswordState) {
            if (it) {
                onCredentialsValidated.invoke()
            } else {
                binding.pwdInputLayout.error = getString(R.string.error_password_not_match)
            }
        }
    }

    override fun setLoginData(loginData: LoginData) {
        this.loginData = loginData
    }

    override fun setOnCredentialsValidated(callback: () -> Unit) {
        this.onCredentialsValidated = callback
    }
}
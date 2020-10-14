package revolhope.splanes.com.presentation.feature.login.authfragment.register

import android.app.Activity
import revolhope.splanes.com.domain.model.LoginData
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.common.base.BaseActivity
import revolhope.splanes.com.presentation.common.base.BaseFragment
import revolhope.splanes.com.presentation.databinding.FragmentLoginRegisterBinding
import revolhope.splanes.com.presentation.feature.login.LoginActivity
import revolhope.splanes.com.presentation.feature.login.authfragment.LoginContract
import revolhope.splanes.com.presentation.feature.register.RegisterActivity

class LoginRegisterFragment : BaseFragment<FragmentLoginRegisterBinding>(), LoginContract {

    private lateinit var loginData: LoginData
    private lateinit var onCredentialsValidated: () -> Unit

    override val layoutResource: Int
        get() = R.layout.fragment_login_register

    override fun initViews() {
        super.initViews()
        binding.registerButton.setOnClickListener { onRegisterClick() }
    }

    private fun onRegisterClick() {
        (activity as? BaseActivity<*>)?.let { activity ->
            RegisterActivity.getIntent(activity)?.let {
                activity.startActivityForResult(
                    intent = it,
                    requestCode = RegisterActivity.REQ_CODE
                ) { _, _, resultCode ->
                    if (resultCode == Activity.RESULT_OK) {
                        (activity as? LoginActivity)?.refreshUI()
                    }
                }
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
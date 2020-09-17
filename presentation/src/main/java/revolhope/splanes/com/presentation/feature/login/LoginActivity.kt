package revolhope.splanes.com.presentation.feature.login

import android.app.Activity
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.databinding.ActivityLoginBinding
import revolhope.splanes.com.presentation.extensions.*
import revolhope.splanes.com.presentation.feature.common.base.BaseActivity
import revolhope.splanes.com.presentation.feature.register.RegisterActivity

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    override val layoutResource: Int
        get() = R.layout.activity_login

    override fun initViews() {
        super.initViews()
        binding.loginHelp.makeLinks(
            links = arrayOf(
                Pair(
                    getString(R.string.click_here),
                    {

                    }
                )
            )
        )
        binding.registerButton.setOnClickListener { onRegisterClick() }
    }

    override fun initObservers() {
        super.initObservers()
        observe(viewModel.loginData) {
            it?.let(viewModel::doLogin) ?: updateUI()
        }
        observe(viewModel.loginResponse) {
            // TODO(navigate or fetch data)
        }
    }

    override fun loadData() {
        super.loadData()
        viewModel.fetchLoginData()
    }

    private fun onRegisterClick() {
        startActivityForResult(
            intent = RegisterActivity.getIntent(this),
            requestCode = RegisterActivity.REQ_CODE
        ) { data, _, resultCode ->
            if (resultCode == Activity.RESULT_OK) {

            }
        }
    }

    private fun updateUI() {
        binding.loginHelp.gone()
        binding.fingerIcon.gone()
        binding.registerHelp.visible()
        binding.registerButton.visible()
    }
}
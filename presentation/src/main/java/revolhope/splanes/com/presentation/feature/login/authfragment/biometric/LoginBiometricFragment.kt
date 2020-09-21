package revolhope.splanes.com.presentation.feature.login.authfragment.biometric

import android.content.res.ColorStateList
import android.os.Build
import dagger.hilt.android.AndroidEntryPoint
import revolhope.splanes.com.domain.model.LoginData
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.common.base.BaseFragment
import revolhope.splanes.com.presentation.common.dialog.showToast
import revolhope.splanes.com.presentation.common.extensions.visibility
import revolhope.splanes.com.presentation.databinding.FragmentLoginBiometricBinding
import revolhope.splanes.com.presentation.feature.login.authfragment.LoginContract
import revolhope.splanes.com.presentation.util.biometric.BiometricResponseModel
import revolhope.splanes.com.presentation.util.biometric.BiometricUtils
import javax.inject.Inject

@AndroidEntryPoint
class LoginBiometricFragment : BaseFragment<FragmentLoginBiometricBinding>(), LoginContract {

    @Inject
    lateinit var biometricUtils: BiometricUtils
    private lateinit var loginData: LoginData
    private lateinit var onCredentialsValidated: () -> Unit

    override val layoutResource: Int
        get() = R.layout.fragment_login_biometric

    override fun initViews() {
        super.initViews()
        binding.retryButton.setOnClickListener {
            binding.fingerImage.imageTintList =
                ColorStateList.valueOf(requireContext().getColor(R.color.colorPrimaryDark))
            onResume()
        }
    }

    override fun onResume() {
        super.onResume()
        biometricUtils.authenticate(
            fragment = this,
            responseModel = BiometricResponseModel(
                onSuccess = onCredentialsValidated,
                onFail = ::onBiometricError,
                onError = ::onBiometricError
            )
        )
    }

    private fun onBiometricError(message: String? = null) {
        context?.showToast(message ?: requireContext().getString(R.string.error_biometrics_invalid))
        binding.retryButton.visibility(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        binding.fingerImage.imageTintList =
            ColorStateList.valueOf(requireContext().getColor(R.color.pomegranate))
    }

    override fun setLoginData(loginData: LoginData) {
        this.loginData = loginData
    }

    override fun setOnCredentialsValidated(callback: () -> Unit) {
        this.onCredentialsValidated = callback
    }
}
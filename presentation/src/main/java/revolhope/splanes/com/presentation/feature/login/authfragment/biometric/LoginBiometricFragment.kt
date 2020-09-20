package revolhope.splanes.com.presentation.feature.login.authfragment.biometric

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import revolhope.splanes.com.domain.model.LoginData
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.common.base.BaseFragment
import revolhope.splanes.com.presentation.common.dialog.showToast
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
        biometricUtils.authenticate(
            activity = requireActivity(),
            responseModel = BiometricResponseModel(
                onSuccess = onCredentialsValidated,
                onFail = {
                    context?.showToast("T_Biometric fails")
                },
                onError = {
                    context?.showToast("T_Biometric error")
                }
            )
        )
    }

    override fun setLoginData(loginData: LoginData) {
        this.loginData = loginData
    }

    override fun setOnCredentialsValidated(callback: () -> Unit) {
        this.onCredentialsValidated = callback
    }
}
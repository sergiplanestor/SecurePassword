package revolhope.splanes.com.presentation.util.biometric

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.domain.util.cryptographic.CryptographyUtils
import javax.inject.Inject


class BiometricUtils @Inject constructor(private val cryptographyUtils: CryptographyUtils) {

    fun canAuthenticate(context: Context): Pair<Boolean, Int?> {
        if (!checkGrantedPermissions(context)) {
            return false to R.string.error_biometrics_no_permissions
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return context.getSystemService(BiometricManager::class.java)?.let {
                when (it.canAuthenticate()) {
                    BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                        false to R.string.error_biometrics_currently_unavailable
                    }
                    BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                        false to R.string.error_biometrics_currently_unavailable
                    }
                    BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                        false to R.string.error_biometrics_non_hardware
                    }
                    else /* BiometricManager.BIOMETRIC_SUCCESS */ -> {
                        true to null
                    }
                }
            } ?: false to R.string.error_biometrics_generic
        } else {
            return context.getSystemService(FingerprintManager::class.java)?.let {
                if (!it.isHardwareDetected) {
                    false to R.string.error_biometrics_non_hardware
                }
                if (!it.hasEnrolledFingerprints()) {
                    false to R.string.error_biometrics_currently_unavailable
                }
                true to null
            } ?: false to R.string.error_biometrics_generic
        }
    }

    private fun checkGrantedPermissions(context: Context) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.USE_BIOMETRIC
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.USE_FINGERPRINT
            ) == PackageManager.PERMISSION_GRANTED
        }


    fun authenticate(fragment: Fragment, responseModel: BiometricResponseModel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            authenticateSdkQ(fragment, responseModel)
        } else {
            authenticateSdkLessThanQ(fragment, responseModel)
        }
    }

    private fun authenticateSdkQ(
        fragment: Fragment,
        responseModel: BiometricResponseModel
    ) {
        val executor = ContextCompat.getMainExecutor(fragment.requireContext())
        BiometricPrompt(
            fragment,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    responseModel.onError.invoke(errString.toString())
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    responseModel.onSuccess.invoke()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    responseModel.onFail.invoke()
                }
            }
        ).run {
            authenticate(
                BiometricPrompt.PromptInfo.Builder()
                    .setTitle(fragment.getString(R.string.biometrics))
                    .setSubtitle(fragment.getString(R.string.biometric_help))
                    .setNegativeButtonText(fragment.getString(R.string.cancel))
                    .build()
            )
        }
    }

    private fun authenticateSdkLessThanQ(
        fragment: Fragment,
        responseModel: BiometricResponseModel
    ) {
        cryptographyUtils.fingerprintAuth(
            fingerprintManager = fragment.requireActivity().getSystemService(FingerprintManager::class.java),
            onSuccess = responseModel.onSuccess,
            onError = responseModel.onError,
            onFail = responseModel.onFail,
        )
    }
}
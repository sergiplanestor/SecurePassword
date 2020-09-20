package revolhope.splanes.com.presentation.util.cryptographic

import android.hardware.fingerprint.FingerprintManager
import android.os.CancellationSignal
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.inject.Inject

class CryptographyUtils @Inject constructor() {

    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val FINGERPRINT_KEY = "FingerprintKey"
        private const val TRANSFORMATION =
            KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7
    }

    private var keyStore: KeyStore? = null

    init {
        try {
            keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
            keyStore?.load(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun generateKey(keyName: String, userAuthRequired: Boolean = false) {
        try {
            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
            keyGenerator?.init(
                KeyGenParameterSpec.Builder(
                    keyName,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                ).apply {
                    setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    setUserAuthenticationRequired(userAuthRequired)
                    setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                }.build()
            )
            keyGenerator?.generateKey()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun generateCipher(
        keyName: String,
        mode: Int
    ): Cipher? {
        return try {
            Cipher.getInstance(TRANSFORMATION).apply {
                keyStore?.load(null)
                init(
                    mode,
                    keyStore?.getKey(
                        keyName,
                        null
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun existsKey(keyName: String): Boolean {
        keyStore?.load(null)
        return keyStore?.containsAlias(keyName) ?: false
    }

    fun fingerprintAuth(
        fingerprintManager: FingerprintManager,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onFail: () -> Unit
    ) {
        if (!existsKey(FINGERPRINT_KEY)) generateKey(FINGERPRINT_KEY, true)
        generateCipher(FINGERPRINT_KEY, Cipher.ENCRYPT_MODE)?.let(FingerprintManager::CryptoObject)
        fingerprintManager.authenticate(
            generateCipher(
                FINGERPRINT_KEY,
                Cipher.ENCRYPT_MODE
            )?.let(FingerprintManager::CryptoObject),
            CancellationSignal(),
            0,
            object : FingerprintManager.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    onError.invoke(errString?.toString() ?: "")
                }

                override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess.invoke()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFail.invoke()
                }
            },
            null
        )
    }

    fun encrypt() {

    }

    fun decrypt() {

    }

}
package revolhope.splanes.com.domain.util.cryptographic

import android.hardware.fingerprint.FingerprintManager
import android.os.CancellationSignal
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import revolhope.splanes.com.domain.BuildConfig
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class CryptographyUtils @Inject constructor() {

    companion object {
        private val secretKey = BuildConfig.SECRET.decode().let {
            SecretKeySpec(
                it,
                0,
                it.size,
                KeyProperties.KEY_ALGORITHM_AES
            )
        }
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

    private fun generateFingerKey(): SecretKey? {
        try {
            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
            keyGenerator?.init(
                KeyGenParameterSpec.Builder(
                    FINGERPRINT_KEY,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                ).apply {
                    setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    setUserAuthenticationRequired(true)
                    setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    setKeySize(256)
                }.build()
            )
            return keyGenerator?.generateKey()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun generateCipher(
        keyName: String,
        mode: Int,
        iv: ByteArray? = null
    ): Cipher? {
        return try {
            Cipher.getInstance(TRANSFORMATION).apply {
                keyStore?.load(null)
                if (iv != null) {
                    init(mode, keyStore?.getKey(keyName, null), IvParameterSpec(iv))
                } else {
                    init(mode, keyStore?.getKey(keyName, null))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun existsFingerKey(): Boolean {
        keyStore?.load(null)
        return keyStore?.containsAlias(FINGERPRINT_KEY) ?: false
    }

    @Suppress("DEPRECATION")
    fun fingerprintAuth(
        fingerprintManager: FingerprintManager,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onFail: () -> Unit
    ) {
        if (!existsFingerKey()) generateFingerKey()
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

    fun encrypt(value: String): CryptographyModel =
        value.toByteArray().let { bytes ->
            var iv1: ByteArray
            var iv2: ByteArray

            val key = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES).apply {
                init(256)
            }.generateKey()

            val rawData = Cipher.getInstance(TRANSFORMATION).apply {
                init(Cipher.ENCRYPT_MODE, key)
            }.run {
                val raw = doFinal(bytes)
                iv1 = this.iv
                raw.encode()
            }

            val secret = Cipher.getInstance(TRANSFORMATION).apply {
                init(Cipher.ENCRYPT_MODE, secretKey)
            }.run {
                val raw = doFinal(key.encoded)
                iv2 = this.iv
                raw.encode()
            }

            CryptographyModel(
                raw = rawData,
                secret = secret,
                ivRaw = iv1.encode(),
                ivSecret = iv2.encode()
            )
        }

    fun decrypt(model: CryptographyModel): String =
        Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(model.ivSecret.decode()))
        }.doFinal(model.secret.decode()).let {
            Cipher.getInstance(TRANSFORMATION).apply {
                init(
                    Cipher.DECRYPT_MODE, SecretKeySpec(
                        it,
                        0,
                        it.size,
                        KeyProperties.KEY_ALGORITHM_AES
                    ), IvParameterSpec(model.ivRaw.decode())
                )
            }.doFinal(model.raw.decode())
        }.let { String(it) }

}
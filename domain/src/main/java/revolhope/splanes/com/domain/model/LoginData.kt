package revolhope.splanes.com.domain.model

import androidx.annotation.IntDef
import java.io.Serializable

data class LoginData(
    val email: String,
    val pwd: String,
    val pattern: String,
    @AuthenticationMethod val defaultAuthMethod: Int,
    val createdOn: Long,
    val lastAccessOn: Long
): Serializable

@IntDef(AuthenticationMethod.PASSWORD, AuthenticationMethod.BIOMETRIC, AuthenticationMethod.PATTERN)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class AuthenticationMethod {
    companion object {

        const val PASSWORD = 0

        const val BIOMETRIC = 1

        const val PATTERN = 2
    }
}
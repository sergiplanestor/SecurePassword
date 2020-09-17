package revolhope.splanes.com.data.response

data class LoginDataResponse(
    val email: String?,
    val pwd: String?,
    val pattern: String?,
    val defaultAuthMethod: Int,
    val createdOn: Long?,
    val lastAccessOn: Long?
)
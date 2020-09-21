package revolhope.splanes.com.domain.util.cryptographic

data class CryptographyModel(
    val raw: String,
    val secret: String,
    val ivRaw: String,
    val ivSecret: String
)
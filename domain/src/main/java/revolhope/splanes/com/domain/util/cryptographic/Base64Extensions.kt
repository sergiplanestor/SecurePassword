package revolhope.splanes.com.domain.util.cryptographic

import android.util.Base64


fun ByteArray.encode(): String = Base64.encodeToString(this, Base64.DEFAULT)

fun String.decode(): ByteArray = Base64.decode(toByteArray(), Base64.DEFAULT)
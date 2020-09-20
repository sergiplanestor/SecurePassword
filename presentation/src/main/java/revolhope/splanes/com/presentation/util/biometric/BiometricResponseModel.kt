package revolhope.splanes.com.presentation.util.biometric

data class BiometricResponseModel(
    val onSuccess: () -> Unit,
    val onFail: () -> Unit,
    val onError: (reason: String) -> Unit
)
package revolhope.splanes.com.domain.network

sealed class ResponseState<T> {
    data class Success<T>(val data: T) : ResponseState<T>()
    data class Error<T>(val message: String? = null, val throwable: Throwable? = null) : ResponseState<T>()
}
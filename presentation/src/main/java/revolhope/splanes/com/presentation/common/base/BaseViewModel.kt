package revolhope.splanes.com.presentation.common.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import revolhope.splanes.com.domain.network.ResponseState
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {

    val loaderState: LiveData<Boolean> get() = _loaderState
    private val _loaderState = MutableLiveData<Boolean>()

    val errorState: LiveData<String> get() = _errorState
    private val _errorState = MutableLiveData<String>()

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    protected fun launchAsync(
        showLoader: Boolean = true,
        closure: suspend () -> Unit
    ) {
        if (showLoader) _loaderState.postValue(true)
        launch {
            withContext(Dispatchers.IO) {
                closure.invoke()
            }.also { _loaderState.postValue(false) }
        }
    }

    protected fun <T> handleResponse(
        responseState: ResponseState<T>,
        shouldPostError: Boolean = true
    ): T? =
        when (responseState) {
            is ResponseState.Success -> {
                responseState.data
            }
            is ResponseState.Error -> {
                if (shouldPostError) {
                    _errorState.postValue(
                        if (!responseState.message.isNullOrBlank()) {
                            responseState.message
                        } else {
                            responseState.throwable?.message ?: ""
                        }
                    )
                }
                responseState.throwable?.printStackTrace()
                null
            }
        }
}
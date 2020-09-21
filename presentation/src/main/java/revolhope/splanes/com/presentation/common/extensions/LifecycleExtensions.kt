package revolhope.splanes.com.presentation.common.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun <T> LifecycleOwner.observe(
    liveData: LiveData<T>,
    closure: (T) -> Unit
) {
    liveData.observe(this, { closure.invoke(it) })
}
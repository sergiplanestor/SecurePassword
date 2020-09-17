package revolhope.splanes.com.domain.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun ViewModel.launchAsync (action: suspend () -> Unit) =
    viewModelScope.launch(Dispatchers.IO) { action.invoke() }
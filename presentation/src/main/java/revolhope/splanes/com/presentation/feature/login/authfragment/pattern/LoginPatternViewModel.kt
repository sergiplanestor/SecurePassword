package revolhope.splanes.com.presentation.feature.login.authfragment.pattern

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.utils.PatternLockUtils
import revolhope.splanes.com.domain.model.LoginData
import revolhope.splanes.com.presentation.common.base.BaseViewModel

class LoginPatternViewModel @ViewModelInject constructor(
    @Assisted private val state: SavedStateHandle
) : BaseViewModel() {

    val patternState: LiveData<Boolean> get() = _patternState
    private var _patternState = MutableLiveData<Boolean>()

    fun checkPattern(
        view: PatternLockView,
        loginData: LoginData,
        dots: MutableList<PatternLockView.Dot>?
    ) {
        _patternState.value = dots?.let {
            loginData.pattern == PatternLockUtils.patternToMD5(view, dots)
        } ?: false
    }
}
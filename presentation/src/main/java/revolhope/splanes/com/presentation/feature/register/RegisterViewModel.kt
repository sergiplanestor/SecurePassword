package revolhope.splanes.com.presentation.feature.register

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.utils.PatternLockUtils
import revolhope.splanes.com.domain.model.AuthenticationMethod
import revolhope.splanes.com.domain.model.LoginData
import revolhope.splanes.com.domain.usecase.InsertLoginDataUseCase
import revolhope.splanes.com.domain.util.sha256
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.common.base.BaseViewModel

class RegisterViewModel @ViewModelInject constructor(
    @Assisted private val state: SavedStateHandle,
    private val insertLoginDataUseCase: InsertLoginDataUseCase
) : BaseViewModel() {

    val patternState: LiveData<Pair<Boolean, String>> get() = _patternState
    private var _patternState = MutableLiveData<Pair<Boolean, String>>()

    val emailError: LiveData<Pair<Boolean, String>> get() = _emailError
    private var _emailError = MutableLiveData<Pair<Boolean, String>>()

    val patternError: LiveData<Boolean> get() = _patternError
    private var _patternError = MutableLiveData<Boolean>()

    val pwdError: LiveData<Boolean> get() = _pwdError
    private var _pwdError = MutableLiveData<Boolean>()

    val registerState: LiveData<Boolean> get() = _registerState
    private var _registerState = MutableLiveData<Boolean>()

    fun checkPattern(
        context: Context,
        view: PatternLockView,
        dots: MutableList<PatternLockView.Dot>?
    ) {
        dots?.let {
            if (it.size < 4) {
                _patternState.value = false to context.getString(R.string.error_pattern_size)
            } else {
                _patternState.value = true to PatternLockUtils.patternToMD5(view, dots)
            }
        } ?: _patternState.postValue(false to context.getString(R.string.error_pattern_generic))
    }

    fun storeCredentials(
        context: Context,
        email: String?,
        pwd: String?,
        pattern: String?,
        mode: Int
    ) {
        var hasErrors = false
        when {
            email.isNullOrBlank() -> {
                _emailError.value = false to context.getString(R.string.error_blank_field)
                hasErrors = true
            }
            !email.matches(Regex(".*@.*(\\.com|\\.es)")) -> {
                _emailError.value = false to context.getString(R.string.error_email_pattern)
                hasErrors = true
            }
            else -> {
                _emailError.value = true to ""
            }
        }
        if (pattern == null) {
            _patternError.value = true
            hasErrors = true
        } else {
            _patternError.value = false
        }
        if (mode == AuthenticationMethod.PASSWORD && pwd.isNullOrBlank()) {
            _pwdError.value = true
            hasErrors = true
        } else {
            _pwdError.value = false
        }
        if (hasErrors) return

        launchAsync {
            handleResponse(
                responseState = insertLoginDataUseCase.invoke(
                    InsertLoginDataUseCase.Request(
                        LoginData(
                            email = email ?: "",
                            pwd = pwd?.run(::sha256) ?: "",
                            pattern = pattern ?: "",
                            defaultAuthMethod = mode,
                            createdOn = System.currentTimeMillis(),
                            lastAccessOn = System.currentTimeMillis()
                        )
                    )
                )
            )?.let(_registerState::postValue)
        }
    }
}
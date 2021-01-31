package revolhope.splanes.com.presentation.feature.register

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import dagger.hilt.android.AndroidEntryPoint
import revolhope.splanes.com.domain.model.AuthenticationMethod
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.common.base.BaseActivity
import revolhope.splanes.com.presentation.common.dialog.DialogModel
import revolhope.splanes.com.presentation.common.dialog.showPickerDialog
import revolhope.splanes.com.presentation.common.dialog.showToast
import revolhope.splanes.com.presentation.common.extensions.justify
import revolhope.splanes.com.presentation.databinding.ActivityRegisterBinding
import revolhope.splanes.com.presentation.common.extensions.observe
import revolhope.splanes.com.presentation.util.biometric.BiometricUtils
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    @Inject
    lateinit var biometricUtils: BiometricUtils
    private val viewModel: RegisterViewModel by viewModels()
    private var pattern: String? = null

    @AuthenticationMethod
    private var authMode: Int = AuthenticationMethod.PASSWORD

    override val layoutResource: Int
        get() = R.layout.activity_register

    companion object {
        private const val PATTERN_DELAY = 750L
        const val REQ_CODE = 0x459
        fun getIntent(activity: BaseActivity<*>?) =
            activity?.let {
                Intent(activity, RegisterActivity::class.java).apply {
                    putExtras(
                        bundleOf(
                            EXTRA_NAVIGATION_TRANSITION to NavTransition.LATERAL
                        )
                    )
                }
            }
    }

    override fun initObservers() {
        super.initObservers()
        observe(viewModel.patternState, ::onPatternStateReceived)
        observe(viewModel.patternError, ::onPatternError)
        observe(viewModel.emailError, ::onEmailError)
        observe(viewModel.pwdError, ::onPasswordError)
        observe(viewModel.storeCredentialsState, ::onStoreCredentialsStateReceived)
        observe(viewModel.registerState, ::onRegisterStateReceived)
    }

    override fun initViews() {
        super.initViews()

        binding.currentAuthMode.text = getString(
            R.string.register_default_auth_mode,
            getString(R.string.password)
        )
        initListeners()
        justifyTexts()
    }

    private fun initListeners() {
        binding.backButton.setOnClickListener { onBackPressed() }
        binding.patternLockView.addPatternLockListener(object : PatternLockViewListener {

            override fun onStarted() {
                binding.patternLockView.normalStateColor = getColor(R.color.colorPrimaryDark)
            }

            override fun onProgress(progressPattern: MutableList<PatternLockView.Dot>?) {
                binding.patternLockView.setViewMode(
                    if (progressPattern != null && progressPattern.size < 4) {
                        PatternLockView.PatternViewMode.WRONG
                    } else {
                        PatternLockView.PatternViewMode.CORRECT
                    }
                )
            }

            override fun onComplete(pattern: MutableList<PatternLockView.Dot>?) {
                viewModel.checkPattern(
                    context = this@RegisterActivity,
                    view = binding.patternLockView,
                    dots = pattern
                )
                binding.patternLockView.correctStateColor
                binding.patternLockView.postDelayed(
                    { binding.patternLockView.clearPattern() },
                    PATTERN_DELAY
                )
            }

            override fun onCleared() {
            }
        })
        binding.authModeButton.setOnClickListener {
            showPickerDialog(
                DialogModel.Picker(
                    title = getString(R.string.pick_auth_mode_title),
                    message = getString(R.string.pick_auth_mode_message),
                    positiveText = getString(R.string.pick),
                    items = listOf(
                        getString(R.string.password),
                        getString(R.string.biometrics),
                        getString(R.string.pattern)
                    ),
                    onItemSelected = {
                        binding.currentAuthMode.text = getString(
                            R.string.register_default_auth_mode,
                            it
                        )
                        authMode = when (it) {
                            getString(R.string.password) -> {
                                AuthenticationMethod.PASSWORD
                            }
                            getString(R.string.biometrics) -> {
                                AuthenticationMethod.BIOMETRIC
                            }
                            getString(R.string.pattern) -> {
                                AuthenticationMethod.PATTERN
                            }
                            else -> AuthenticationMethod.PASSWORD
                        }

                    }
                )
            )
        }
        binding.storeButton.setOnClickListener {
            viewModel.storeCredentials(
                email = binding.emailTextInput.text?.toString(),
                pwd = binding.pwdTextInput.text?.toString(),
                pattern = pattern,
                mode = authMode
            )
        }
    }

    private fun justifyTexts() {
        binding.pwdTitle.justify()
        binding.patternTitle.justify()
        binding.currentAuthMode.justify()
    }

    /**
     * @param state Pair containing if pattern is ok as first param and, in case of patter ok
     * second param contain pattern, in case of false, contains error message.
     */
    private fun onPatternStateReceived(state: Pair<Boolean, String>) {
        if (state.first) {
            pattern = state.second
        } else {
            pattern = null
            showToast(state.second)
        }
        binding.patternLockView.postDelayed(
            { binding.patternLockView.clearPattern() },
            PATTERN_DELAY
        )
    }

    private fun onPatternError(isOk: Boolean) {
        binding.patternLockView.normalStateColor = if (isOk) {
            getColor(android.R.color.holo_green_dark)
        } else {
            getColor(R.color.pomegranate)
        }
        binding.patternLockView.invalidate()
    }

    /**
     * @param state Pair containing if email is ok as first param and, in case of email KO
     * second param contains error message as string resource id.
     */
    private fun onEmailError(state: Pair<Boolean, Int>) {
        binding.emailInputLayout.error = if (state.first) null else getString(state.second)
    }

    /**
     * @param state Pair containing if password is ok as first param and, in case of password KO
     * second param contains error message as string resource id.
     */
    private fun onPasswordError(state: Pair<Boolean, Int>) {
        binding.pwdInputLayout.error = if (state.first) null else getString(state.second)
    }

    private fun onStoreCredentialsStateReceived(isSuccess: Boolean) {
        if (isSuccess) {
            viewModel.registerUser()
        } else {
            showToast(R.string.error_storing_credentials)
        }
    }

    private fun onRegisterStateReceived(isSuccess: Boolean) {
        if (isSuccess) {
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            showToast(R.string.error_storing_credentials)
        }
    }
}
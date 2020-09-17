package revolhope.splanes.com.presentation.feature.register

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import dagger.hilt.android.AndroidEntryPoint
import revolhope.splanes.com.domain.model.AuthenticationMethod
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.databinding.ActivityRegisterBinding
import revolhope.splanes.com.presentation.extensions.observe
import revolhope.splanes.com.presentation.feature.common.base.BaseActivity
import revolhope.splanes.com.presentation.feature.common.dialog.DialogModel
import revolhope.splanes.com.presentation.feature.common.dialog.showPickerDialog
import revolhope.splanes.com.presentation.feature.common.dialog.showToast

@AndroidEntryPoint
class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    private val viewModel: RegisterViewModel by viewModels()
    private var pattern: String? = null

    @AuthenticationMethod
    private var authMode: Int = AuthenticationMethod.PASSWORD

    override val layoutResource: Int
        get() = R.layout.activity_register

    companion object {
        private const val PATTERN_DELAY = 750L
        const val REQ_CODE = 0x459
        fun getIntent(activity: BaseActivity<*>) =
            Intent(activity, RegisterActivity::class.java).apply {
                putExtras(
                    bundleOf(
                        EXTRA_NAVIGATION_TRANSITION to NavTransition.LATERAL
                    )
                )
            }
    }

    override fun initViews() {
        super.initViews()
        binding.backButton.setOnClickListener { onBackPressed() }
        binding.currentAuthMode.text = getString(
            R.string.register_default_auth_mode,
            getString(R.string.password)
        )
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
                context = this,
                email = binding.emailTextInput.text?.toString(),
                pwd = binding.pwdTextInput.text?.toString(),
                pattern = pattern,
                mode = authMode
            )
        }
    }

    override fun initObservers() {
        super.initObservers()
        observe(viewModel.patternState) {
            if (it.first) {
                pattern = it.second
            } else {
                pattern = null
                Toast.makeText(this, it.second, Toast.LENGTH_LONG).show()
            }
            binding.patternLockView.postDelayed(
                { binding.patternLockView.clearPattern() },
                PATTERN_DELAY
            )
        }
        observe(viewModel.patternError) {
            if (it) {
                binding.patternLockView.normalStateColor = getColor(android.R.color.holo_green_dark)
            } else {
                binding.patternLockView.normalStateColor = getColor(R.color.pomegranate)
            }
            binding.patternLockView.invalidate()
        }
        observe(viewModel.emailError) {
            if (it.first) {
                binding.emailInputLayout.error = null
            } else {
                binding.emailInputLayout.error = it.second
            }
        }
        observe(viewModel.pwdError) {
            binding.pwdInputLayout.error = if (it) getString(R.string.error_pwd_selected) else null
        }
        observe(viewModel.registerState) { registerSuccess ->
            if (registerSuccess) {
                setResult(Activity.RESULT_OK)
                onBackPressed()
            } else {
                showToast(R.string.error_storing_credentials)
            }
        }
    }
}
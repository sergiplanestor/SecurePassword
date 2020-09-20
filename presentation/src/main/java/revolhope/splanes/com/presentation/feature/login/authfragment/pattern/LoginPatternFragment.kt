package revolhope.splanes.com.presentation.feature.login.authfragment.pattern

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import revolhope.splanes.com.domain.model.LoginData
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.common.base.BaseFragment
import revolhope.splanes.com.presentation.common.dialog.showToast
import revolhope.splanes.com.presentation.databinding.FragmentLoginPatternBinding
import revolhope.splanes.com.presentation.extensions.observe
import revolhope.splanes.com.presentation.feature.login.authfragment.LoginContract

class LoginPatternFragment : BaseFragment<FragmentLoginPatternBinding>(), LoginContract {

    private val viewModel: LoginPatternViewModel by viewModels()
    private lateinit var loginData: LoginData
    private lateinit var onCredentialsValidated: () -> Unit

    override val layoutResource: Int
        get() = R.layout.fragment_login_pattern

    companion object {
        private const val PATTERN_DELAY = 750L
    }

    override fun initViews() {
        super.initViews()
        binding.patternLockView.addPatternLockListener(object : PatternLockViewListener {

            override fun onStarted() {
                binding.patternLockView.normalStateColor = requireContext().getColor(R.color.colorPrimaryDark)
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
                    view = binding.patternLockView,
                    loginData = loginData,
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
    }

    override fun initObservers() {
        super.initObservers()
        observe(viewModel.patternState) {
            if (it) {
                onCredentialsValidated.invoke()
            } else {
                context?.showToast("T_Pattern error!")
            }
            binding.patternLockView.postDelayed(
                { binding.patternLockView.clearPattern() },
                PATTERN_DELAY
            )
        }
    }

    override fun setLoginData(loginData: LoginData) {
        this.loginData = loginData
    }

    override fun setOnCredentialsValidated(callback: () -> Unit) {
        this.onCredentialsValidated = callback
    }
}
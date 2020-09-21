package revolhope.splanes.com.presentation.feature.dashboard

import android.content.Intent
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import dagger.hilt.android.AndroidEntryPoint
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.common.base.BaseActivity
import revolhope.splanes.com.presentation.databinding.ActivityDashboardBinding
import revolhope.splanes.com.domain.util.cryptographic.CryptographyUtils
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : BaseActivity<ActivityDashboardBinding>() {

    @Inject
    lateinit var cryptographyUtils: CryptographyUtils

    private val viewModel: DashboardViewModel by viewModels()

    override val layoutResource: Int
        get() = R.layout.activity_dashboard

    companion object {
        fun start(baseActivity: BaseActivity<*>) {
            baseActivity.startActivity(
                Intent(baseActivity, DashboardActivity::class.java).apply {
                    putExtras(
                        bundleOf(
                            EXTRA_NAVIGATION_TRANSITION to NavTransition.UP
                        )
                    )
                }
            )
        }
    }

    override fun initViews() {
        super.initViews()

    }
}
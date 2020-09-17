package revolhope.splanes.com.presentation.feature.common.base

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import revolhope.splanes.com.presentation.R

abstract class BaseActivity<T: ViewDataBinding> : AppCompatActivity() {

    abstract val layoutResource: Int
    lateinit var binding: T
    private var isFirstOnResume: Boolean = true
    private var onActivityResultMap: MutableMap<Int, (Intent?, Int, Int) -> Unit> = mutableMapOf()

    companion object {
        const val EXTRA_NAVIGATION_TRANSITION = "nav.transition"
    }

    enum class NavTransition {
        LATERAL,
        UP,
        MODAL,
        FADE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResource)
        initViews()
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        if (isFirstOnResume) {
            loadData()
            isFirstOnResume = isFirstOnResume.not()
        } else {
            reloadData()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overrideTransition()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                overrideTransition()
                return true
            }
        }
        return item?.let { super.onOptionsItemSelected(item) } ?: false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        this.onActivityResultMap[requestCode]?.invoke(data, requestCode, resultCode)
        this.onActivityResultMap.remove(requestCode)
    }

    protected open fun initViews() {
        // Nothing to do here
    }

    protected open fun initObservers() {
        // Nothing to do here
    }

    protected open fun loadData() {
        // Nothing to do here
    }

    protected open fun reloadData() {
        // Nothing to do here
    }

    override fun startActivity(intent: Intent?) {
        val anim = getNavAnimations(intent)
        super.startActivity(intent)
        overridePendingTransition(anim.first, anim.second)
    }

    fun startActivityForResult(
        intent: Intent?,
        requestCode: Int,
        onActivityResult: (data: Intent?, requestCode: Int, resultCode: Int) -> Unit
    ) {
        this.onActivityResultMap[requestCode] = onActivityResult
        val anim = getNavAnimations(intent)
        super.startActivityForResult(intent, requestCode)
        overridePendingTransition(anim.first, anim.second)
    }

    private fun getNavAnimations(intent: Intent?, isStart: Boolean = true): Pair<Int, Int> {
        val bundle = intent?.extras
        return when (bundle?.getSerializable(EXTRA_NAVIGATION_TRANSITION) as NavTransition?) {
            NavTransition.LATERAL ->
                if (isStart) {
                    bundle?.putSerializable(EXTRA_NAVIGATION_TRANSITION, NavTransition.LATERAL)
                    Pair(R.anim.slide_in_right, R.anim.slide_out_left)
                } else {
                    Pair(R.anim.slide_in_left, R.anim.slide_out_right)
                }
            NavTransition.UP -> Pair(R.anim.slide_in_up, R.anim.slide_out_up)
            NavTransition.MODAL ->
                if (isStart) {
                    bundle?.putSerializable(EXTRA_NAVIGATION_TRANSITION, NavTransition.MODAL)
                    Pair(R.anim.slide_in_up, android.R.anim.fade_out)
                } else {
                    Pair(android.R.anim.fade_in, R.anim.slide_out_down)
                }
            NavTransition.FADE ->
                if (isStart) {
                    bundle?.putSerializable(EXTRA_NAVIGATION_TRANSITION, NavTransition.FADE)
                    Pair(android.R.anim.fade_in, 0)
                } else {
                    Pair(0, android.R.anim.fade_out)
                }
            else -> Pair(0, 0)
        }
    }

    private fun overrideTransition() {
        val anim = getNavAnimations(intent, isStart = false)
        overridePendingTransition(anim.first, anim.second)
    }
}
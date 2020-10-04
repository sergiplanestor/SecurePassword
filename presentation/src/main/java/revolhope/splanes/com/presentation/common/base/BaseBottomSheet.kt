package revolhope.splanes.com.presentation.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheet<T : ViewDataBinding> : BottomSheetDialogFragment() {

    abstract val layoutResource: Int
    open val stateExpanded = true
    lateinit var binding: T
    private var isFirstOnResume: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResource, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        initObserve()
        if (stateExpanded) expand(view)
    }

    override fun onResume() {
        super.onResume()
        if (isFirstOnResume) {
            loadData()
            isFirstOnResume = false
        }
    }

    open fun loadData() {
        // Nothing to do here
    }

    fun show(fm: FragmentManager) = show(fm, javaClass.name)

    private fun expand(view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val bottomSheetRes = com.google.android.material.R.id.design_bottom_sheet
                    (dialog as BottomSheetDialog?)?.findViewById<FrameLayout>(bottomSheetRes)?.let {
                        with(BottomSheetBehavior.from(it)) {
                            state = BottomSheetBehavior.STATE_EXPANDED
                            peekHeight = 0
                        }
                    }
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        )
    }

    open fun initObserve() {
        // Nothing to do here
    }

    abstract fun initViews()
}

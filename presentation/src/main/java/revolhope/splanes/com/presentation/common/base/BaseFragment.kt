package revolhope.splanes.com.presentation.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<T: ViewDataBinding> : Fragment() {

    abstract val layoutResource: Int
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
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
        loadData()
    }

    open fun initViews() {
        // Nothing to do here
    }

    open fun initObservers() {
        // Nothing to do here
    }

    open fun loadData() {
        // Nothing to do here
    }
}
package revolhope.splanes.com.presentation.feature.common.base

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import revolhope.splanes.com.presentation.R

abstract class BaseDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(context, R.style.AppDialogStyle)
        initViews(builder)
        return builder.create()
    }

    fun show(fragmentManager: FragmentManager) = show(fragmentManager, this::class.java.name)

    abstract fun initViews(builder: AlertDialog.Builder)
}
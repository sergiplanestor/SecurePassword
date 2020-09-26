package revolhope.splanes.com.presentation.common.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.common.base.BaseDialog
import revolhope.splanes.com.presentation.common.extensions.Font
import revolhope.splanes.com.presentation.common.extensions.typeface

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

fun Context.showToast(messageInt: Int, duration: Int = Toast.LENGTH_LONG) {
    showToast(getString(messageInt))
}

fun Context.vibrate(time: Long = 350) {
    with(getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this?.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            this?.vibrate(time)
        }
    }
}

fun FragmentActivity.showPickerDialog(model: DialogModel.Picker) =
    PopupAlert(model).show(supportFragmentManager)

fun FragmentActivity.showAcceptCancelDialog(model: DialogModel.Simple) =
    PopupAlert(model).show(supportFragmentManager)

fun FragmentActivity.showOkDialog(
    title: String? = null,
    message: String,
    onOkClick: (() -> Unit)? = null
) =
    PopupAlert(
        model = DialogModel.Simple(
            title = title,
            message = message,
            positiveText = getString(R.string.ok),
            onPositiveClick = onOkClick
        )
    ).show(supportFragmentManager)

fun FragmentActivity.howErrorDialog(message: String, onOkClick: (() -> Unit)? = null) =
    showOkDialog(getString(R.string.error), message, onOkClick)

class PopupAlert(private val model: DialogModel) : BaseDialog() {

    private var selectedItem: String? = null

    override fun initViews(builder: AlertDialog.Builder) {

        builder.setTitle(typeface(Font.MEDIUM, context?.assets, model.title))
        when (model) {
            is DialogModel.Simple -> {
                builder.setMessage(model.message)
                builder.setPositiveButton(model.positiveText) { _, _ ->
                    model.onPositiveClick?.invoke()
                }
                model.negativeText?.let {
                    builder.setNegativeButton(it) { _, _ ->
                        model.onNegativeClick?.invoke()
                    }
                }
                builder.setCancelable(model.isCancelable)
            }
            is DialogModel.Picker -> {

                builder.setSingleChoiceItems(
                    model.items.toTypedArray(),
                    -1
                ) { _, index ->
                    selectedItem = model.items[index]
                }

                builder.setPositiveButton(model.positiveText) { _, _ ->
                    selectedItem?.run { model.onItemSelected.invoke(this) }
                }
                model.negativeText?.let {
                    builder.setNegativeButton(it) { _, _ ->
                        model.onCancel?.invoke()
                    }
                }
                builder.setCancelable(true)
            }
        }

    }
}


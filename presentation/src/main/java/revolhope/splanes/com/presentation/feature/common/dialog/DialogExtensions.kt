package revolhope.splanes.com.presentation.feature.common.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.feature.common.base.BaseDialog

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

fun Context.showToast(messageInt: Int, duration: Int = Toast.LENGTH_LONG) {
    showToast(getString(messageInt))
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

        val typeface = Typeface.createFromAsset(context?.assets, "poppins_medium.ttf")
        builder.setTitle(typeface(typeface, model.title))
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

fun typeface(typeface: Typeface, string: CharSequence?): SpannableString =
    SpannableString(string).apply {
        setSpan(TypefaceSpan(typeface), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }


class TypefaceSpan(private val typeface: Typeface) : MetricAffectingSpan() {
    override fun updateDrawState(tp: TextPaint) {
        tp.typeface = typeface
        tp.flags = tp.flags or Paint.SUBPIXEL_TEXT_FLAG
    }

    override fun updateMeasureState(p: TextPaint) {
        p.typeface = typeface
        p.flags = p.flags or Paint.SUBPIXEL_TEXT_FLAG
    }
}
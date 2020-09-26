package revolhope.splanes.com.presentation.common.extensions

import android.content.res.AssetManager
import android.graphics.Paint
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

enum class Font(val value: String) {
    REGULAR("poppins_regular.ttf"),
    MEDIUM("poppins_medium.ttf"),
    BOLD("poppins_bold.ttf"),
    THIN("poppins_thin.ttf"),
    TITLE("lobstertwo_bold.ttf");
}

fun typeface(
    font: Font,
    assets: AssetManager?,
    string: CharSequence?
): SpannableString =
    SpannableString(string).apply {
        setSpan(
            TypefaceSpan(
                Typeface.createFromAsset(assets, font.value)
            ),
            0,
            length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
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
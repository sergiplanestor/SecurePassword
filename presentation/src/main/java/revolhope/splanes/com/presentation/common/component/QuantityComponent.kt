package revolhope.splanes.com.presentation.common.component

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import revolhope.splanes.com.domain.model.EntryKeyLength
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.databinding.ComponentQuantityBinding

class QuantityComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var binding: ComponentQuantityBinding =
        ComponentQuantityBinding.inflate(LayoutInflater.from(context), this, true)
    private val lengthValues = EntryKeyLength.values()
    private var length: EntryKeyLength = EntryKeyLength.LENGTH_12

    init {
        initViews()
    }

    private fun initViews() {
        binding.edit.setText(length.value.toString())
        setPreviousClick()
        setNextClick()
    }

    private fun setPreviousClick() {
        binding.previousButton.imageTintList =
            ColorStateList.valueOf(context.getColor(R.color.black))
        binding.previousButton.setOnClickListener {
            val i = lengthValues.indexOf(length) - 1
            if (i >= 1) {
                setNextClick()
                length = lengthValues[i]
                binding.edit.setText(lengthValues[i].value.toString())
            }
            if (i == 1) {
                binding.previousButton.setOnClickListener(null)
                binding.previousButton.imageTintList =
                    ColorStateList.valueOf(context.getColor(R.color.colorSecondaryLight))
            }
        }
    }

    private fun setNextClick() {
        binding.nextButton.imageTintList =
            ColorStateList.valueOf(context.getColor(R.color.black))
        binding.nextButton.setOnClickListener {
            val i = lengthValues.indexOf(length) + 1
            if (i <= lengthValues.lastIndex - 1) {
                setPreviousClick()
                length = lengthValues[i]
                binding.edit.setText(lengthValues[i].value.toString())
            }
            if (i == lengthValues.lastIndex - 1) {
                binding.nextButton.setOnClickListener(null)
                binding.nextButton.imageTintList =
                    ColorStateList.valueOf(context.getColor(R.color.colorSecondaryLight))
            }
        }
    }

    fun getSelected(): EntryKeyLength = length

}
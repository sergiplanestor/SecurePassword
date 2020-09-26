package revolhope.splanes.com.presentation.common.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.databinding.ComponentDualSelectorBinding

class DualSelector @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    enum class Option(val value: Int) {
        OPTION_LEFT(0),
        OPTION_RIGHT(1);

        companion object {
            fun parse(value: Int): Option = values().find { it.value == value } ?: OPTION_LEFT
        }
    }

    private var binding: ComponentDualSelectorBinding =
        ComponentDualSelectorBinding.inflate(LayoutInflater.from(context), this, true)
    private var optionSelected: Option = Option.OPTION_LEFT

    init {
        attrs?.let(::applyAttrs)
        initViews()
    }

    private fun applyAttrs(attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.DualSelector)
        setOptionLeft(ta.getString(R.styleable.DualSelector_optionLeft) ?: "")
        setOptionRight(ta.getString(R.styleable.DualSelector_optionRight) ?: "")
        setOptionSelected(
            Option.parse(
                ta.getInt(R.styleable.DualSelector_initialSelected, 0)
            )
        )
        ta.recycle()
    }

    private fun initViews() {
        binding.optionLeft.setOnClickListener {
            setOptionSelected(Option.OPTION_LEFT)
            changeSelected()
        }
        binding.optionRight.setOnClickListener {
            setOptionSelected(Option.OPTION_RIGHT)
            changeSelected()
        }
        changeSelected()
    }

    private fun changeSelected() {
        if (optionSelected == Option.OPTION_LEFT) {
            binding.optionLeft.setTextColor(
                ContextCompat.getColor(context, R.color.white)
            )
            binding.optionRight.setTextColor(
                ContextCompat.getColor(context, R.color.black)
            )
            binding.optionLeft.background =
                ContextCompat.getDrawable(context, R.drawable.shape_dual_selector_activated)
            binding.optionRight.background =
                ContextCompat.getDrawable(context, R.drawable.shape_dual_selector_disabled)
        } else {
            binding.optionLeft.setTextColor(
                ContextCompat.getColor(context, R.color.black)
            )
            binding.optionRight.setTextColor(
                ContextCompat.getColor(context, R.color.white)
            )
            binding.optionLeft.background =
                ContextCompat.getDrawable(context, R.drawable.shape_dual_selector_disabled)
            binding.optionRight.background =
                ContextCompat.getDrawable(context, R.drawable.shape_dual_selector_activated)
        }
    }

    private fun setOptionLeft(option: String) {
        binding.optionLeft.text = option
    }

    private fun setOptionRight(option: String) {
        binding.optionRight.text = option
    }

    private fun setOptionSelected(option: Option) {
        this.optionSelected = option
    }

    fun getOptionSelected(): Option {
        return optionSelected
    }

}
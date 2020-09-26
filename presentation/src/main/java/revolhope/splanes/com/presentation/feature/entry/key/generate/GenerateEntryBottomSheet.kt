package revolhope.splanes.com.presentation.feature.entry.key.generate

import revolhope.splanes.com.domain.model.EntryKeyComplexity
import revolhope.splanes.com.domain.model.EntryKeyLength
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.common.base.BaseBottomSheet
import revolhope.splanes.com.presentation.common.component.DualSelector
import revolhope.splanes.com.presentation.common.extensions.visibility
import revolhope.splanes.com.presentation.databinding.BottomSheetGenerateBinding
import revolhope.splanes.com.presentation.util.generate.GeneratePasswordUtil

class GenerateEntryBottomSheet(
    private val callback: (String, EntryKeyComplexity, EntryKeyLength) -> Unit
) : BaseBottomSheet<BottomSheetGenerateBinding>() {

    override val layoutResource: Int get() = R.layout.bottom_sheet_generate
    override val stateExpanded: Boolean get() = false
    private var isCustomSize: Boolean = false

    override fun initViews() {
        binding.customSizeButton.setOnClickListener {
            isCustomSize = isCustomSize.not()
            binding.customSizeInputLayout.visibility(isCustomSize)
            binding.quantityComponent.visibility(isCustomSize.not(), isGone = false)
            binding.customSizeButton.setText(if (isCustomSize) R.string.predefined_size else R.string.custom_size)
        }
        binding.doneButton.setOnClickListener {
            binding.customSizeInputLayout.error = null
            if (!isCustomSize || checkCustomSizeField()) {
                val size = if (isCustomSize) {
                    binding.customSizeEditText.text?.toString()?.toInt() ?: 12
                } else {
                    binding.quantityComponent.getSelected().value
                }
                callback.invoke(
                    GeneratePasswordUtil.generate(
                        isSimple = binding.dualSelector.getOptionSelected() == DualSelector.Option.OPTION_LEFT,
                        size = size
                    ),
                    obtainComplexity(),
                    if (isCustomSize) EntryKeyLength.LENGTH_CUSTOM else binding.quantityComponent.getSelected()
                )
                dismiss()
            } else {
                binding.customSizeInputLayout.error = context?.getString(R.string.error_blank_field)
            }
        }
    }

    private fun obtainComplexity(): EntryKeyComplexity =
        if (binding.dualSelector.getOptionSelected() == DualSelector.Option.OPTION_LEFT) {
            EntryKeyComplexity.SIMPLE
        } else {
            EntryKeyComplexity.COMPLEX
        }

    private fun checkCustomSizeField() =
        !binding.customSizeEditText.text?.toString().isNullOrBlank()

}
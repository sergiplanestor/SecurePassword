package revolhope.splanes.com.presentation.feature.entry.dir

import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.common.base.BaseBottomSheet
import revolhope.splanes.com.presentation.databinding.BottomSheetNewDirBinding

class EntryDirBottomSheet(
    private val callback: (String) -> Unit
) : BaseBottomSheet<BottomSheetNewDirBinding>() {

    override val layoutResource: Int
        get() = R.layout.bottom_sheet_new_dir

    override fun initViews() {
        binding.createButton.setOnClickListener {
            if (checkFields()) {
                callback.invoke(binding.nameEditText.text.toString())
                this.dismiss()
            }
        }
    }

    private fun checkFields(): Boolean {
        return if (binding.nameEditText.text.toString().isBlank()) {
            binding.nameInputLayout.error = getString(R.string.error_blank_field)
            false
        } else {
            binding.nameInputLayout.error = null
            true
        }
    }
}
package revolhope.splanes.com.presentation.feature.entry.dir

import revolhope.splanes.com.domain.model.EntryDirectoryModel
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.common.base.BaseBottomSheet
import revolhope.splanes.com.presentation.databinding.BottomSheetNewDirBinding

class EntryDirBottomSheet(
    private val model: EntryDirectoryModel? = null,
    private val callback: (String) -> Unit
) : BaseBottomSheet<BottomSheetNewDirBinding>() {

    override val layoutResource: Int
        get() = R.layout.bottom_sheet_new_dir

    override fun initViews() {
        binding.title.text = getString(
            if (model != null) R.string.update_dir_bottom_sheet_title else R.string.new_dir_bottom_sheet_title
        )
        binding.createButton.text = getString(
            if (model != null) R.string.update else R.string.create_directory
        )
        model?.let { binding.nameEditText.setText(it.name) }
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
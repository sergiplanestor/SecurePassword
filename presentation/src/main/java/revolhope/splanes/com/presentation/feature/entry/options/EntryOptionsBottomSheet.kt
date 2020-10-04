package revolhope.splanes.com.presentation.feature.entry.options

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import revolhope.splanes.com.domain.model.EntryDirectoryModel
import revolhope.splanes.com.domain.model.EntryModel
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.common.base.BaseBottomSheet
import revolhope.splanes.com.presentation.common.dialog.DialogModel
import revolhope.splanes.com.presentation.common.dialog.showAcceptCancelDialog
import revolhope.splanes.com.presentation.common.dialog.showToast
import revolhope.splanes.com.presentation.common.extensions.visibility
import revolhope.splanes.com.presentation.databinding.BottomSheetEntryOptionsBinding
import revolhope.splanes.com.presentation.feature.entry.dir.EntryDirBottomSheet
import revolhope.splanes.com.presentation.feature.entry.move.EntryMoveBottomSheet

@AndroidEntryPoint
class EntryOptionsBottomSheet(
    private val entryModel: EntryModel
) : BaseBottomSheet<BottomSheetEntryOptionsBinding>() {

    private val viewModel: EntryOptionsViewModel by viewModels()

    override val layoutResource: Int
        get() = R.layout.bottom_sheet_entry_options

    override fun initViews() {
        binding.title.text = getString(
            R.string.entry_options,
            getString(
                if (entryModel is EntryDirectoryModel) {
                    R.string.directory
                } else {
                    R.string.key
                }
            )
        )
        binding.buttonRename.visibility(entryModel is EntryDirectoryModel)
        binding.buttonRename.setOnClickListener {
            EntryDirBottomSheet(entryModel as EntryDirectoryModel, ::onDirectoryRenamed).show(
                childFragmentManager
            )
        }
        binding.buttonMove.setOnClickListener {
            EntryMoveBottomSheet(::onEntryMoved).show(childFragmentManager)
        }
        binding.buttonDelete.setOnClickListener {
            activity?.showAcceptCancelDialog(
                DialogModel.Simple(
                    title = getString(R.string.dialog_delete_title),
                    message = getString(R.string.dialog_delete_message),
                    positiveText = getString(R.string.dialog_delete_accept),
                    negativeText = getString(R.string.cancel),
                    onPositiveClick = ::onEntryDelete,
                    isCancelable = false
                )
            )
        }
    }

    override fun initObserve() {
        super.initObserve()
    }

    private fun onDirectoryRenamed(name: String) {
        context?.showToast("New name: $name")
    }

    private fun onEntryMoved(dir: EntryDirectoryModel) {
        context?.showToast("Move to -> dir name: ${dir.name}")
    }

    private fun onEntryDelete() {
        context?.showToast("Delete item")
    }
}
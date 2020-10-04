package revolhope.splanes.com.presentation.feature.entry.move

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import revolhope.splanes.com.domain.model.EntryDirectoryModel
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.common.base.BaseBottomSheet
import revolhope.splanes.com.presentation.common.extensions.observe
import revolhope.splanes.com.presentation.databinding.BottomSheetMoveBinding
import revolhope.splanes.com.presentation.feature.dashboard.EntryAdapter
import revolhope.splanes.com.presentation.feature.dashboard.EntryIndicatorAdapter

@AndroidEntryPoint
class EntryMoveBottomSheet(
    private val callback: (EntryDirectoryModel) -> Unit
) : BaseBottomSheet<BottomSheetMoveBinding>() {

    private val viewModel: EntryMoveViewModel by viewModels()

    private var currentDirectory: EntryDirectoryModel = EntryDirectoryModel.Root

    override val layoutResource: Int
        get() = R.layout.bottom_sheet_move

    override fun initViews() {
        binding.buttonMove.setOnClickListener {
            callback.invoke(currentDirectory)
            dismiss()
        }
    }

    override fun initObserve() {
        super.initObserve()
        observe(viewModel.entries) {
            if (it != null) {
                currentDirectory = it.first
                setupEntries(it.second.filterIsInstance<EntryDirectoryModel>())
                viewModel.fetchDirHierarchy(it.first.id)
            }
        }
        observe(viewModel.dirHierarchy, ::setupIndicators)
    }

    private fun setupEntries(directories: List<EntryDirectoryModel>) {
        binding.recyclerContent.layoutManager =
            GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        binding.recyclerContent.adapter =
            EntryAdapter(directories, null, ::onDirClick, null)
    }

    private fun setupIndicators(list: List<EntryDirectoryModel>) {
        with(binding.recyclerIndicators) {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = EntryIndicatorAdapter(list, ::onDirClick)
        }
    }

    private fun onDirClick(dir: EntryDirectoryModel) = viewModel.fetchDirectories(dir.id)

    override fun loadData() {
        super.loadData()
        viewModel.fetchDirectories(currentDirectory.id)
    }
}
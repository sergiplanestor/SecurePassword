package revolhope.splanes.com.presentation.feature.dashboard

import android.content.Intent
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import revolhope.splanes.com.domain.model.EntryDirectoryModel
import revolhope.splanes.com.domain.model.EntryKeyModel
import revolhope.splanes.com.domain.model.EntryModel
import revolhope.splanes.com.domain.util.cryptographic.CryptographyUtils
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.common.base.BaseActivity
import revolhope.splanes.com.presentation.common.dialog.vibrate
import revolhope.splanes.com.presentation.common.extensions.observe
import revolhope.splanes.com.presentation.common.extensions.visibility
import revolhope.splanes.com.presentation.databinding.ActivityDashboardBinding
import revolhope.splanes.com.presentation.feature.entry.options.EntryOptionsBottomSheet
import revolhope.splanes.com.presentation.feature.entry.dir.EntryDirBottomSheet
import revolhope.splanes.com.presentation.feature.entry.key.EntryKeyActivity
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : BaseActivity<ActivityDashboardBinding>() {

    @Inject
    lateinit var cryptographyUtils: CryptographyUtils

    private val viewModel: DashboardViewModel by viewModels()
    private var currentDirectory: EntryDirectoryModel = EntryDirectoryModel.Root

    override val layoutResource: Int
        get() = R.layout.activity_dashboard

    companion object {
        fun start(baseActivity: BaseActivity<*>) {
            baseActivity.startActivity(
                Intent(baseActivity, DashboardActivity::class.java).apply {
                    putExtras(
                        bundleOf(
                            EXTRA_NAVIGATION_TRANSITION to NavTransition.UP
                        )
                    )
                }
            )
        }
    }

    override fun initViews() {
        super.initViews()
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        binding.fabAddKey.setOnClickListener {
            EntryKeyActivity.start(baseActivity = this, parentId = currentDirectory.id)
        }
        binding.fabAddDirectory.setOnClickListener {
            EntryDirBottomSheet(callback = ::onNewDir).show(supportFragmentManager)
        }
    }

    override fun initObservers() {
        super.initObservers()
        observe(viewModel.loaderState) { if (it) showLoader() else hideLoader() }
        observe(viewModel.entries) {
            it?.let {
                currentDirectory = it.first
                setupEntries(it.second)
                viewModel.fetchDirHierarchy(it.first.id)
            } ?: setupEmptyState(true)
        }
        observe(viewModel.dirHierarchy, ::setupIndicator)
        observe(viewModel.insertEntryState) {
            if (it) {
                loadData()
            } else {
                // TODO: error manage
            }
        }
    }

    override fun loadData() {
        super.loadData()
        viewModel.fetchEntries(currentDirectory.id)
    }

    private fun setupEntries(entries: List<EntryModel>) {
        setupActionBar()
        setupEmptyState(entries.isEmpty())
        binding.recyclerContent.layoutManager =
            GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        binding.recyclerContent.adapter =
            EntryAdapter(entries, ::onKeyClick, ::onDirClick, ::onLongClick)
    }

    private fun setupActionBar() {
        supportActionBar?.run {
            if (currentDirectory.id != EntryDirectoryModel.Root.id) {
                title = currentDirectory.name
                setDisplayHomeAsUpEnabled(true)
            } else {
                title = getString(R.string.app_name)
                setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    private fun setupIndicator(hierarchy: List<EntryDirectoryModel>) {
        with(binding.recyclerIndicators) {
            layoutManager =
                LinearLayoutManager(this@DashboardActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = EntryIndicatorAdapter(hierarchy, ::onDirClick)
        }
    }

    private fun setupEmptyState(show: Boolean) = binding.emptyState.visibility(show)

    private fun onNewDir(name: String) {
        viewModel.insertDir(name = name, parentDir = currentDirectory)
    }

    private fun onKeyClick(key: EntryKeyModel) {
        EntryKeyActivity.start(baseActivity = this, parentId = currentDirectory.id, keyModel = key)
    }

    private fun onDirClick(dir: EntryDirectoryModel) = viewModel.fetchEntries(dir.id)

    private fun onLongClick(entry: EntryModel) {
        vibrate(time = 500)
        EntryOptionsBottomSheet(entry).show(supportFragmentManager)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
        if (item?.itemId == android.R.id.home && currentDirectory.id != EntryDirectoryModel.Root.id) {
            currentDirectory.parentId?.let(viewModel::fetchEntries)
            true
        } else {
            super.onOptionsItemSelected(item)
        }

    override fun onBackPressed() {
        currentDirectory.parentId?.let(viewModel::fetchEntries) ?: super.onBackPressed()
    }
}
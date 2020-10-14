package revolhope.splanes.com.presentation.feature.entry.key

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import dagger.hilt.android.AndroidEntryPoint
import revolhope.splanes.com.domain.model.EntryDirectoryModel
import revolhope.splanes.com.domain.model.EntryKeyComplexity
import revolhope.splanes.com.domain.model.EntryKeyLength
import revolhope.splanes.com.domain.model.EntryKeyModel
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.common.base.BaseActivity
import revolhope.splanes.com.presentation.common.dialog.showToast
import revolhope.splanes.com.presentation.common.dialog.vibrate
import revolhope.splanes.com.presentation.common.extensions.observe
import revolhope.splanes.com.presentation.common.extensions.visible
import revolhope.splanes.com.presentation.databinding.ActivityEntryKeyBinding
import revolhope.splanes.com.presentation.feature.entry.key.generate.GenerateEntryBottomSheet
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class EntryKeyActivity : BaseActivity<ActivityEntryKeyBinding>() {

    private val viewModel: EntryKeyViewModel by viewModels()
    private var complexity: EntryKeyComplexity? = null
    private var length: EntryKeyLength? = null

    override val layoutResource: Int
        get() = R.layout.activity_entry_key

    companion object {
        private const val REQ_CODE = 0x123
        private const val EXTRA_MODEL = "EntryKeyActivity#Model"
        private const val EXTRA_DIR = "EntryKeyActivity#Dir"
        fun start(
            baseActivity: BaseActivity<*>,
            keyModel: EntryKeyModel? = null,
            parentId: String,
            onKeyCreated: (Intent?, Int, Int) -> Unit
        ) {
            baseActivity.startActivityForResult(
                Intent(
                    baseActivity,
                    EntryKeyActivity::class.java
                ).apply {
                    putExtras(
                        bundleOf(
                            EXTRA_NAVIGATION_TRANSITION to NavTransition.LATERAL,
                            EXTRA_MODEL to keyModel,
                            EXTRA_DIR to parentId
                        )
                    )
                },
                REQ_CODE,
                onKeyCreated
            )
        }
    }

    override fun initViews() {
        super.initViews()
        supportActionBar?.run {
            title = getString(R.string.add_key)
            setHomeAsUpIndicator(R.drawable.ic_back)
            setDisplayHomeAsUpEnabled(true)
        }
        with(binding.generateButton) {
            setOnClickListener {
                GenerateEntryBottomSheet(::onPasswordGenerated).show(supportFragmentManager)
            }
        }
        binding.copyButton.setOnClickListener {
            (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).run {
                setPrimaryClip(
                    ClipData.newPlainText("key", binding.keyEditText.text.toString())
                )
            }
            showToast(R.string.password_copied)
            vibrate()
        }
        with(binding.createButton) {
            text =
                getString(if (getModel() != null) R.string.update_password else R.string.create_password)
            setOnClickListener {
                viewModel.insertEntry(
                    name = binding.nameEditText.text.toString(),
                    key = binding.keyEditText.text.toString(),
                    keyLength = length,
                    keyComplexity = complexity,
                    extraInfo = binding.extraInfoEditText.text.toString(),
                    parentId = getDirectory(),
                    oldModel = getModel()
                )
            }
        }
        getModel()?.let(::fillFields)
    }

    override fun initObservers() {
        super.initObservers()
        observe(viewModel.loaderState) { if(it) showLoader() else hideLoader() }
        observe(viewModel.entryKeyState) {
            if (it) {
                // TODO: Give some feedback?
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                // TODO: Error management
            }
        }
        observe(viewModel.formKeyState) {
            binding.keyInputLayout.error = if (it) null else getString(R.string.error_blank_field)
        }
        observe(viewModel.formNameState) {
            binding.nameInputLayout.error = if (it) null else getString(R.string.error_blank_field)
        }
    }

    private fun fillFields(model: EntryKeyModel) {
        binding.nameEditText.setText(model.name)
        binding.keyEditText.setText(model.key)
        binding.extraInfoEditText.setText(model.extraInfo)
        val sdf = SimpleDateFormat("dd-MM-yy", Locale.ROOT)
        if (model.dateCreation != -1L) {
            binding.creationDate.text = getString(
                R.string.date_creation,
                sdf.format(model.dateCreation)
            )
            binding.creationDate.visible()
        }
        if (model.dateUpdate != -1L) {
            binding.updatedDate.text = getString(
                R.string.date_updated,
                sdf.format(model.dateUpdate)
            )
            binding.updatedDate.visible()
        }
    }

    private fun onPasswordGenerated(
        pwd: String,
        complexity: EntryKeyComplexity,
        length: EntryKeyLength
    ) {
        this.complexity = complexity
        this.length = length
        binding.keyEditText.setText(pwd)
    }

    private fun getModel(): EntryKeyModel? =
        intent.extras?.getSerializable(EXTRA_MODEL) as? EntryKeyModel

    private fun getDirectory(): String =
        intent.extras?.getString(EXTRA_DIR) ?: EntryDirectoryModel.Root.id
}
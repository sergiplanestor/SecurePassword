package revolhope.splanes.com.presentation.feature.login.authfragment.register.existingaccount

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import revolhope.splanes.com.presentation.R
import revolhope.splanes.com.presentation.common.base.BaseBottomSheet
import revolhope.splanes.com.presentation.common.dialog.showToast
import revolhope.splanes.com.presentation.common.extensions.observe
import revolhope.splanes.com.presentation.databinding.BottomSheetLoginExistingAccountBinding

@AndroidEntryPoint
class AlreadyHaveAccountBottomSheet(
    private val callback: () -> Unit
): BaseBottomSheet<BottomSheetLoginExistingAccountBinding>() {

    private val viewModel: AlreadyHaveAccountViewModel by viewModels()

    override val layoutResource: Int
        get() = R.layout.bottom_sheet_login_existing_account

    override fun initViews() {
        binding.loginButton.setOnClickListener {
            viewModel.validateFields(
                email = binding.emailInputText.text.toString(),
                pwd = binding.pwdInputText.text.toString()
            )
        }
    }

    override fun initObserve() {
        super.initObserve()
        observe(viewModel.loaderState) {
            if (it) binding.appLoader.show() else binding.appLoader.hide()
        }
        observe(viewModel.onCredentialsStored) {
            if (it) {
                callback.invoke()
                dismiss()
            } else {
                context?.showToast(R.string.error_generic)
            }
        }
        observe(viewModel.emailFormState) {
            binding.emailInputLayout.error = if (it.first) null else getString(it.second)
        }
        observe(viewModel.pwdFormState) {
            binding.pwdInputLayout.error =  if (it.first) null else getString(it.second)
        }
        observe(viewModel.isFormValidated) {
            if (it) {
                binding.emailInputLayout.error = null
                binding.pwdInputLayout.error = null
                viewModel.fetchUserRemote(
                    email = binding.emailInputText.text.toString(),
                    pwd = binding.pwdInputText.text.toString()
                )
            }
        }
    }
}
package revolhope.splanes.com.presentation.common.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import revolhope.splanes.com.presentation.common.extensions.gone
import revolhope.splanes.com.presentation.common.extensions.visible
import revolhope.splanes.com.presentation.databinding.ComponentLoaderBinding

class AppLoader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: ComponentLoaderBinding =
        ComponentLoaderBinding.inflate(LayoutInflater.from(context), this, true)

    fun hide() {
        binding.lottie.cancelAnimation()
        this.gone()
    }

    fun show() {
        binding.lottie.playAnimation()
        this.visible()
    }
}
package revolhope.splanes.com.presentation.feature.common.dialog

sealed class DialogModel(
    open val title: String? = null,
    open val message: String? = null,
    open val positiveText: String,
    open val negativeText: String? = null,
) {
    data class Simple(
        override val title: String? = null,
        override val message: String? = null,
        override val positiveText: String,
        override val negativeText: String? = null,
        val onPositiveClick: (() -> Unit)? = null,
        val onNegativeClick: (() -> Unit)? = null,
        val isCancelable: Boolean = false
    ) : DialogModel(title, message, positiveText, negativeText)

    data class Picker(
        override val title: String? = null,
        override val message: String? = null,
        override val positiveText: String,
        override val negativeText: String? = null,
        val items: List<String>,
        val onItemSelected: (String) -> Unit,
        val onCancel: (() -> Unit)? = null
    ) : DialogModel(title, message, positiveText, negativeText)
}


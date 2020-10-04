package revolhope.splanes.com.presentation.feature.dashboard

import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import revolhope.splanes.com.domain.model.EntryDirectoryModel
import revolhope.splanes.com.presentation.R

class EntryIndicatorAdapter(
    private val hierarchy: List<EntryDirectoryModel>,
    private val onDirClick: (EntryDirectoryModel) -> Unit
) : RecyclerView.Adapter<EntryIndicatorAdapter.ViewHolder>() {

    private companion object {
        private const val SEPARATOR = " > "
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.holder_entry_indicator, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(hierarchy[position]) {
            holder.name.text = if (id != EntryDirectoryModel.Root.id) {
                "$name$SEPARATOR"
            } else {
                "${holder.itemView.context.getString(R.string.home)}$SEPARATOR"
            }
            holder.setLink(position != hierarchy.lastIndex, this, onDirClick)
        }
    }

    override fun getItemCount(): Int = hierarchy.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView by lazy { itemView.findViewById(R.id.dirName) }

        fun setLink(
            show: Boolean,
            model: EntryDirectoryModel? = null,
            callback: (EntryDirectoryModel) -> Unit
        ) {
            if (show && model != null) {
                name.setTextColor(itemView.context.getColor(R.color.colorPrimary))
                name.text = SpannableString(name.text).apply {
                    setSpan(
                        UnderlineSpan(),
                        0,
                        length - SEPARATOR.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                itemView.setOnClickListener {
                    callback.invoke(model)
                }
            } else {
                name.setTextColor(itemView.context.getColor(R.color.gray_tertiary))
            }
        }
    }
}
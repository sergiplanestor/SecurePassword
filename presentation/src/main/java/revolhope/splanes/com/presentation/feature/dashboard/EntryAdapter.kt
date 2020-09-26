package revolhope.splanes.com.presentation.feature.dashboard

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import revolhope.splanes.com.domain.model.EntryDirectoryModel
import revolhope.splanes.com.domain.model.EntryKeyModel
import revolhope.splanes.com.domain.model.EntryModel
import revolhope.splanes.com.presentation.R

class EntryAdapter(
    private val entries: List<EntryModel>,
    private val onKeyClick: (EntryKeyModel) -> Unit,
    private val onDirClick: (EntryDirectoryModel) -> Unit,
    private val onLongClick: (EntryModel) -> Unit
) : RecyclerView.Adapter<EntryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.holder_entry_dashboard, parent, false)
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(entries[position]) {
            holder.name.text = name
            holder.itemView.setOnLongClickListener {
                onLongClick.invoke(this)
                true
            }
            when (this) {
                is EntryDirectoryModel -> {
                    holder.icon.setImageResource(R.drawable.ic_directory)
                    holder.icon.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            holder.itemView.context,
                            if (content.isEmpty()) {
                                R.color.colorSecondaryLight
                            } else {
                                R.color.colorSecondaryDark
                            }
                        )
                    )
                    holder.itemView.setOnClickListener { onDirClick.invoke(this) }
                }
                is EntryKeyModel -> {
                    holder.icon.setImageResource(R.drawable.ic_key)
                    holder.icon.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            holder.itemView.context,
                            R.color.colorPrimary
                        )
                    )
                    holder.itemView.setOnClickListener { onKeyClick.invoke(this) }
                }
            }
        }
    }

    override fun getItemCount(): Int = entries.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView by lazy { itemView.findViewById(R.id.icon) }
        val name: TextView by lazy { itemView.findViewById(R.id.name) }
    }
}
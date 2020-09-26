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
    private val directory: EntryDirectoryModel,
    private val onDirClick: (EntryDirectoryModel) -> Unit
) : RecyclerView.Adapter<EntryIndicatorAdapter.ViewHolder>() {

    private val dirList: MutableList<EntryDirectoryModel> = mutableListOf()

    init {
        createListFromDirectory()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.holder_entry_indicator, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(dirList[position]) {
            holder.name.text = if (id != EntryDirectoryModel.Root.id) {
                "$name / "
            } else {
                "${holder.itemView.context.getString(R.string.home)} / "
            }
            // holder.setLink(id != directory.id, this, onDirClick)
        }
        holder.name.text = dirList[position].name
    }

    override fun getItemCount(): Int {
        var items = 1
        var directoryModel: EntryDirectoryModel? = directory
        while (directoryModel?.id != EntryDirectoryModel.Root.id) {
            items++
            directoryModel = directoryModel?.parentDirectoryModel
        }
        return items
    }


    private fun createListFromDirectory() {
        var directoryModel: EntryDirectoryModel = directory
        dirList.add(directory)
        while (directoryModel.parentDirectoryModel != null) {
            directoryModel = directoryModel.parentDirectoryModel!!
            dirList.add(0, directoryModel)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView by lazy { itemView.findViewById(R.id.dirName) }

        fun setLink(
            show: Boolean,
            model: EntryDirectoryModel? = null,
            callback: (EntryDirectoryModel) -> Unit)
        {
            if (show && model != null) {
                name.setTextColor(itemView.context.getColor(R.color.colorPrimary))
                name.text = SpannableString(name.text).apply {
                    setSpan(UnderlineSpan(), 0, length - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }

                // name.paintFlags = name.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                itemView.setOnClickListener {
                    callback.invoke(model)
                }
            } else {
                name.setTextColor(itemView.context.getColor(R.color.gray_tertiary))
                // name.paintFlags = name.paintFlags or (Paint.UNDERLINE_TEXT_FLAG).inv()
            }
        }
    }
}
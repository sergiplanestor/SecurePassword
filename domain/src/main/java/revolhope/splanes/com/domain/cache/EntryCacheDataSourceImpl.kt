package revolhope.splanes.com.domain.cache

import revolhope.splanes.com.domain.model.EntryDirectoryModel
import revolhope.splanes.com.domain.model.EntryModel

object EntryCacheDataSourceImpl {

    private var entries: MutableList<EntryModel>? = null

    fun isCacheEmpty() = entries == null

    fun insertCache(entries: List<EntryModel>) {
        this.entries = entries.toMutableList()
    }

    fun insertEntry(entry: EntryModel) {
        entries?.add(entry)
    }

    fun updateEntry(entry: EntryModel) {
        entries?.indexOfFirst { it.id == entry.id }?.let { index ->
            if (index != -1) {
                entries?.removeAt(index)
                entries?.add(index, entry)
            }
        }
    }

    fun deleteEntry(entry: EntryModel) {
        val toDelete = fetchChildren(entry.id).toMutableList().apply {
            add(entry)
        }
        toDelete.forEach { e ->
            entries?.indexOfFirst { it.id == e.id }?.let { index ->
                if (index != -1) entries?.removeAt(index)
            }
        }
    }

    fun fetch(parentId: String?): Pair<EntryDirectoryModel, List<EntryModel>>? {
        val parent =
            if (parentId != EntryDirectoryModel.Root.id) {
                entries?.find {
                    it is EntryDirectoryModel && it.id == parentId
                } as? EntryDirectoryModel
            } else {
                EntryDirectoryModel.Root
            }
        val children = entries?.filter { it.parentId == parentId }
        return if (parent != null && children != null) {
            parent to children
        } else null
    }

    fun fetchHierarchy(dirId: String): List<EntryDirectoryModel> {
        var dirSeek: String? = dirId
        val hierarchy = mutableListOf<EntryDirectoryModel>()
        while (dirSeek != null && dirSeek != EntryDirectoryModel.Root.id) {
            val dir = entries?.find { it is EntryDirectoryModel && it.id == dirSeek }
            dirSeek = if (dir != null) {
                hierarchy.add(0, dir as EntryDirectoryModel)
                dir.parentId
            } else {
                null
            }
        }
        hierarchy.add(0, EntryDirectoryModel.Root)
        return hierarchy
    }

    fun fetchChildren(dirId: String): List<EntryModel> {
        val children = entries?.filter { it.parentId == dirId }?.toMutableList() ?: mutableListOf()
        if (children.any { it is EntryDirectoryModel }) {
            children.filterIsInstance(EntryDirectoryModel::class.java).forEach {
                children.addAll(fetchChildren(it.id))
            }
        }
        return children
    }
}
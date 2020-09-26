package revolhope.splanes.com.domain.model

import java.io.Serializable

data class EntryDirectoryModel(
    override val id: String,
    override val name: String,
    override var parentDirectoryModel: EntryDirectoryModel?,
    override val isDirectory: Boolean,
    val content: MutableList<EntryModel> = mutableListOf()
) : EntryModel(), Serializable {

    companion object {
        val Root get() = EntryDirectoryModel(
            id = "ROOT",
            name = "/",
            parentDirectoryModel = null,
            isDirectory = true,
            content = mutableListOf()
        )
    }
}
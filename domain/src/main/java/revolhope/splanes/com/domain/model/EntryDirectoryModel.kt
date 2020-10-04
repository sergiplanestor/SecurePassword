package revolhope.splanes.com.domain.model

import java.io.Serializable

data class EntryDirectoryModel(
    override val id: String,
    override val name: String,
    override var parentId: String?,
    override val isDirectory: Boolean
) : EntryModel(), Serializable {

    companion object {
        val Root get() = EntryDirectoryModel(
            id = "ROOT",
            name = "/",
            parentId = null,
            isDirectory = true
        )
    }
}
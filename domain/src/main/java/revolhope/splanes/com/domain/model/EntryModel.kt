package revolhope.splanes.com.domain.model

import java.io.Serializable

abstract class EntryModel : Serializable {
    abstract val id: String
    abstract val name: String
    abstract var parentDirectoryModel: EntryDirectoryModel?
    abstract val isDirectory: Boolean
}
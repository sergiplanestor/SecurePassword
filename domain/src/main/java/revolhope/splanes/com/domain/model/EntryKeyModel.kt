package revolhope.splanes.com.domain.model

import java.io.Serializable

data class EntryKeyModel(
    override val id: String,
    override val name: String,
    val key: String,
    val keyLength: EntryKeyLength,
    val keyComplexity: EntryKeyComplexity,
    val extraInfo: String,
    override var parentDirectoryModel: EntryDirectoryModel?,
    override val isDirectory: Boolean,
    val dateCreation: Long,
    val dateUpdate: Long
): EntryModel(), Serializable
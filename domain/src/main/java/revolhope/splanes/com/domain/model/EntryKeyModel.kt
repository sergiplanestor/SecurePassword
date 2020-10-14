package revolhope.splanes.com.domain.model

import java.io.Serializable

data class EntryKeyModel(
    override val id: String,
    override var name: String,
    val key: String,
    val keyLength: EntryKeyLength,
    val keyComplexity: EntryKeyComplexity,
    val extraInfo: String,
    override var parentId: String?,
    override val isDirectory: Boolean,
    val dateCreation: Long,
    val dateUpdate: Long
): EntryModel(), Serializable
package revolhope.splanes.com.domain.model

enum class EntryKeyLength(val value: Int) {
    LENGTH_UNKNOWN(-1),
    LENGTH_4(4),
    LENGTH_6(6),
    LENGTH_8(8),
    LENGTH_12(12),
    LENGTH_16(16),
    LENGTH_24(24),
    LENGTH_32(32),
    LENGTH_64(64),
    LENGTH_128(128),
    LENGTH_CUSTOM(-2);

    companion object {
        fun parse(value: Int) : EntryKeyLength = values().find { it.value == value } ?: LENGTH_UNKNOWN
    }
}

enum class EntryKeyComplexity(val value: Int) {
    UNKNOWN(-1),
    SIMPLE(0),
    COMPLEX(1);

    companion object {
        fun parse(value: Int) : EntryKeyComplexity = values().find { it.value == value } ?: UNKNOWN
    }
}
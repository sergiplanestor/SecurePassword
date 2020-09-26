package revolhope.splanes.com.presentation.util.generate

import kotlin.random.Random

object GeneratePasswordUtil {

    private const val SIMPLE_UNIVERSE =
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNNOPQRSTUVWXYZ0123456789-_+;:*()=$&¡!@#"
    private const val COMPLEX_UNIVERSE = "$SIMPLE_UNIVERSE,àèìòùáéíóúäëïöüçÀÈÌÒÙÁÉÍÓÚÄËÏÖÜÇ.,{}[]'/\\%<>"

    fun generate(isSimple: Boolean, size: Int): String {
        val universe = obtainUniverse(isSimple).toMutableList()
        return CharArray(size).apply {
            for (i in 0 until size) {
                this[i] = universe.removeAt(randNum(maxInclusive = universe.lastIndex))
            }
        }.concatToString()
    }

    private fun obtainUniverse(isSimple: Boolean): CharArray {
        val universeArray =
            (if (isSimple) SIMPLE_UNIVERSE else COMPLEX_UNIVERSE).toCharArray().toMutableList()
        return CharArray(universeArray.size).apply {
            for (i in 0 until universeArray.size) {
                this[i] = universeArray.removeAt(randNum(maxInclusive = universeArray.lastIndex))
            }
        }
    }

    private fun randNum(minInclusive: Int = 0, maxInclusive: Int): Int {
        return if (minInclusive >= maxInclusive) {
            minInclusive
        } else {
            Random(System.nanoTime()).nextInt(minInclusive, maxInclusive + 1)
        }
    }
}

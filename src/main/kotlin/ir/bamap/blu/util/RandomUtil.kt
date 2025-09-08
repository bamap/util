package ir.bamap.blu.util

class RandomUtil {
    companion object {
        const val BIG_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        const val SMALL_LETTERS = "abcdefghijklmnopqrstuvwxyz"
        const val SIGNS = "~!@#$%^&*()_-+"
        const val NUMBERS = "0123456789"

        @JvmStatic
        fun nextString(length: Int, characters: String = "$BIG_LETTERS$SMALL_LETTERS$NUMBERS"): String {
            return List(length) {
                characters.random()
            }.joinToString("")
        }
    }
}

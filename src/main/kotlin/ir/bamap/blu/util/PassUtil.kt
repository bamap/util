package ir.bamap.blu.util

import ir.bamap.blu.util.RandomUtil.Companion.BIG_LETTERS
import ir.bamap.blu.util.RandomUtil.Companion.NUMBERS
import ir.bamap.blu.util.RandomUtil.Companion.SIGNS
import ir.bamap.blu.util.RandomUtil.Companion.SMALL_LETTERS
import ir.bamap.blu.util.RandomUtil.Companion.nextString
import java.util.*

class PassUtil {
    companion object {

        private const val ALL_CHARS = "$BIG_LETTERS$SMALL_LETTERS$SIGNS$NUMBERS"
        private const val ALL_CHAR_LENGTH = ALL_CHARS.length

        @JvmStatic
        fun generate(length: Int = 8): String {

            val lettersLength = BIG_LETTERS.length * length / ALL_CHAR_LENGTH
            val randomString = listOf(
                nextString(lettersLength, BIG_LETTERS),
                nextString(lettersLength, SMALL_LETTERS),
                nextString(SIGNS.length * length / ALL_CHAR_LENGTH, SIGNS),
                nextString(NUMBERS.length * length / ALL_CHAR_LENGTH, NUMBERS),
            ).joinToString("")

            return mixString(randomString + nextString(length - randomString.length, ALL_CHARS))
        }

        @JvmStatic
        fun generate(oldPassword: String, length: Int = 8, similarityLength: Int = 4): String {

            var newPassword = generate(length)
            var tryCount = 100
            while (!checkPasswordSimilarity(newPassword, oldPassword, similarityLength) && tryCount-- > 0) {
                newPassword = generate(length)
            }

            return newPassword
        }

        @JvmStatic
        fun checkPasswordSimilarity(newPassword: String, oldPassword: String, similarityLength: Int): Boolean {
            var differences = 0
            for(char in newPassword) {
                if(!oldPassword.contains(char))
                    differences ++
            }

            return differences >= similarityLength
        }

        private fun mixString(string: String, count: Int = 10): String {
            if(count <= 0)
                return string

            var mixed = ""
            val chars = string.toMutableList()
            while (chars.isNotEmpty()) {
                mixed += chars.removeAt(Random().nextInt(0, chars.size))
            }

            return mixString(mixed, count - 1)
        }
    }
}

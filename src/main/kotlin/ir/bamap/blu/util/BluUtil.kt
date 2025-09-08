package ir.bamap.blu.util

import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*

class BluUtil {
    companion object {

        /**
         * @param command the command you want to run, For example "java -version"
         * @param changeResponseCallback If you want to break running command return false, otherwise return true
         */
        @JvmStatic
        fun runCommand(
            command: String,
            changeResponseCallback: ((response: String) -> Boolean)? = null
        ): String {
            val process = ProcessBuilder("cmd", "/c", command)
                .redirectErrorStream(true)
                .start()

            val inputStream = process.inputStream
            val builder = StringBuilder()
            val reader = BufferedReader(InputStreamReader(inputStream, Charset.forName(StandardCharsets.UTF_8.name())))
            var charInt = reader.read()

            while (charInt != -1) {
                builder.append(charInt.toChar())
                if (changeResponseCallback != null) {
                    val continueCommand = changeResponseCallback(builder.toString())
                    if(!continueCommand)
                        break
                }
                charInt = reader.read()
            }

            return builder.toString()
        }

        /**
         * Check input string is an english string or not
         *
         * @param str if be null, the method return false, but if the input size be 0, The method return true
         * @return
         */
        @JvmStatic
        fun isEnglish(str: String?): Boolean {
            if (str == null) return false
            if (str.isBlank()) return true

            for (char in str) {
                if (char in 'a'..'z' || char in 'A'..'Z' || char in '0'..'9')
                    continue

                return false
            }

            return true
        }

        /**
         *
         * @param target Contain variables String
         * @param variables replace contain variables in [target] with this entry of hash.
         * @return replaced [target] variables
         * @sample <p>Example: <b>http://itsme.com/{var}</b> => if key[<b>{var}</b>] exist in variables Map,
         * and if it's value be <b>"myvalue"</b>, method return: <b>http://itsme.com/myvalue</b></p>
         */
        @JvmStatic
        fun replaceVariables(target: String, variables: Map<String, String>): String {
            var result = target
            variables.forEach { (key, value) ->
                result = result.replace(key, value)
            }

            return result
        }

        @JvmStatic
        fun toNumberOrNull(value: Any?): Number? {
            if (value == null)
                return null

            if(value is Number)
                return value

            val stringValue = value.toString();
            val int = stringValue.toIntOrNull()
            if (int != null)
                return int

            val long = stringValue.toLongOrNull()
            if (long != null)
                return long

            val float = stringValue.toFloatOrNull()
            if (float != null && float.isFinite())
                return float

            return stringValue.toDoubleOrNull()
        }

        @JvmStatic
        fun forceToNumber(value: Any?): Number {
            return toNumberOrNull(value) ?: throw NumberFormatException((value ?: "null").toString())
        }

        /**
         * @return get Boolean value type if [value] is similar to TRUE, True and etc.
         */
        @JvmStatic
        fun toBooleanOrNull(value: Any?): Boolean? {
            if (value == null)
                return null

            if(value is Boolean)
                return value

            return when (value.toString().lowercase(Locale.getDefault())) {
                "true" -> true
                "false" -> false
                else -> null
            }
        }

        /**
         * @param type Valid values are "or", "|", "and", "&", "xor", "^"
         */
        @JvmStatic
        fun bitwise(collection: Collection<Int>, type: String): Int {
            if(collection.isEmpty()) {
                return 0
            }

            val list = collection.toMutableList()
            var result = list.removeAt(0)

            when(type.lowercase()) {
                "or", "|" -> {
                    list.forEach { result = result or it }
                }
                "and", "&" -> {
                    list.forEach { result = result and it }
                }
                "xor", "^" -> {
                    list.forEach { result = result xor it }
                }
                else -> throw RuntimeException("Valid types are: \"or\", \"|\", \"and\", \"&\", \"xor\", \"^\"")
            }

            return result
        }
    }
}

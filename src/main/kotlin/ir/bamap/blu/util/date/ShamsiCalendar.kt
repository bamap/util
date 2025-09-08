package ir.bamap.blu.util.date

import java.time.DayOfWeek
import java.time.Year
import java.util.*
import kotlin.math.abs
import kotlin.math.floor

class ShamsiCalendar(date: Date = Date()) {
    companion object {
        private const val DEFAULT_PATTERN = "yyyy/MM/dd - HH:mm:ss"
        private val grgSumOfDays = arrayOf(
            intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365),
            intArrayOf(0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335, 366)
        )
        private val hshSumOfDays = arrayOf(
            intArrayOf(0, 31, 62, 93, 124, 155, 186, 216, 246, 276, 306, 336, 365),
            intArrayOf(0, 31, 62, 93, 124, 155, 186, 216, 246, 276, 306, 336, 366)
        )
        private val dayNames = mapOf(
            DayOfWeek.SATURDAY to "شنبه", DayOfWeek.SUNDAY to "یکشنبه",
            DayOfWeek.MONDAY to "دوشنبه", DayOfWeek.TUESDAY to "سه شنبه", DayOfWeek.WEDNESDAY to "چهارشنبه",
            DayOfWeek.THURSDAY to "پنچشنبه", DayOfWeek.FRIDAY to "جمعه"
        )
        private val patternKeys = arrayOf(
            "yyyy", "yy", "MMMMM", "MMM", "MM", "M", "dd", "d", "hh", "HH", "mm", "ss",
            "E", "D", "F", "w", "W", "a", "k", "K"
        )

        @JvmStatic
        fun format(ShamsiCalendar: ShamsiCalendar = ShamsiCalendar(), pattern: String = DEFAULT_PATTERN): String {
            var final = pattern
            patternKeys.forEach {
                if (final.contains(it))
                    final = final.replace(it, getDataByFormat(ShamsiCalendar, it))
            }

            return final
        }

        @JvmStatic
        fun format(date: Date = Date(), pattern: String = DEFAULT_PATTERN): String =
            format(ShamsiCalendar(date), pattern)

        /**
         * Be attention minimum day is [DayOfWeek.SATURDAY] and maximum day is [DayOfWeek.FRIDAY]
         * @return if day1 = day 2 it return 0, If day1 > day2 it return 1 and otherwise return -1
         */
        @JvmStatic
        fun compareDaysOfWeek(day1: DayOfWeek, day2: DayOfWeek): Int {
            if (day1 == day2)
                return 0

            if (day1 == DayOfWeek.SATURDAY)
                return -1
            if (day2 == DayOfWeek.SATURDAY)
                return 1

            if (day1 == DayOfWeek.SUNDAY)
                return -1
            if (day2 == DayOfWeek.SUNDAY)
                return 1

            if (day1 > day2)
                return 1

            return -1
        }

        private fun getDataByFormat(calendar: ShamsiCalendar, subPattern: String): String {
            return when (subPattern) {
                "yyyy" -> calendar.year.toString()
                "yy" -> (calendar.year % 100).toString()
                "MMMMM" -> calendar.getMonthName().localName
                "MMM" -> calendar.getMonthName().shortName
                "MM" -> if (calendar.month < 10) "0${calendar.month}" else "${calendar.month}"
                "M" -> "${calendar.month}"
                "dd" -> if (calendar.day < 10) "0${calendar.day}" else "${calendar.day}"
                "d" -> "${calendar.day}"
                "hh" -> {
                    var h = calendar.hour % 12; return if (h == 0) return "12" else "$h"
                }

                "HH" -> "${calendar.hour}"
                "mm" -> "${calendar.minute}"
                "ss" -> "${calendar.second}"
                "E" -> dayNames.getOrDefault(DateUtil.getDayOfWeek(calendar.date), "")
                else -> "Not Supported"
            }
        }
    }

    var second: Int = 0
        private set

    var minute: Int = 0
        private set

    var hour: Int = 0
        private set

    var day: Int = 0
        private set

    var month: Int = 0
        private set

    var year: Int = 0
        private set

    var date: Date = date
        set(value) {
            field = value
            calculateVariables()
        }

    private val calendar: Calendar = Calendar.getInstance()

    constructor(time: Long) : this(Date(time))

    init {
        calculateVariables()
    }

    fun isLeap(): Boolean = Year.isLeap(year.toLong())

    fun getMonthName(): ShamsiMonth = ShamsiMonth.of(month)

    fun getDayOfWeek(): DayOfWeek = DateUtil.getDayOfWeek(date)

    fun isLeap(year: Int): Boolean {
        val referenceYear = 1375.0
        var startYear = 1375.0
        val yearRes = year - referenceYear
        if (yearRes > 0) {
            if (yearRes >= 33) {
                val numb = yearRes / 33
                startYear = referenceYear + floor(numb) * 33
            }
        } else {
            startYear = if (yearRes >= -33) {
                referenceYear - 33
            } else {
                val numb = abs(yearRes / 33)
                referenceYear - (floor(numb) + 1) * 33
            }
        }
        val leapYears = doubleArrayOf(
            startYear,
            startYear + 4,
            startYear + 8,
            startYear + 16,
            startYear + 20,
            startYear + 24,
            startYear + 28,
            startYear + 33
        )
        return Arrays.binarySearch(leapYears, year.toDouble()) >= 0
    }

    fun format(pattern: String = DEFAULT_PATTERN): String = format(this, pattern)

    private fun calculateVariables() {
        toShamsi()
    }

    private fun toShamsi() {
        val gregorianCalendar = BluGregorianCalendar(date)
        var hshDay = 0
        var hshMonth = 0
        var hshElapsed: Int

        var hshYear = gregorianCalendar.year - 621
        val grgLeap = gregorianCalendar.isLeap()
        var hshLeap = this.isLeap(hshYear - 1)
        val grgElapsed = grgSumOfDays[if (grgLeap) 1 else 0][gregorianCalendar.month - 1] + gregorianCalendar.day
        val christmasToNowruz = if (hshLeap && grgLeap) 80 else 79
        if (grgElapsed <= christmasToNowruz) {
            hshElapsed = grgElapsed + 286
            hshYear--
            if (hshLeap && !grgLeap) hshElapsed++
        } else {
            hshElapsed = grgElapsed - christmasToNowruz
            hshLeap = this.isLeap(hshYear)
        }
        if (year >= 2029 && (year - 2029) % 4 == 0) {
            hshElapsed++
        }
        for (i in 1..12) {
            if (hshSumOfDays[if (hshLeap) 1 else 0][i] >= hshElapsed) {
                hshMonth = i
                hshDay = hshElapsed - hshSumOfDays[if (hshLeap) 1 else 0][i - 1]
                break
            }
        }

        year = hshYear
        month = hshMonth
        day = hshDay
        hour = gregorianCalendar.hour
        minute = gregorianCalendar.minute
        second = gregorianCalendar.second
    }
}

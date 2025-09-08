package ir.bamap.blu.util.date

import java.time.DayOfWeek
import java.time.Year
import java.util.*

/**
 * @author Morteza Malvandi
 */
class DateUtil {
    companion object {
        /**
         * Add amount to date and then check with new Date(). If date be null, The method return true
         */
        @JvmStatic
        fun expired(date: Date?, field: Int, amount: Int): Boolean {
            if (date == null) return true

            val added = add(date, field, amount)
            return added.before(Date())
        }

        @JvmStatic
        fun inSameDay(d1: Date?, d2: Date?): Boolean {
            if (d1 == null || d2 == null) return false

            val cal1 = getCalendar(d1)
            val cal2 = getCalendar(d2)

            return cal1.get(Calendar.DAY_OF_WEEK) == cal2.get(Calendar.DAY_OF_WEEK) && cal1.get(Calendar.YEAR) == cal2.get(
                Calendar.YEAR
            )
        }

        @JvmStatic
        fun nextDay(date: Date): Date {
            val calender = getCalendar(date)
            calender.add(Calendar.DATE, 1)
            return calender.time
        }

        /**
         * Adds or subtracts the specified amount of time to the given calendar field,
         * based on the calendar's rules. For example, to subtract 5 days from
         * the current time of the calendar, you can achieve it by calling:
         * <p> [Calendar.add(Calendar.DAY_OF_MONTH, -5)] </p>.
         * @param field some examples of field: [Calendar.DATE], [Calendar.MINUTE], ...
         */
        @JvmStatic
        fun add(date: Date, field: Int, amount: Int): Date {
            val cal = getCalendar(date)
            cal.add(field, amount)
            return cal.time
        }

        @JvmStatic
        fun getCalendar(date: Date?): Calendar {
            val calendar = Calendar.getInstance()
            calendar.time = date
            return calendar
        }

        @JvmStatic
        fun isLeap(date: Date): Boolean = isLeap(getYear(date))

        @JvmStatic
        fun isLeap(year: Number): Boolean = Year.isLeap(year.toLong())

        @JvmStatic
        fun getYear(date: Date): Int = getCalendar(date).get(Calendar.YEAR)

        @JvmStatic
        fun getMonth(date: Date): Int = getCalendar(date).get(Calendar.MONTH)

        @JvmStatic
        fun getDayOfWeek(date: Date): DayOfWeek {
            val calendar = getCalendar(date)

            /**
             * [Calendar.MONDAY] is 2, but [DayOfWeek.MONDAY] is 1.
             */
            var calendarDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
            if (calendarDayOfWeek == 0)
                calendarDayOfWeek = 7
            return DayOfWeek.of(calendarDayOfWeek)
        }
    }
}

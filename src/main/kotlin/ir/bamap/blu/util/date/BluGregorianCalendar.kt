package ir.bamap.blu.util.date

import java.time.Month
import java.time.Year
import java.util.*

class BluGregorianCalendar(date: Date = Date()) {
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

    constructor(year: Int, month: Int, dayOfMonth: Int, hourOfDay: Int = 0, minute: Int = 0, second: Int = 0) : this() {
        val gregorianCalender = GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute, second)
        this.date = gregorianCalender.time
    }

    init {
        calculateVariables()
    }

    fun isLeap(): Boolean = Year.isLeap(year.toLong())

    fun getMonthName(): Month = Month.of(month)

    private fun calculateVariables() {
        calendar.time = date

        second = calendar.get(Calendar.SECOND)
        minute = calendar.get(Calendar.MINUTE)
        hour = calendar.get(Calendar.HOUR_OF_DAY)
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH) + 1
        year = calendar.get(Calendar.YEAR)
    }
}

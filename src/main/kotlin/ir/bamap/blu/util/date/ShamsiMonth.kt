package ir.bamap.blu.util.date

import java.time.DateTimeException

/**
 * The class is base on [java.time.Month] class
 * @author Morteza Malvandi
 */
enum class ShamsiMonth(var localName: String, var shortName: String) {
    /**
     * The singleton instance for the month of Farvardin with 31 days.
     * This has the numeric value of `1`.
     */
    FARVARDIN("فروردین", "فرو"),

    /**
     * The singleton instance for the month of Ordibehesht with 31 days.
     * This has the numeric value of `2`.
     */
    ORDIBEHESHT("اردیبهشت", "ارد"),

    /**
     * The singleton instance for the month of Khordad with 31 days.
     * This has the numeric value of `3`.
     */
    KHORDAD("خرداد", "خرد"),

    /**
     * The singleton instance for the month of TIR with 31 days.
     * This has the numeric value of `4`.
     */
    TIR("تیر", "تیر"),

    /**
     * The singleton instance for the month of Mordad with 31 days.
     * This has the numeric value of `5`.
     */
    MORDAD("مرداد", "مرد"),

    /**
     * The singleton instance for the month of Shahrivar with 31 days.
     * This has the numeric value of `6`.
     */
    SHAHRIVAR("شهریور", "شهر"),

    /**
     * The singleton instance for the month of Mehr with 30 days.
     * This has the numeric value of `7`.
     */
    MEHR("مهر", "مهر"),

    /**
     * The singleton instance for the month of Aban with 30 days.
     * This has the numeric value of `8`.
     */
    ABAN("آبان", "آبا"),

    /**
     * The singleton instance for the month of Azar with 30 days.
     * This has the numeric value of `9`.
     */
    AZAR("آذر", "آذر"),

    /**
     * The singleton instance for the month of Dey with 30 days.
     * This has the numeric value of `10`.
     */
    DEY("دی", "دی"),

    /**
     * The singleton instance for the month of Bahman with 30 days.
     * This has the numeric value of `11`.
     */
    BAHMAN("بهمن", "بهم"),

    /**
     * The singleton instance for the month of Esfand with 29 days, or 30 in a leap year.
     * This has the numeric value of `12`.
     */
    ESFAND("اسفند", "اسف");

    companion object {

        /**
         * Private cache of all the constants.
         */
        private val ENUMS = ShamsiMonth.values()

        /**
         * Obtains an instance of `Month` from an `int` value.
         *
         *
         * `Month` is an enum representing the 12 months of the year.
         * This factory allows the enum to be obtained from the `int` value.
         * The `int` value follows the ISO-8601 standard, from 1 (Farvardin) to 12 (Esfand).
         *
         * @param month  the month-of-year to represent, from 1 (Farvardin) to 12 (Esfand)
         * @return the month-of-year, not null
         * @throws DateTimeException if the month-of-year is invalid
         */
        @JvmStatic
        fun of(month: Int): ShamsiMonth {
            if (month < 1 || month > 12) {
                throw DateTimeException("Invalid value for MonthOfYear: $month")
            }
            return ENUMS[month - 1]
        }
    }

    /**
     * Gets the month-of-year `int` value.
     *
     *
     * The values are numbered following the ISO-8601 standard,
     * from 1 (Farvardin) to 12 (Esfand).
     *
     * @return the month-of-year, from 1 (Farvardin) to 12 (Esfand)
     */
    fun getValue(): Int {
        return ordinal + 1
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the month-of-year that is the specified number of months after this one.
     *
     *
     * The calculation rolls around the end of the year from Esfand to Farvardin.
     * The specified period may be negative.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param months  the months to add, positive or negative
     * @return the resulting month, not null
     */
    operator fun plus(months: Long): ShamsiMonth {
        val amount = (months % 12).toInt()
        return ENUMS[(ordinal + (amount + 12)) % 12]
    }

    /**
     * Returns the month-of-year that is the specified number of months before this one.
     *
     *
     * The calculation rolls around the start of the year from Farvardin to Esfand.
     * The specified period may be negative.
     *
     *
     * This instance is immutable and unaffected by this method call.
     *
     * @param months  the months to subtract, positive or negative
     * @return the resulting month, not null
     */
    operator fun minus(months: Long): ShamsiMonth {
        return plus(-(months % 12))
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the length of this month in days.
     *
     *
     * This takes a flag to determine whether to return the length for a leap year or not.
     *
     *
     * Farvardin, Ordibehesht, Khordad, Tir, Mordad and Shahrivar have 31 days.
     * Mehr, Aban, Azar, Dey and Bahman have 30 days.
     * Esfand has 29 days in a standard year and 30 days in a leap year.
     *
     * @param leapYear  true if the length is required for a leap year
     * @return the length of this month in days, from 29 to 31
     */
    fun length(leapYear: Boolean): Int {
        return when (this) {
            ESFAND -> if (leapYear) 30 else 29
            MEHR, ABAN, AZAR, DEY, BAHMAN -> 30
            else -> 31
        }
    }

    /**
     * Gets the minimum length of this month in days.
     *
     *
     * Esfand has a minimum length of 29 days.
     * MEHR, ABAN, AZAR, DEY, BAHMAN have 30 days.
     * All other months have 31 days.
     *
     * @return the minimum length of this month in days, from 29 to 31
     */
    fun minLength(): Int {
        return when (this) {
            ESFAND -> 29
            MEHR, ABAN, AZAR, DEY, BAHMAN -> 30
            else -> 31
        }
    }

    /**
     * Gets the maximum length of this month in days.
     *
     *
     * Esfand has a maximum length of 30 days.
     * MEHR, ABAN, AZAR, DEY, BAHMAN have 30 days.
     * All other months have 31 days.
     *
     * @return the maximum length of this month in days, 30 or 31
     */
    fun maxLength(): Int {
        return when (this) {
            ESFAND -> 30
            MEHR, ABAN, AZAR, DEY, BAHMAN -> 30
            else -> 31
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the day-of-year corresponding to the first day of this month.
     *
     *
     * This returns the day-of-year that this month begins on
     *
     * @return the day of year corresponding to the first day of this month, from 1 to 336
     */
    fun firstDayOfYear(): Int {
        return when (this) {
            FARVARDIN -> 1
            ORDIBEHESHT -> 32
            KHORDAD -> 63
            TIR -> 94
            MORDAD -> 125
            SHAHRIVAR -> 155
            MEHR -> 186
            ABAN -> 216
            AZAR -> 246
            DEY -> 276
            BAHMAN -> 306
            ESFAND -> 336
        }
    }

    /**
     * Gets the month corresponding to the first month of this quarter.
     *
     *
     * The year can be divided into four quarters.
     * This method returns the first month of the quarter for the base month.
     * Farvardin, Ordibehesh and Khordad return Farvardin.
     * Tir, Mordad and Shahrivar return Tir.
     * Mehr, Aban and Azar return Mehr.
     * Dey, Bahman and Esfand return Dey.
     *
     * @return the first month of the quarter corresponding to this month, not null
     */
    fun firstMonthOfQuarter(): ShamsiMonth {
        return ENUMS[ordinal / 3 * 3]
    }
}

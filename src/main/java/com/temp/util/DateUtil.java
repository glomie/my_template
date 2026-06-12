package com.temp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static final String PATTERN_DATE       = "yyyy-MM-dd";
    public static final String PATTERN_DATETIME   = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_COMPACT    = "yyyyMMddHHmmss";
    public static final String PATTERN_DATE_CN    = "yyyy年MM月dd日";

    // -------------------------------------------------------------------------
    // Format
    // -------------------------------------------------------------------------

    public static String format(Date date, String pattern) {
        if (date == null) return null;
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String formatDate(Date date) {
        return format(date, PATTERN_DATE);
    }

    public static String formatDateTime(Date date) {
        return format(date, PATTERN_DATETIME);
    }

    // -------------------------------------------------------------------------
    // Parse
    // -------------------------------------------------------------------------

    public static Date parse(String text, String pattern) {
        if (text == null || text.isEmpty()) return null;
        try {
            return new SimpleDateFormat(pattern).parse(text);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Cannot parse date: " + text + " with pattern: " + pattern, e);
        }
    }

    public static Date parseDate(String text) {
        return parse(text, PATTERN_DATE);
    }

    public static Date parseDateTime(String text) {
        return parse(text, PATTERN_DATETIME);
    }

    // -------------------------------------------------------------------------
    // 获取当前时间
    // -------------------------------------------------------------------------

    public static Date now() {
        return new Date();
    }

    public static Date today() {
        return startOfDay(new Date());
    }

    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    // -------------------------------------------------------------------------
    // 日期边界
    // -------------------------------------------------------------------------

    public static Date startOfDay(Date date) {
        Calendar c = toCalendar(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date endOfDay(Date date) {
        Calendar c = toCalendar(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    public static Date startOfMonth(Date date) {
        Calendar c = toCalendar(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date endOfMonth(Date date) {
        Calendar c = toCalendar(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    public static Date startOfYear(Date date) {
        Calendar c = toCalendar(date);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date endOfYear(Date date) {
        Calendar c = toCalendar(date);
        c.set(Calendar.MONTH, 11);
        c.set(Calendar.DAY_OF_MONTH, 31);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    // -------------------------------------------------------------------------
    // 日期运算
    // -------------------------------------------------------------------------

    public static Date addDays(Date date, int days) {
        return add(date, Calendar.DAY_OF_MONTH, days);
    }

    public static Date addMonths(Date date, int months) {
        return add(date, Calendar.MONTH, months);
    }

    public static Date addYears(Date date, int years) {
        return add(date, Calendar.YEAR, years);
    }

    public static Date addHours(Date date, int hours) {
        return add(date, Calendar.HOUR_OF_DAY, hours);
    }

    public static Date addMinutes(Date date, int minutes) {
        return add(date, Calendar.MINUTE, minutes);
    }

    private static Date add(Date date, int field, int amount) {
        Calendar c = toCalendar(date);
        c.add(field, amount);
        return c.getTime();
    }

    // -------------------------------------------------------------------------
    // 日期差值
    // -------------------------------------------------------------------------

    /** 返回两个日期相差的天数（date2 - date1），忽略时分秒 */
    public static long daysBetween(Date date1, Date date2) {
        LocalDate d1 = toLocalDate(date1);
        LocalDate d2 = toLocalDate(date2);
        return ChronoUnit.DAYS.between(d1, d2);
    }

    /** 返回两个日期相差的月数（date2 - date1） */
    public static long monthsBetween(Date date1, Date date2) {
        LocalDate d1 = toLocalDate(date1);
        LocalDate d2 = toLocalDate(date2);
        return ChronoUnit.MONTHS.between(d1, d2);
    }

    /** 返回两个日期相差的秒数（date2 - date1） */
    public static long secondsBetween(Date date1, Date date2) {
        return (date2.getTime() - date1.getTime()) / 1000;
    }

    // -------------------------------------------------------------------------
    // 比较
    // -------------------------------------------------------------------------

    public static boolean isBefore(Date date, Date other) {
        return date.before(other);
    }

    public static boolean isAfter(Date date, Date other) {
        return date.after(other);
    }

    public static boolean isSameDay(Date date1, Date date2) {
        return formatDate(date1).equals(formatDate(date2));
    }

    /** 判断 date 是否在 [start, end] 区间内（含边界） */
    public static boolean isBetween(Date date, Date start, Date end) {
        return !date.before(start) && !date.after(end);
    }

    // -------------------------------------------------------------------------
    // 属性获取
    // -------------------------------------------------------------------------

    public static int getYear(Date date)        { return toCalendar(date).get(Calendar.YEAR); }
    public static int getMonth(Date date)       { return toCalendar(date).get(Calendar.MONTH) + 1; }
    public static int getDayOfMonth(Date date)  { return toCalendar(date).get(Calendar.DAY_OF_MONTH); }
    public static int getDayOfWeek(Date date)   { return toCalendar(date).get(Calendar.DAY_OF_WEEK); }
    public static int getHour(Date date)        { return toCalendar(date).get(Calendar.HOUR_OF_DAY); }
    public static int getMinute(Date date)      { return toCalendar(date).get(Calendar.MINUTE); }
    public static int getSecond(Date date)      { return toCalendar(date).get(Calendar.SECOND); }

    /** 判断是否是周末（周六或周日） */
    public static boolean isWeekend(Date date) {
        int dow = getDayOfWeek(date);
        return dow == Calendar.SATURDAY || dow == Calendar.SUNDAY;
    }

    /** 判断是否是闰年 */
    public static boolean isLeapYear(Date date) {
        return toLocalDate(date).isLeapYear();
    }

    // -------------------------------------------------------------------------
    // 类型转换
    // -------------------------------------------------------------------------

    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Date fromLocalDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date fromLocalDateTime(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date fromTimestamp(long timestamp) {
        return new Date(timestamp);
    }

    // -------------------------------------------------------------------------
    // 私有辅助
    // -------------------------------------------------------------------------

    private static Calendar toCalendar(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    // -------------------------------------------------------------------------
    // 示例
    // -------------------------------------------------------------------------

    public static void main(String[] args) {
        Date date = fromTimestamp(1500961916332L);
        System.out.println("timestamp -> Date : " + formatDateTime(date));
        System.out.println("start of day      : " + formatDateTime(startOfDay(date)));
        System.out.println("end of month      : " + formatDateTime(endOfMonth(date)));
        System.out.println("+7 days           : " + formatDate(addDays(date, 7)));
        System.out.println("days from today   : " + daysBetween(date, now()));
        System.out.println("is weekend        : " + isWeekend(date));
        System.out.println("is leap year      : " + isLeapYear(date));
    }
}

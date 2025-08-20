package com.aegis.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/20 14:28
 * @Description: 时间工具类
 */
public class DateUtils {

    // 获取当前日期
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    // 获取当前时间
    public static LocalTime getCurrentTime() {
        return LocalTime.now();
    }

    // 获取当前日期时间
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    // 格式化日期为字符串
    public static String formatLocalDate(LocalDate date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }

    // 解析字符串为LocalDate
    public static LocalDate parseLocalDate(String dateStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(dateStr, formatter);
    }

    // 增加指定天数
    public static LocalDate addDays(LocalDate date, long days) {
        return date.plusDays(days);
    }

    // 减少指定天数
    public static LocalDate minusDays(LocalDate date, long days) {
        return date.minusDays(days);
    }

    // 计算两个日期之间的天数差
    public static long getDaysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    // 获取指定日期所在月份的第一天
    public static LocalDate getFirstDayOfMonth(LocalDate date) {
        return date.withDayOfMonth(1);
    }

    // 获取指定日期所在月份的最后一天
    public static LocalDate getLastDayOfMonth(LocalDate date) {
        return date.withDayOfMonth(date.lengthOfMonth());
    }

    // 判断两个日期是否相等
    public static boolean isSameDate(LocalDate date1, LocalDate date2) {
        return date1.isEqual(date2);
    }

    // 判断日期是否在指定范围内
    public static boolean isDateInRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return date.isAfter(startDate) && date.isBefore(endDate);
    }

    // 获取指定日期的星期几
    public static DayOfWeek getDayOfWeek(LocalDate date) {
        return date.getDayOfWeek();
    }

    // 判断是否为闰年
    public static boolean isLeapYear(int year) {
        return Year.of(year).isLeap();
    }

    // 获取指定月份的天数
    public static int getDaysInMonth(int year, int month) {
        return YearMonth.of(year, month).lengthOfMonth();
    }

    // 获取指定日期的年份
    public static int getYear(LocalDate date) {
        return date.getYear();
    }

    // 获取指定日期的月份
    public static int getMonth(LocalDate date) {
        return date.getMonthValue();
    }

    // 获取指定日期的天数
    public static int getDayOfMonth(LocalDate date) {
        return date.getDayOfMonth();
    }

    // 获取指定日期的小时数
    public static int getHour(LocalDateTime dateTime) {
        return dateTime.getHour();
    }

    // 获取指定日期的分钟数
    public static int getMinute(LocalDateTime dateTime) {
        return dateTime.getMinute();
    }

    // 获取指定日期的秒数
    public static int getSecond(LocalDateTime dateTime) {
        return dateTime.getSecond();
    }

    // 判断指定日期是否在当前日期之前
    public static boolean isBefore(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }

    // 判断指定日期是否在当前日期之后
    public static boolean isAfter(LocalDate date) {
        return date.isAfter(LocalDate.now());
    }

    // 判断指定日期是否在当前日期之前或相等
    public static boolean isBeforeOrEqual(LocalDate date) {
        return date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now());
    }

    // 判断指定日期是否在当前日期之后或相等
    public static boolean isAfterOrEqual(LocalDate date) {
        return date.isAfter(LocalDate.now()) || date.isEqual(LocalDate.now());
    }

    // 获取指定日期的年龄
    public static int getAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

    // 获取指定日期的季度
    public static int getQuarter(LocalDate date) {
        return (date.getMonthValue() - 1) / 3 + 1;
    }

    // 获取指定日期的下一个工作日
    public static LocalDate getNextWorkingDay(LocalDate date) {
        do {
            date = date.plusDays(1);
        } while (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY);
        return date;
    }

    // 获取指定日期的上一个工作日
    public static LocalDate getPreviousWorkingDay(LocalDate date) {
        do {
            date = date.minusDays(1);
        } while (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY);
        return date;
    }

    // 获取指定日期所在周的第一天（周一）
    public static LocalDate getFirstDayOfWeek(LocalDate date) {
        return date.with(DayOfWeek.MONDAY);
    }

    // 获取指定日期所在周的最后一天（周日）
    public static LocalDate getLastDayOfWeek(LocalDate date) {
        return date.with(DayOfWeek.SUNDAY);
    }

    // 获取指定日期所在年的第一天
    public static LocalDate getFirstDayOfYear(LocalDate date) {
        return date.withDayOfYear(1);
    }

    // 获取指定日期所在年的最后一天
    public static LocalDate getLastDayOfYear(LocalDate date) {
        return date.withDayOfYear(date.lengthOfYear());
    }

    // 获取指定日期所在季度的第一天
    public static LocalDate getFirstDayOfQuarter(LocalDate date) {
        int month = (date.getMonthValue() - 1) / 3 * 3 + 1;
        return LocalDate.of(date.getYear(), month, 1);
    }

    // 获取指定日期所在季度的最后一天
    public static LocalDate getLastDayOfQuarter(LocalDate date) {
        int month = (date.getMonthValue() - 1) / 3 * 3 + 3;
        return LocalDate.of(date.getYear(), month, Month.of(month).maxLength());
    }

    // 判断指定日期是否为工作日（周一至周五）
    public static boolean isWeekday(LocalDate date) {
        return date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY;
    }

    // 判断指定日期是否为周末（周六或周日）
    public static boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    // 获取指定日期所在月份的工作日天数
    public static int getWeekdayCountOfMonth(LocalDate date) {
        int weekdayCount = 0;
        LocalDate firstDayOfMonth = getFirstDayOfMonth(date);
        LocalDate lastDayOfMonth = getLastDayOfMonth(date);

        while (!firstDayOfMonth.isAfter(lastDayOfMonth)) {
            if (isWeekday(firstDayOfMonth)) {
                weekdayCount++;
            }
            firstDayOfMonth = firstDayOfMonth.plusDays(1);
        }

        return weekdayCount;
    }

    // 获取指定日期所在月份的周末天数
    public static int getWeekendCountOfMonth(LocalDate date) {
        int weekendCount = 0;
        LocalDate firstDayOfMonth = getFirstDayOfMonth(date);
        LocalDate lastDayOfMonth = getLastDayOfMonth(date);

        while (!firstDayOfMonth.isAfter(lastDayOfMonth)) {
            if (isWeekend(firstDayOfMonth)) {
                weekendCount++;
            }
            firstDayOfMonth = firstDayOfMonth.plusDays(1);
        }

        return weekendCount;
    }

    // 获取指定日期所在年份的工作日天数
    public static int getWeekdayCountOfYear(LocalDate date) {
        int weekdayCount = 0;
        LocalDate firstDayOfYear = getFirstDayOfYear(date);
        LocalDate lastDayOfYear = getLastDayOfYear(date);

        while (!firstDayOfYear.isAfter(lastDayOfYear)) {
            if (isWeekday(firstDayOfYear)) {
                weekdayCount++;
            }
            firstDayOfYear = firstDayOfYear.plusDays(1);
        }

        return weekdayCount;
    }

}

package com.ball.base.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @author JimChery
 */
public class DateUtil {

    public static final int HOUR = 1000 * 60 * 60;

    public static final long DAY = HOUR * 24;

    public static final int NANO = 1000000;

    private static int zoneOffsetMilliseconds = TimeZone.getDefault().getRawOffset();

    private static ZoneOffset zoneOffset = ZoneOffset.ofHours(zoneOffsetMilliseconds / HOUR);

    private static final DateTimeFormatter DATE_FORMAT_NO_SPLIT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter DATE_FORMAT_HAS_SPLIT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_FORMAT_WEEK = DateTimeFormatter.ofPattern("EEEE dd/MM");


    /**
     * 设置全局时区
     * @param hour -
     */
    public static void setZoneOffset(int hour) {
        zoneOffset = ZoneOffset.ofHours(hour);
        zoneOffsetMilliseconds = hour * HOUR;
    }

    public static long getTime(LocalDateTime localDateTime) {
        return localDateTime.toEpochSecond(zoneOffset) * 1000L + localDateTime.getNano() / NANO;
    }

    public static long getTime(LocalDate localDate) {
        return localDate.toEpochDay() * DAY - zoneOffsetMilliseconds;
    }

    public static LocalDateTime toLocalDateTime(long milliseconds) {
        long second = milliseconds / 1000;
        long na = (milliseconds % 1000) * NANO;
        return LocalDateTime.ofEpochSecond(second, (int)na, zoneOffset);
    }

    public static LocalDate toLocalDate(long milliseconds) {
        return LocalDate.ofEpochDay((milliseconds + zoneOffsetMilliseconds) / DAY);
    }

    public static String formatDate(LocalDate localDate) {
        return localDate.format(DATE_FORMAT_NO_SPLIT);
    }

    public static String formatDate(LocalDateTime localDateTime) {
        return localDateTime.format(DATE_FORMAT_NO_SPLIT);
    }

    public static boolean isYesterday(LocalDate localDate) {
        return localDate != null && localDate.plusDays(1).compareTo(LocalDate.now()) == 0;
    }

    public static String formatWeek(LocalDateTime localDateTime) {
        return localDateTime.format(DATE_FORMAT_WEEK);
    }

    public static void main(String[] argv) {
        System.out.println(toLocalDateTime(1629300273000L));
        System.out.println(getTime(LocalDate.now()));
        System.out.println(toLocalDate(1631116800000L));
        System.out.println(formatWeek(LocalDateTime.now()));
    }
 }

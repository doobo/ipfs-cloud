package com.github.doobo.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 简单时间切片工具
 */
public abstract class DateSplitUtils {

    /**
     * 最大切分重复次数
     */
    public final static int MAX_COUNT = 10000;

    public enum IntervalType {
        DAY,
        HOUR,
        MINUTE,
        SECOND,
        ;
    }

    /**
     * 时间切割
     * @param startTime    被切割的开始时间
     * @param endTime      被切割的结束时间
     * @param interval > 0
     */
    public static List<DateSplit> splitDate(Date startTime, Date endTime, IntervalType intervalType, int interval) {
        if (interval < 0) {
            return null;
        }
        if (endTime.getTime() <= startTime.getTime()) {
            return null;
        }

        if (intervalType == IntervalType.DAY) {
            return splitByDay(startTime, endTime, interval);
        }
        if (intervalType == IntervalType.HOUR) {
            return splitByHour(startTime, endTime, interval);
        }
        if (intervalType == IntervalType.MINUTE) {
            return splitByMinute(startTime, endTime, interval);
        }
        if (intervalType == IntervalType.SECOND) {
            return splitBySecond(startTime, endTime, interval);
        }
        return null;
    }

    /**
     * 按照天切割时间区间
     */
    public static List<DateSplit> splitByDay(Date startTime, Date endTime, int intervalDays) {
        return splitByDay(startTime, endTime, intervalDays, MAX_COUNT);
    }

    /**
     * 按照天切割时间区间,指定最大切分次数,超出后面加一个剩余的
     */
    public static List<DateSplit> splitByDay(Date startTime, Date endTime, int intervalDays, int maxCount) {
        if (startTime == null || endTime == null || endTime.getTime() <= startTime.getTime()) {
            return null;
        }
        maxCount = maxCount < 1? MAX_COUNT: maxCount;
        intervalDays = Math.max(intervalDays, 1);
        List<DateSplit> dateSplits = new ArrayList<>(16);
        DateSplit param = new DateSplit();
        param.setStartDateTime(startTime);
        param.setEndDateTime(endTime);
        param.setEndDateTime(addDays(startTime, intervalDays));
        int i = 0;
        Date tempEndTime = null;
        while (i < maxCount) {
            param.setStartDateTime(startTime);
            tempEndTime = addDays(startTime, intervalDays);
            if (tempEndTime.getTime() >= endTime.getTime()) {
                tempEndTime = endTime;
            }
            param.setEndDateTime(tempEndTime);
            dateSplits.add(new DateSplit(param.getStartDateTime(), param.getEndDateTime()));
            startTime = addDays(startTime, intervalDays);
            if (startTime.getTime() >= endTime.getTime()) {
                break;
            }
            if (param.getEndDateTime().getTime() >= endTime.getTime()) {
                break;
            }
            i++;
        }
        if(tempEndTime.getTime() < endTime.getTime()){
            dateSplits.add(new DateSplit(tempEndTime, endTime));
        }
        return dateSplits;
    }

    /**
     * 按照小时切割时间区间
     */
    public static List<DateSplit> splitByHour(Date startTime, Date endTime, int intervalHours){
        return splitByHour(startTime, endTime, intervalHours, MAX_COUNT);
    }

    /**
     * 按照小时切割时间区间,指定最多切分多少次,超出后面加一个剩余的
     */
    public static List<DateSplit> splitByHour(Date startTime, Date endTime, int intervalHours, int maxCount) {
        if(startTime == null || endTime == null){
            return null;
        }
        if(endTime.getTime() <= startTime.getTime()) {
            return null;
        }
        maxCount = maxCount < 1? MAX_COUNT: maxCount;
        intervalHours = Math.max(intervalHours, 1);
        List<DateSplit> dateSplits = new ArrayList<>(32);
        DateSplit param = new DateSplit();
        param.setStartDateTime(startTime);
        param.setEndDateTime(endTime);
        param.setEndDateTime(addHours(startTime, intervalHours));
        int i = 0;
        Date tempEndTime = null;
        while (i < maxCount) {
            param.setStartDateTime(startTime);
            tempEndTime = addHours(startTime, intervalHours);
            if (tempEndTime.getTime() >= endTime.getTime()) {
                tempEndTime = endTime;
            }
            param.setEndDateTime(tempEndTime);
            dateSplits.add(new DateSplit(param.getStartDateTime(), param.getEndDateTime()));
            startTime = addHours(startTime, intervalHours);
            if (startTime.getTime() >= endTime.getTime()) {
                break;
            }
            if (param.getEndDateTime().getTime() >= endTime.getTime()) {
                break;
            }
            i++;
        }
        if(tempEndTime.getTime() < endTime.getTime()){
            dateSplits.add(new DateSplit(tempEndTime, endTime));
        }
        return dateSplits;
    }

    /**
     * 按照分钟切割时间区间,指定最多切分多少次,超出后面加一个剩余的
     */
    public static List<DateSplit> splitByMinute(Date startTime, Date endTime, int intervalMinutes){
        return splitByMinute(startTime, endTime, intervalMinutes, MAX_COUNT);
    }

    /**
     * 按照分钟切割时间区间
     */
    public static List<DateSplit> splitByMinute(Date startTime, Date endTime, int intervalMinutes, int maxCount) {
        if (endTime.getTime() <= startTime.getTime()) {
            return null;
        }
        maxCount = maxCount < 1? MAX_COUNT: maxCount;
        intervalMinutes = Math.max(intervalMinutes, 1);
        List<DateSplit> dateSplits = new ArrayList<>(64);
        DateSplit param = new DateSplit();
        param.setStartDateTime(startTime);
        param.setEndDateTime(endTime);
        param.setEndDateTime(addMinute(startTime, intervalMinutes));
        int i = 0;
        Date tempEndTime = null;
        while (i < maxCount) {
            param.setStartDateTime(startTime);
            tempEndTime = addMinute(startTime, intervalMinutes);
            if (tempEndTime.getTime() >= endTime.getTime()) {
                tempEndTime = endTime;
            }
            param.setEndDateTime(tempEndTime);
            dateSplits.add(new DateSplit(param.getStartDateTime(), param.getEndDateTime()));
            startTime = addMinute(startTime, intervalMinutes);
            if (startTime.getTime() >= endTime.getTime()) {
                break;
            }
            if (param.getEndDateTime().getTime() >= endTime.getTime()) {
                break;
            }
            i++;
        }
        if(tempEndTime.getTime() < endTime.getTime()){
            dateSplits.add(new DateSplit(tempEndTime, endTime));
        }
        return dateSplits;
    }

    /**
     * 按照秒切割时间区间
     */
    public static List<DateSplit> splitBySecond(Date startTime, Date endTime, int intervalSeconds){
        return splitBySecond(startTime, endTime, intervalSeconds, MAX_COUNT);
    }

    /**
     * 按照秒切割时间区间,指定最大切分次数,超出后面加一个剩余的
     */
    public static List<DateSplit> splitBySecond(Date startTime, Date endTime, int intervalSeconds, int maxCount) {
        if (endTime == null || startTime == null || endTime.getTime() <= startTime.getTime()) {
            return null;
        }
        maxCount = maxCount < 1? MAX_COUNT: maxCount;
        intervalSeconds = Math.max(intervalSeconds, 1);
        List<DateSplit> dateSplits = new ArrayList<>(128);
        DateSplit param = new DateSplit();
        param.setStartDateTime(startTime);
        param.setEndDateTime(endTime);
        param.setEndDateTime(addSeconds(startTime, intervalSeconds));
        int i = 0;
        Date tempEndTime = null;
        while (i < maxCount) {
            param.setStartDateTime(startTime);
            tempEndTime = addSeconds(startTime, intervalSeconds);
            if (tempEndTime.getTime() >= endTime.getTime()) {
                tempEndTime = endTime;
            }
            param.setEndDateTime(tempEndTime);
            dateSplits.add(new DateSplit(param.getStartDateTime(), param.getEndDateTime()));
            startTime = addSeconds(startTime, intervalSeconds);
            if (startTime.getTime() >= endTime.getTime()) {
                break;
            }
            if (param.getEndDateTime().getTime() >= endTime.getTime()) {
                break;
            }
            i++;
        }
        if(tempEndTime.getTime() < endTime.getTime()){
            dateSplits.add(new DateSplit(tempEndTime, endTime));
        }
        return dateSplits;
    }

    private static Date addDays(Date date, int days) {
        return add(date, Calendar.DAY_OF_MONTH, days);
    }

    private static Date addHours(Date date, int hours) {
        return add(date, Calendar.HOUR_OF_DAY, hours);
    }

    private static Date addMinute(Date date, int minute) {
        return add(date, Calendar.MINUTE, minute);
    }

    private static Date addSeconds(Date date, int second) {
        return add(date, Calendar.SECOND, second);
    }

    private static Date add(final Date date, final int calendarField, final int amount) {
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    private static String formatDateTime(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DateSplit {
        private Date startDateTime;
        private Date endDateTime;

        public String getStartDateTimeStr() {
            return formatDateTime(startDateTime);
        }

        public String getEndDateTimeStr() {
            return formatDateTime(endDateTime);
        }
    }

}

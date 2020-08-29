package com.github.doobo.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;

/**
 * 常用时间格式化工具
 */
public class DateUtils {

    private DateUtils() {
    }

    public static final DateTimeFormatter FORMATTER_1 = DateTimeFormatter.ofPattern(DateFormat.y_m_d_hms.getFt());
	/**
	 * 常用日期格式化字段
	 */
	public enum DateFormat {
		y_m_d_hms("yyyy-MM-dd HH:mm:ss"),
		y_m_d("yyyy-MM-dd"),
		ym("yyyyMM"),
		hm("HH:mm"),
		hms("HH:mm:ss"),
		yMdHmsS("yyyyMMddHHmmssSSS"),
		yMdH("yyyyMMddHH"),
		y_m("yyyy-MM"),
		y_m_d_hm("yyyy-MM-dd HH:mm");

		private final String ft;

		DateFormat(String ft) {
			this.ft = ft;
		}

		public String getFt() {
			return ft;
		}
	}

	/**
	 * 字符串转本地时间
	 * @param date
	 * @param format
	 */
	public static LocalDate getLocalDate(String date,String format){
		format = format == null? DateFormat.y_m_d.getFt():format;
		try {
			return LocalDate.parse(date,DateTimeFormatter.ofPattern(format));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 字符串转本地时间
	 * @param date
	 */
	public static LocalDate getLocalDate(String date){
		return getLocalDate(date, null);
	}

	/**
	 * 字符串转时间
	 * @param date
	 * @param format
	 */
	public static Date getDate(String date,String format){
		format = format == null? DateFormat.y_m_d.getFt():format;
		SimpleDateFormat fm = new SimpleDateFormat(format);
		try {
			return fm.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 字符串转时间
	 * @param date
	 */
	public static Date getDate(String date){
		return getDate(date,null);
	}

	/**
	 * 获取当前时间
	 */
	public static Date getCurDate(){
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}

      /**
       * @Description 将带有纳秒的时间字符串转换成LocalDateTime
       * @param str
       */
      public static LocalDateTime timestampStrToLocalDateTime(String str){
        if(str == null){
          return null;
        }
        long millis = Timestamp.valueOf(str).getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        Date date = calendar.getTime();
        SimpleDateFormat sdm = new  SimpleDateFormat(DateFormat.y_m_d_hms.getFt());
        return LocalDateTime.parse(sdm.format(date),FORMATTER_1);
      }

      /**
       * localDateTime时间格式化
       * @param localDateTime
       * @param format
       */
      public static String localDateTimeStr(LocalDateTime localDateTime,String format){
        if(localDateTime == null){
          return null;
        }
        format = format == null ? DateFormat.y_m_d_hms.getFt():format;
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return df.format(localDateTime);
      }

      /**
       * Date类型转LocalDate类型
       * @param date
       */
      public static LocalDate dateToLocalDate(Date date) {
        if(date == null){
          return null;
        }
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDate();
      }

      /**
       * LocalDateTime类型转Date类型
       * @param localDateTime
       * @return Date
       */
      public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        if(localDateTime == null){
          return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return  Date.from(zdt.toInstant());
      }

      /**
       * date转本地时间
       * @param date
       */
      public static LocalDateTime dateToLocalDateTime(Date date) {
        if(date == null){
          return null;
        }
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
      }

      /**
       * 日期格式化
       * @param date
       */
      public static String formatDate(Date date) {
        return formatDateByFormat(date, DateFormat.y_m_d.getFt());
      }

      /**
       * 获取字符串类型的格式
       * @param date
       * @param format
       */
      public static String formatDateByFormat(Date date, String format) {
        String result;
        format = format == null? DateFormat.y_m_d_hms.getFt():format;
        date = date == null?Calendar.getInstance().getTime():date;
        try {
          SimpleDateFormat sdf = new SimpleDateFormat(format);
          result = sdf.format(date);
        } catch (Exception ex) {
          return localDateTimeStr(dateToLocalDateTime(Calendar.getInstance().getTime()),null);
        }
        return result;
      }

      /**
       * 获取最大日期
       * @param params
       */
      public static Date getMaxDate(Date... params){
        Date maxDate = params[0];
        for (int i = 1; i < params.length; i++) {
          if(maxDate.before(params[i])){
            maxDate = params[i];
          }
        }
        return maxDate;
      }

      /**
       * 获取当前日期是星期几<br>
       * @param dt
       * @return 当前日期是星期几
       */
      public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
          w = 0;
        return weekDays[w];
      }

	/**
	 * 字符转时间
	 * @param dateStr
	 * @param pattern
	 */
	public static LocalDate parseLocalDate(String dateStr, String pattern) {
		return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 字符转时间
	 * @param dateTimeStr
	 * @param pattern
	 */
	public static LocalDateTime parseLocalDateTime(String dateTimeStr, String pattern) {
		return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 字符转时间
	 * @param timeStr
	 * @param pattern
	 */
	public static LocalTime parseLocalTime(String timeStr, String pattern) {
		return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 时间转字符
	 * @param date
	 * @param pattern
	 */
	public static String formatLocalDate(LocalDate date, String pattern) {
		return date.format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 时间转字符
	 * @param datetime
	 * @param pattern
	 */
	public static String formatLocalDateTime(LocalDateTime datetime, String pattern) {
		return datetime.format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 时间转字符
	 * @param time
	 * @param pattern
	 */
	public static String formatLocalTime(LocalTime time, String pattern) {
		return time.format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 日期相隔年数
	 * @param startDateInclusive
	 * @param endDateExclusive
	 */
	public static int periodYears(LocalDate startDateInclusive, LocalDate endDateExclusive) {
		return startDateInclusive.until(endDateExclusive).getYears();
	}

	/**
	 * 当前天,间隔多少年,算周岁
	 * @param startDateInclusive
	 */
	public static int periodYears(LocalDate startDateInclusive) {
		return periodYears(startDateInclusive, LocalDate.now());
	}

	/**
	 * 日期相隔天数
	 * @param startDateInclusive
	 * @param endDateExclusive
	 */
	public static long periodDays(LocalDate startDateInclusive, LocalDate endDateExclusive) {
		return endDateExclusive.toEpochDay() - startDateInclusive.toEpochDay();
	}

	/**
	 * 与当前天间隔天数
	 */
	public static long periodDays(LocalDate endDateExclusive) {
		return endDateExclusive.toEpochDay() - LocalDate.now().toEpochDay();
	}

	/**
	 * 日期相隔小时
	 * @param startInclusive
	 * @param endExclusive
	 */
	public static long durationHours(Temporal startInclusive, Temporal endExclusive) {
		return Duration.between(startInclusive, endExclusive).toHours();
	}

	/**
	 * 日期相隔分钟
	 * @param startInclusive
	 * @param endExclusive
	 */
	public static long durationMinutes(Temporal startInclusive, Temporal endExclusive) {
		return Duration.between(startInclusive, endExclusive).toMinutes();
	}

	/**
	 * 日期相隔毫秒数
	 * @param startInclusive
	 * @param endExclusive
	 */
	public static long durationMillis(Temporal startInclusive, Temporal endExclusive) {
		return Duration.between(startInclusive, endExclusive).toMillis();
	}
}



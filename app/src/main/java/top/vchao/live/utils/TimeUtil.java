package top.vchao.live.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ 创建时间: 2017/5/25 on 17:14.
 * @ 描述：时间工具类
 * @ 作者: 郑卫超 QQ: 2318723605
 */

public class TimeUtil {
    public static final SimpleDateFormat DATE_FORMAT_MIN = new SimpleDateFormat("yyyy/MM/dd");
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat Time_FORMAT = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DATE_FORMAT_NUMBER = new SimpleDateFormat("yyyyMMddHHmmss");

    private TimeUtil() {
        throw new AssertionError();
    }

    public static String getPhpTime2Date(String str) {
        return str.substring(0, 10);
    }

    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }


    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }


    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }


    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }


    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    public static String getYear() {
        return getTime(getCurrentTimeInLong(), new SimpleDateFormat("yyyy"));
    }

    public static String getMonth() {
        return getTime(getCurrentTimeInLong(), new SimpleDateFormat("MM"));
    }

    public static String getDate() {
        return getTime(getCurrentTimeInLong(), new SimpleDateFormat("dd"));
    }

    public static String getNowHour() {
        return getTime(getCurrentTimeInLong(), new SimpleDateFormat("hh"));
    }

    /**
     * 根据日期获得星期
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDaysName = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        // String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" };
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }

    public static String getTodayWeek() {
        return getWeek(getTime(getCurrentTimeInLong(), DATE_FORMAT_DATE));
    }

    public static String getNowTime() {
        return getTime(getCurrentTimeInLong(), Time_FORMAT);
    }

    public static String getWeek(String pTime) {
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            Week += "天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            Week += "六";
        }

        return "星期" + Week;
    }

}


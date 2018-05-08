package top.vchao.live.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by liuli on 2015/11/27.
 */
public class DateUtils {


    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */

    public static Date parse(String strDate, String pattern) {

        if (TextUtils.isEmpty(strDate)) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据时间戳获得星期
     *
     * @return
     */
    public static String getWeekOfLongTime(Long timeStamp) {
        String[] weekDaysName = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(timeStamp));
        int mydate = cd.get(Calendar.DAY_OF_WEEK) - 1;

        return weekDaysName[mydate];
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

    /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */

    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    public static int getNowHour() {
        String hh = getTime(getCurrentTimeInLong(), new SimpleDateFormat("HH"));
        int hour = Integer.parseInt(hh);
        return hour;
    }

    public static String getToday() {
        String today = getTime(getCurrentTimeInLong(), new SimpleDateFormat("MM-dd"));
        return today;
    }

    public static String getTomorrow() {
        String today = getTime(getCurrentTimeInLong() + 86400000, new SimpleDateFormat("MM-dd"));
        return today;
    }


    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    public static long getTommoryTimeInLong() {
        return System.currentTimeMillis() + 86400000;
    }

    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    static public String getNowStr(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String now = sdf.format(new Date());
        return now;
    }

    static public Date getFormatDate(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d = new Date();
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            LogUtils.e(e + "");
        }
        return d;
    }

    static public String getDateStr(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String d = sdf.format(date);
        return d;
    }

    static public String getPadZeroString(String s, int size) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < (size - s.length()); i++) {
            sb.append("0");
        }
        sb.append(s);
        return sb.toString();
    }

    /**
     * 得到某月的天数
     *
     * @param year
     * @param month
     * @return
     */
    static public int getDayCountOfMonth(String year, String month) {
        Calendar cal = Calendar.getInstance();
        // 年
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        // 月，因为Calendar里的月是从0开始，所以要-1
        cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    static public String getYesterday(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Calendar now = Calendar.getInstance();
        now.roll(Calendar.DAY_OF_YEAR, -1); //昨天
        return df.format(now.getTime());
    }

    /**
     * 获取和今天附近的某天
     *
     * @param format
     * @param diff
     * @return
     */
    static public String getADay(String format, int diff) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Calendar now = Calendar.getInstance();
        int beforeM = now.get(Calendar.MONTH);
        now.roll(Calendar.DAY_OF_YEAR, diff); //
        int nowM = now.get(Calendar.MONTH);
        //必须进行日期处理，否则2009-01-04日前七天是2009-12-28
        if (nowM > beforeM) {
            now.roll(Calendar.YEAR, -1);
        }
        return df.format(now.getTime());
    }

    static public String getTomorrow(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Calendar now = Calendar.getInstance();
        now.roll(Calendar.DAY_OF_YEAR, 1); //明天
        return df.format(now.getTime());
    }

    /**
     * 得到最近num天的全部日期
     * 说明:
     * 1.日期是从昨天开始算的.
     * 2.如果num=2 , 日期是2008-03-14 ,则返回的结果为 2008-03-12、2008-03-13
     *
     * @param num
     * @return
     */
    public static String[] getDaysByNum(int num, String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] result = {};
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDateFromString(date, "yyyy-MM-dd"));
        //最近一周
        result = new String[num];
        for (int i = num; i > 0; i--) {
            cal.add(Calendar.DAY_OF_YEAR, -1);
            result[i - 1] = sdf.format(new Date(cal.getTimeInMillis()));
        }
        return result;
    }

    public static Date getDateFromString(String dateStr, String format) {
        if ((dateStr == null) || (format == null)) {
            try {
                throw new Exception("数据类型异常" + dateStr + "|" + format);
            } catch (Exception e) {
                LogUtils.e("数据类型异常:" + e);
            }
        }
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date;
        try {
            date = df.parse(dateStr);
            return date;
        } catch (Exception ex) {
            LogUtils.e("" + ex.toString());
            return new Date();
        }
    }

    static public int getNowYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    static public int getNowMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;
    }

    public static String[] getMonthRang(String year, String month) {
        String beginDate = year + "-" + month + "-01";
        String endDate = year + "-" + month + "-" +
                getDayCountOfMonth(year, month);
        return getDaysByRang(beginDate, endDate);
    }

    public static String[] getDaysByRang(String beginDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //得到两个日期间相差多少天
        int num = dateDiff(beginDate, endDate);
        if (num < 0) {
            //颠倒一下日期
            String tmp = beginDate;
            beginDate = endDate;
            endDate = tmp;
            num = 0 - num;
        }
        String[] result = {};
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(beginDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        num = num + 1; //把开始和结束日期都包含进去
        result = new String[num];
        for (int i = 0; i < num; i++) {
            if (i > 0) {
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
            result[i] = sdf.format(new Date(cal.getTimeInMillis()));
        }
        return result;
    }

    public static int dateDiff(String beginDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(endDate);
        } catch (ParseException e) {
            date = new Date();
            e.printStackTrace();
        }
        long end = date.getTime();
        try {
            date = sdf.parse(beginDate);
        } catch (ParseException e) {
            date = new Date();
            e.printStackTrace();
        }
        long begin = date.getTime();
        long day = (end - begin) / (1000 * 3600 * 24); //除1000是把毫秒变成秒
        return Integer.parseInt(Long.toString(day));
    }
}

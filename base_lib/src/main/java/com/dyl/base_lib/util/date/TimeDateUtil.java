package com.dyl.base_lib.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * 时间日期相关工具类
 */
public class TimeDateUtil
{
    public static final String ymdhms = "yyyy-MM-dd HH:mm:ss";
    public static final String ymd = "yyyy-MM-dd";
    public static final String hms = "HH:mm:ss";
    public static final String ss = "ss";
    public static final String mm = "mm";
    public static final String hh = "HH";
    public static final String dd = "dd";


    //日期时间格式转换，包括String、Date、Long。Date是String和Long的桥梁。

    /**
     *Date转String
     * @param data
     * @param formatType
     * @return
     */
    public static String date2String(Date data, String formatType)
    {
        return new SimpleDateFormat(formatType).format(data);
    }


    /**
     * String转Date
     * @param strTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static Date string2Date(String strTime, String formatType) throws ParseException
    {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }





    /**
     * Long转Date
     * @param currentTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static Date long2Date(long currentTime, String formatType) throws ParseException
    {
        Date date = new Date(currentTime);
        return date;
    }


    /**
     * Date转Long
     * @param date
     * @return
     */
    public static long date2Long(Date date)
    {
        return date.getTime();
    }





    /**
     * Long转String
     * @param currentTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static String long2String(long currentTime, String formatType) throws ParseException
    {
        Date date = long2Date(currentTime, formatType);
        String strTime = date2String(date, formatType);
        return strTime;
    }


    /**
     * String转Long
     * @param strTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static long stringToLong(String strTime, String formatType) throws ParseException
    {
        Date date = string2Date(strTime, formatType); // String类型转成date类型
        if (date == null)
        {
            return 0;
        } else
        {
            long currentTime = date2Long(date); // date类型转成long类型
            return currentTime;
        }
    }

    public static long milliTodays(long diff){
        long days = diff / (1000 * 60 * 60 * 24);
        return days;
    }
    public static long milliTohours(long diff){
        long hours = diff/(1000* 60 * 60);
        return hours;
    }
    public static long milliTohours1(long diff){
        long hours = (diff-milliTodays(diff)*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
        return hours;
    }
    public static long milliTominutes(long diff){
        long minutes = (diff-milliTohours(diff)*(1000* 60 * 60))/(1000* 60);
        return minutes;
    }
    public static long milliToseconds(long diff){
        long seconds = (diff-milliTohours(diff)*(1000* 60 * 60)-milliTominutes(diff)*(1000*60))/1000;
        return seconds;
    }
    //毫秒转换成天、小时、分
    public static String longToString(long diff,String str){

        long days = diff / (1000 * 60 * 60 * 24);

        long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);

        long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);

        long seconds = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60)-minutes*(1000*60))/1000;

        return "请在"+days+"天"+hours+"小时"+minutes+"分内"+str+"，超时自动取消";
    }


    //时区相关

    /**
     * 判断用户的设备时区是否为东八区（中国）
     * @return
     */
    public static boolean isInEasternEightZones()
    {
        boolean defaultVaule = true;
        if (TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08"))
        {
            defaultVaule = true;
        }
        else
        {
            defaultVaule = false;
        }
        return defaultVaule;
    }


    /**
     * 根据不同时区，转换时间
     */
    public static Date transformTimeZone(Date date, TimeZone oldZone, TimeZone newZone)
    {
        Date finalDate = null;
        if (date != null)
        {
            int timeOffset = oldZone.getOffset(date.getTime()) - newZone.getOffset(date.getTime());
            finalDate = new Date(date.getTime() - timeOffset);
        }
        return finalDate;
    }


    /**
     * 显示日期为x小时前、昨天、前天、x天前等
     * @param longTime
     * @return
     * @throws ParseException
     */
    public static String showTimeAhead(long longTime) throws ParseException
    {
        String resultTime = "";

        //传入的日期
        Date date = null;
        if (isInEasternEightZones())
        {
            date = long2Date(longTime, ymdhms);
        } else
        {
            date = transformTimeZone(long2Date(longTime, ymdhms), TimeZone.getTimeZone("GMT+08"), TimeZone.getDefault());
        }

        //当前日期
        Calendar cal = Calendar.getInstance();
        Date nowDate = new Date();
        int days = (int) (nowDate.getDay() - date.getDay());

        //如果日期相同
        if (days == 0)
        {
            int hour = nowDate.getHours() - date.getHours();
            //如果小时相同
            if (hour == 0)
            {
                resultTime = (nowDate.getMinutes() - date.getMinutes()) + "分钟前";
            }
            else
            {
                resultTime = hour + "小时前";
            }
        } else if (days == 1)
        {
            resultTime = "昨天";
        } else if (days == 2)
        {
            resultTime = "前天 ";
        } else if (days > 2 && days < 31)
        {
            resultTime = days + "天前";
        } else if (days >= 31 && days <= 2 * 31)
        {
            resultTime = "一个月前";
        } else if (days > 2 * 31 && days <= 3 * 31)
        {
            resultTime = "2个月前";
        } else if (days > 3 * 31 && days <= 4 * 31)
        {
            resultTime = "3个月前";
        } else
        {
            resultTime = long2String(longTime, ymdhms);
        }
        return resultTime;
    }


}
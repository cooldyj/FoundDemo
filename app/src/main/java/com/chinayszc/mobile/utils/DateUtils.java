package com.chinayszc.mobile.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    
    /**
     * MM-dd HH:mm时间格式，示例：04-09 18:23
     */
    public static final String YMDHMS_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	static String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

	public static String getDate(long timestamp) { //获取日期 yyyy-MM_dd
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		String date = format.format(timestamp);
		return date;	
	}
	
	public static String getTime(long timestamp) { //获取时间 HH:MM
		SimpleDateFormat format = new SimpleDateFormat( "MM-dd HH:mm" );
		String time = format.format(timestamp);
		return time;
	}
	
	public static String getTime(long timestamp, String formatString) {
		String time = "";
		try {
			SimpleDateFormat format = new SimpleDateFormat(formatString);
			time = format.format(timestamp);
		}catch (Exception e){
			e.printStackTrace();
		}
        return time;
    }
	
	public static String getDay(long timestamp) { //获取时间 HH:MM
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(timestamp));
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return weekDays[w];
	}  
	
	/**
	 * 获取星期
	 * 0-6对应星期日到星期六
	 * @param timestamp
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static int getDayofWeek(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(timestamp));
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return w;
    }  
	
	public static String getCorrespondingTime(long timestamp) { //获取相对时间，xx分钟前，xx小时前
	    Date now = new Date();
        long l = now.getTime() - timestamp; //获取时间差
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l/ (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        if(hour > 0 ) {
        	return String.format("%d小时前", hour);
        } else {
        	if(min > 0) {
        		return String.format("%d分钟前", min);
        	} else {
        		return String.format("刚刚", min);
        	}
        } 
	}
	
	public static boolean isToday(long timestamp) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		if(sd.format(timestamp).equals(sd.format(new Date()))){
			return true;
		}
		return false;
	}
	
	public static boolean isTheSameDay(long timestamp1, long timestamp2) { // 判断是否是同一天
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		if(sd.format(timestamp1).equals(sd.format(timestamp2))){
			return true;
		}
		return false;
	}
}

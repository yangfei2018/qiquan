package com.cjy.qiquan.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * 时间转换
 * @author terrymeng
 *
 */
public class DateFormater {
	
	final static Logger Out = Logger.getLogger(DateFormater.class);
	public static final String F_FULL = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String start = "2012-12-27";	
	public static final String datetimeFormatNoSplit = "yyyyMMddHHmmss";
	public static final String datetimeFormat = "yyyy-MM-dd HH:mm:ss";
	public static final String dateFormat = "yyyy-MM-dd";
	public static final String timeFormat = "HH:mm:ss";
	public static final String timeFormatNoSecond = "HH:mm";
	public static final String dateFormatNoSplit = "yyyyMMdd";
	public static final String dateFormatNoSplit1 = "yyMMdd";
	public static final String timeFormatHH = "HH";
	public static final String timeFormatHHmm = "HHmm";
	public static final String timeFormatmm = "mm";
	public static final String dateFormatDay = "dd";
	public static final String datetimeFormat2 = "yyyy-MM-dd HH:mm";
	
	public static final  SimpleDateFormat dateFormat_yyyyMMdd=new SimpleDateFormat("yyyyMMdd");
	
	public static final  SimpleDateFormat dateFormat_HHmm=new SimpleDateFormat("HHmm");
	
	public static final  SimpleDateFormat dateFormat_HH=new SimpleDateFormat("HH");
	
	public static final  SimpleDateFormat dateFormat_MMdd=new SimpleDateFormat("MM/dd");
	
	
	public static String standardDateFormat(long time){
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		
		Date date = new Date(time);
		return df.format(date);
	}
	
	/**
	 * 获得日期显示格式
	 * 
	 * @param time
	 * @return
	 */
	public static String standardDateFormat(Date time) {
		StringBuilder pattern = new StringBuilder();
		Calendar thisCalendar = Calendar.getInstance();
		Calendar timeCalendar = Calendar.getInstance();
		timeCalendar.setTime(time);
		if (thisCalendar.get(Calendar.YEAR) == timeCalendar.get(Calendar.YEAR)) {
			// 同一年
			if ((thisCalendar.get(Calendar.MONTH) == timeCalendar
					.get(Calendar.MONTH))
					&& (thisCalendar.get(Calendar.DATE) == timeCalendar
							.get(Calendar.DATE))) {
				// 同一天
				pattern.append("HH:mm");
			} else {
				// 不同天
				pattern.append("MM-dd HH:mm");
			}
		} else {
			pattern.append("yyyy-MM-dd HH:mm");
		}
		return new SimpleDateFormat(pattern.toString()).format(time);
	}

	/**
	 * 标准的时间格式化，是否带毫秒值
	 * 
	 * @param time
	 * @param hasSecond
	 * @return
	 */
	public static String standardDateFormat(Date time, boolean hasSecond) {
		StringBuilder pattern = new StringBuilder();
		Calendar thisCalendar = Calendar.getInstance();
		Calendar timeCalendar = Calendar.getInstance();
		timeCalendar.setTime(time);
		if (thisCalendar.get(Calendar.YEAR) == timeCalendar.get(Calendar.YEAR)) {
			// 同一年
			if ((thisCalendar.get(Calendar.MONTH) == timeCalendar
					.get(Calendar.MONTH))
					&& (thisCalendar.get(Calendar.DATE) == timeCalendar
							.get(Calendar.DATE))) {
				// 同一天
				pattern.append("HH:mm");
			} else {
				// 不同天
				pattern.append("MM-dd HH:mm");
			}
		} else {
			pattern.append("yyyy-MM-dd HH:mm");
		}
		if (hasSecond) {
			pattern.append(":ss");
		}
		return new SimpleDateFormat(pattern.toString()).format(time);
	}

	/**
	 * 获得当前时间和参数时间的时间差值,以xx秒 xx分钟 xx小时 xx天显示
	 * 
	 * @param time
	 * @return
	 */
	public static String viewDateStandardFormat(Date date) {

		long longTime = date.getTime();
		long aa = System.currentTimeMillis() - longTime;
		if (aa <= 1000) {
			return "1秒前";
		}
		long bb = aa / 1000;// 秒
		if (bb < 60) {
			return bb + "秒前";
		}
		long cc = bb / 60;// minitue
		if (cc < 60) {
			return cc + "分钟前";
		}
		long dd = cc / 60;
		if (dd < 24) {
			return dd + "小时前";
		}
		long ee = dd / 24;
		if (ee < 7) {
			return ee + "天前";
		}
		long ff = ee / 7;
		if (ff <= 4) {
			return ff + "周前";
		}
		return standardDateFormat(date);
	}
	
	public static String viewDateStandardFormat(long longTime){
		long aa = System.currentTimeMillis() - longTime;
		if (aa <= 1000) {
			return "1秒前";
		}
		long bb = aa / 1000;// 秒
		if (bb < 60) {
			return bb + "秒前";
		}
		long cc = bb / 60;// minitue
		if (cc < 60) {
			return cc + "分钟前";
		}
		long dd = cc / 60;
		if (dd < 24) {
			return dd + "小时前";
		}
		long ee = dd / 24;
		if (ee < 7) {
			return ee + "天前";
		}
		long ff = ee / 7;
		if (ff <= 4) {
			return ff + "周前";
		}
		return standardDateFormat(longTime);
	}
	
	/**
	 * 获得当前时间和参数时间的时间差值,以xx秒 xx分钟 xx小时 xx天显示
	 * 
	 * @param time
	 * @return
	 */
	public static String viewDateDiff(Date date) {

		long longTime = date.getTime();
		long aa = System.currentTimeMillis() - longTime;
		if (aa <= 1000) {
			return "1秒";
		}
		long bb = aa / 1000;// 秒
		if (bb < 60) {
			return bb + "秒";
		}
		long cc = bb / 60;// minitue
		if (cc < 60) {
			return cc + "分钟";
		}
		long dd = cc / 60;
		if (dd < 24) {
			return dd + "小时";
		}
		long ee = dd / 24;
		if (ee < 7) {
			return ee + "天";
		}
		long ff = ee / 7;
		if (ff <= 4) {
			return ff + "周";
		}
		return standardDateFormat(date);
	}
	
	
	public static int getCurrentTimeStamp(){
		return (int)(System.currentTimeMillis() / 1000);
	}
	
	/**
	 * 获取两个时间的差值，以规定格式显示 秒单位
	 * @param bigTime
	 * @param smallTime
	 * @return
	 */
	public static String getTimeDiffToSecend(long bigTime,long smallTime){
		if(smallTime >= bigTime){
			return "已过期";
		}
		long diff = bigTime - smallTime;
		return getTimeDiff(diff*1000L);
	}
	
	/**
	 * 获取两个时间的差值，以规定格式显示
	 * @param bigTime
	 * @param smallTime
	 * @return
	 */
	public static String getTimeDiff(long bigTime, long smallTime){
		if(smallTime >= bigTime){
			return "";
		}
		long diff = bigTime - smallTime;
		return getTimeDiff(diff);
	}
	
	
	public static int getTimeDiffDay(int bigDay,int smallDay){
		long bigTime = simpleDateParse(""+bigDay,DateFormater.dateFormatNoSplit).getTime();
		long smallTime = simpleDateParse(""+smallDay,DateFormater.dateFormatNoSplit).getTime();
		return getTimeDiffDay(bigTime,smallTime);
	}
	
	public static int getTimeDiffDay(long bigTime,long smallTime){
		if(smallTime >= bigTime){
			return -1;
		}
//		smallTime = smallTime/Constant.DATE.TIME_ONE_DAY*Constant.DATE.TIME_ONE_DAY;
		long diff = bigTime - smallTime;
//		long ms = diff % 1000;
		long d = diff / Constant.DATE.TIME_ONE_DAY;
		if(d >= 0){
			return new Long(d).intValue();
		}
		return -1;
	}
	
	/**
	 * 取得时间显示
	 * @param diff 时间差值
	 * @return
	 */
	public static String getTimeDiff(long diff){
//		System.out.println("getTimeDiff:"+diff);
		String timeRet = "";
		long d = diff / Constant.DATE.TIME_ONE_DAY;
		if(d > 0){
			timeRet += d + "天";
		}
		long h = diff / Constant.DATE.TIME_ONE_HOUR % 24;
		if(h > 0){
			timeRet += h + "小时";
		}
		long m = diff / Constant.DATE.TIME_ONE_MINUTE % 60;
		if(m > 0){
			timeRet += m + "分钟";
		}
		long s = diff / Constant.DATE.TIME_ONE_SECOND % 60;
		if(s > 0){
			timeRet += s + "秒";
		}
		////System.out.println(d+"天"+h+"小时"+m+"分钟"+s+"秒"+ms + "ms");
		return timeRet;
	}
	/**
	 * 
	 * @param diff
	 * @return
	 */
	public static String getTimeDiffFormat(long diff){
		String timeRet = "";
		long d = diff / Constant.DATE.TIME_ONE_DAY;
		if(d > 0){
			timeRet += d + "天";
		}
		long h = diff / Constant.DATE.TIME_ONE_HOUR % 24;
		if(h > 0){
			timeRet += h + ":";
		}
		long m = diff / Constant.DATE.TIME_ONE_MINUTE % 60;
		if(m > 0){
			timeRet += m + ":";
		}
		long s = diff / Constant.DATE.TIME_ONE_SECOND % 60;
		if(s > 0){
			timeRet += s ;
		}
		return timeRet;
	}
	
	/**
	 * 时间加减
	 * 
	 * @param date
	 * @param num
	 * @return
	 */
	public static Date dateAdd(Date date, int num) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, num);
			date = cal.getTime();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 时间加减，输出format格式字符串
	 * 
	 * @param date
	 * @param format
	 * @param num
	 * @return
	 */
	public static String dateAddByDateForString(Date date, String format,
			int num) {
		String ret = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, num);
			date = cal.getTime();
			ret = sdf.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	public static int calDiffDays(String startDate,String endDate){
		long beforeTime;
		long newTime;
		try {
			beforeTime = dateFormat_yyyyMMdd.parse(startDate).getTime();
			newTime = dateFormat_yyyyMMdd.parse(endDate).getTime();
		} catch (ParseException e) {
			return 0;
		}
		
		
		return Long.valueOf(Math.abs(newTime-beforeTime)/Constant.DATE.TIME_ONE_DAY).intValue();
		
	}
	/**
	 * 
	 * @param date
	 * @param format
	 * @param num
	 * @return
	 */
	public static String dateAddByStringForString(String date, String format,
			int num) {
		String ret = null;
		SimpleDateFormat sdf;
		Date dateTime;
		try {
			sdf = new SimpleDateFormat(format);
			dateTime = sdf.parse(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateTime);
			cal.add(Calendar.DATE, num);
			dateTime = cal.getTime();
			ret = sdf.format(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 
	 * @param dateString
	 * @param format
	 * @return
	 */
	public static Date simpleDateParse(String dateString, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String simpleDateFormat(Date date, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 根据时间段，返回给定格式的日期list<String>
	 * 
	 * @param startDate
	 * @param endDate
	 * @param format
	 * @return
	 */
	public static List<String> getDateList(Date startDate, Date endDate,
			String format) {
		try {
			List<String> list = new ArrayList<String>();
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			Calendar calend = Calendar.getInstance();
			calend.setTime(endDate);
			while (cal.before(calend)) {
				list.add(simpleDateFormat(cal.getTime(), format));
				cal.add(Calendar.DATE, 1);
			}
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取当前时间
	 * @param format 显示的格式
	 * @return
	 */
	public static String getNowTimeString(String format){
		Date now = new Date();
		return simpleDateFormat(now, format);
	}
	

	/**
	 * 获取当前时间
	 * @param format 显示的格式
	 * @return
	 */
	public static String getNowDay(String format){
		Date now = new Date();
		return simpleDateFormat(now, format);
	}
	
	/**
	 * 获取前几天日期-----yymmdd
	 * @return
	 */
	public  static  String  getBeforeday(int  num)
	{
		  Calendar   cal   =   Calendar.getInstance();
		  cal.add(Calendar.DATE,   -num);
		  
		  String yesterday = new SimpleDateFormat(dateFormatNoSplit).format(cal.getTime());
		  return  yesterday;
	}
	/**
	 * 获取前几天日期---yy-mm-dd
	 * @return
	 */
	public  static  String  getBeforeday2(int  num)
	{
		  Calendar   cal   =   Calendar.getInstance();
		  cal.add(Calendar.DATE,   -num);
		  String yesterday = new SimpleDateFormat(dateFormat).format(cal.getTime());
		  return  yesterday;
	}
	/**
	 * 获取后几天日期-----yymmdd
	 * @return
	 */
	public  static  String  getLastday(int  num)
	{
		  Calendar   cal   =   Calendar.getInstance();
		  cal.add(Calendar.DATE,   num);
		  String yesterday = new SimpleDateFormat(dateFormatNoSplit).format(cal.getTime());
		  return  yesterday;
	}
	/**
	 * 获得今日整形：20120101
	 * @return
	 */
	public static int getTodayInt(){
		return Integer.parseInt(DateFormater.getNowTimeString(DateFormater.dateFormatNoSplit));
	}
	
	
	public static int getYesInt(){
		return Integer.parseInt(DateFormater.getLastday(-1));
	}
	/**
	 * 获取指定日期整形
	 * @param datetime
	 * @return
	 */
	public static int getDayInt(long datetime){
		Date date = new Date(datetime);
		return Integer.parseInt(simpleDateFormat(date, DateFormater.dateFormatNoSplit));
	}
	
	
	
	public static String getDayFormat(long dateime){
		Date date = new Date(dateime*1000L);
		return DateFormater.standardDateFormat(date);
	}
	
   /* public static int  datelistCount(String  endTime)
    {	
  	  try {		
//  		  String start = "2012-11-22";			
//  		  String end = DateFormater.getNowTimeString(DateFormater.dateFormat);			
  		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");			
  		  Date startDate = sdf.parse(start);	
  		  ////System.out.println(startDate);
  		  Date endDate = sdf.parse(endTime);
  		  //结束日期前50天
  		  Calendar endCalendar = Calendar.getInstance();	
  		  endCalendar.setTime(endDate);
  		  endCalendar.add(Calendar.DATE, -50);
  		  //如果开始日期相差超过50天，只显示最近50天内的数据
  		  if(startDate.before(endCalendar.getTime())){
  			startDate = endCalendar.getTime();
  		  }	
  		  return getTimeDiffDay(endDate.getTime(),startDate.getTime());
  	   } 
  	   catch (ParseException e) 
  	   {			
  		   e.printStackTrace();		
  	   }
  	   return  0;
  }*/
    
    public static List<String>  datelist(String  endTime,int pageNow,int numsPerPage)
    {	
    	if(pageNow<1){
    		pageNow = 1;
    	}
  	  try {		
  		  List<String>  list = new ArrayList<String>();
//  		  String start = "2012-11-22";			
//  		  String end = DateFormater.getNowTimeString(DateFormater.dateFormat);			
  		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");			
  		  Date startDate = sdf.parse(start);	
  		  ////System.out.println(startDate);
  		  Date endDate = sdf.parse(endTime);
  		  //结束日期前50天
  		  Calendar endCalendar = Calendar.getInstance();	
  		  endCalendar.setTime(endDate);
  		  endCalendar.add(Calendar.DATE, -50);
  		  //如果开始日期相差超过50天，只显示最近50天内的数据
  		  if(startDate.before(endCalendar.getTime())){
  			startDate = endCalendar.getTime();
  		  }
  		////System.out.println(endDate);
  		  Calendar startCalendar = Calendar.getInstance();	
  		  int index = 0;
  		  int indexStart = (pageNow-1)*numsPerPage+index+1;
  		
  		  while (startDate.before(endDate)) {
  			  index++;
  			  String  dat = sdf.format(startDate);
  			  startCalendar.setTime(startDate);				
  			  startCalendar.add(Calendar.DATE, 1);				
  			  startDate = startCalendar.getTime();			
  			  if(index < indexStart){
  				  continue;
  			  }else if(index > indexStart+numsPerPage-1){
				  break;
			  }else{
	  			  list.add(dat);
			  }
  			  }	
  		   return  list;
  	   } 
  	   catch (ParseException e) 
  	   {			
  		   e.printStackTrace();		
  	   }
  	   return  null;
  }
    
    /**
     * 判断2个日期字符串大小
     * @param date1
     * @param date2
     * @return
     */
    public static int compare_date(String date1, String date2) 
	{           
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//    	System.out.println("compare_date:"+date1+":"+date2);
//        DateFormat df = new SimpleDateFormat("hh:mm:ss");
        try {
               java.util.Date d1 = sdf.parse(date1);
               java.util.Date d2 = sdf.parse(date2);
               if (d1.getTime() > d2.getTime())
                  {
                       
                        return 1;
                  }
               else if (d1.getTime() < d2.getTime()) 
                 {
                      
                        return -1;
                 } 
               else 
               {
                        return 0;
                }
        } catch (Exception exception) {
                exception.printStackTrace();
        }
        return 0;

	  }

	
	public static int getDay() {
	  Calendar cal = Calendar.getInstance();
	  return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	public static boolean isInTime(int startTime,int endTime){
		Integer now=Integer.parseInt(DateFormater.getNowTimeString(DateFormater.timeFormatHHmm));
		
		if (now>=startTime && now<= endTime){
			return true;
		}
		
		return false;	
	}
	
    /**
     * 是否同一天
     * @param time1
     * @param time2
     * @return
     */
	public static boolean isSamedata(long time1, long time2) {
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(time1);
		Calendar other = Calendar.getInstance();
		other.setTimeInMillis(time2);
		return time.get(Calendar.YEAR) == other.get(Calendar.YEAR) 
				&& time.get(Calendar.MONTH) == other.get(Calendar.MONTH)
				&& time.get(Calendar.DAY_OF_MONTH) == other.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 比较时间是否hour点的同一天
	 * @param time1
	 * @param time2
	 * @param hour
	 * @return
	 */
	public static boolean isSamedata(long time1, long time2, int hour) {
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(time1);
		time.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY) - hour);
		Calendar other = Calendar.getInstance();
		other.setTimeInMillis(time2);
		other.set(Calendar.HOUR_OF_DAY, other.get(Calendar.HOUR_OF_DAY) - hour);
		return time.get(Calendar.YEAR) == other.get(Calendar.YEAR) 
				&& time.get(Calendar.MONTH) == other.get(Calendar.MONTH)
				&& time.get(Calendar.DAY_OF_MONTH) == other.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 是否同一天
	 * @param date1
	 * @param date2
	 * @param hour
	 * @return
	 */
	public static boolean isSamedata(Date date1, Date date2, int hour) {
		Calendar time = Calendar.getInstance();
		time.setTime(date1);
		time.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY) - hour);
		Calendar other = Calendar.getInstance();
		other.setTime(date2);
		other.set(Calendar.HOUR_OF_DAY, other.get(Calendar.HOUR_OF_DAY) - hour);
		return time.get(Calendar.YEAR) == other.get(Calendar.YEAR) 
				&& time.get(Calendar.MONTH) == other.get(Calendar.MONTH)
				&& time.get(Calendar.DAY_OF_MONTH) == other.get(Calendar.DAY_OF_MONTH);
	}
	
	public static int currentDayOfWeek(int hour){
		Calendar time = Calendar.getInstance();
		time.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY) - hour);
		return time.get(Calendar.DAY_OF_WEEK) -1;
	}
	
	public static int diffDay(long time1,long time2,int hour){
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(time1 - (hour * Constant.DATE.TIME_ONE_HOUR));
		time.set(Calendar.HOUR_OF_DAY, 0);
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.SECOND, 0);
		time.set(Calendar.MILLISECOND, 0);
		Calendar other = Calendar.getInstance();
		other.setTimeInMillis(time2 - (hour * Constant.DATE.TIME_ONE_HOUR));
		other.set(Calendar.HOUR_OF_DAY, 0);
		other.set(Calendar.MINUTE, 0);
		other.set(Calendar.SECOND, 0);
		other.set(Calendar.MILLISECOND, 0);
		return getTimeDiffDay(time.getTimeInMillis(),other.getTimeInMillis());
	}
	
	
	public static int getWeekOfToday(){
		Calendar calendar= Calendar.getInstance();
		int dated = calendar.get(Calendar.DAY_OF_WEEK)-1;
		return dated;
	}
	
	
	public static int getWeekOfTodayBefore(int hours){
		Calendar calendar= Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - hours);
		int dated = calendar.get(Calendar.DAY_OF_WEEK)-1;
		return dated;
	}
	
	
	public static int getWeekOfTodayBefore(long time,int hours){
		Calendar calendar= Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - hours);
		int dated = calendar.get(Calendar.DAY_OF_WEEK)-1;
		return dated;
	}
	
//	public static void main(String[] argv) {
//		String  sdf = "20121107";
//		int q = 5;
//		////System.out.println(20121107-2012);
//		
//		////System.out.println(8%7);
////		////System.out.println(new Date(System.currentTimeMillis()));
////		////System.out.println(DateFormater.getNowDay(DateFormater.datetimeFormat));
//		
//        int nowday = 20121117;
//        int yesday = 20121113;
//        int count = 2 ;
//        int aa = nowday - yesday;
//		System.out.println(DateFormater.getLastday(1));
//	}
	
	
	public static final Date format(String timeUTF) {
		if (null == timeUTF || "".equals(timeUTF.trim())) {
			return new Date();
		}
		StringBuilder builder = new StringBuilder();
		timeUTF = timeUTF.replaceAll("/|,|\\.|_", "-").trim();
		
		String time = null;
		int index1 = timeUTF.indexOf('-');
		int index2 = timeUTF.indexOf(':');
		boolean haveDate = false;
		if (index1 < 0 || (index1 > index2 && index2 > 0)) {
			builder.append(getDate());
			time = timeUTF;
		} else {
			String[] dateTmps = new String[]{"00", "00", "00"};
			String dateUTF = null;
			if (timeUTF.indexOf(" ") != -1) {
				dateUTF = timeUTF.substring(0, timeUTF.indexOf(" "));
				time = timeUTF.substring(timeUTF.indexOf(" ") + 1);
			} else {
				dateUTF = timeUTF;
				time = "";
			}
			String[] dates = dateUTF.split("-");
			String date = null;
			for (int i = dates.length - 1, j = 2; i >= 0; i--, j--) {
				date = dates[i].trim();
				dateTmps[j] = date.length() > 1 ? date : 0 + date;
			}
			if(dates.length < 3){
				Calendar calender = Calendar.getInstance();
				if(dates.length < 2){
					dateTmps[1] = String.valueOf(calender.get(Calendar.MONTH));
				}
				dateTmps[0] = String.valueOf(calender.get(Calendar.YEAR));
			}
			builder.append(dateTmps[0]).append('-')
				.append(dateTmps[1]).append('-').append(dateTmps[2]);
			haveDate = true;
		}

		String[] timeTmps = new String[]{"00", "00", "00", "000"};
		String[] ms = time.split("-");
		if (ms.length > 1) {
			String millS = ms[1].trim();
			if (millS.length() > 3) {
				millS = millS.substring(0, 3);
			} else {
				for (; millS.length() < 3;) {
					millS = 0 + millS;
				}
			}
			timeTmps[3] = millS;
		}
		String[] times = ms[0].split(":");
		String tmp = null;
		if(haveDate) {
			for (int i = 0; i < times.length; i++) {
				tmp = times[i].trim();
				timeTmps[i] = tmp.length() > 1 ? tmp : 0 + tmp;
			}
		} else {
			for (int i = times.length - 1, j = 2; i >= 0; i--, j--) {
				tmp = times[i].trim();
				timeTmps[j] = tmp.length() > 1 ? tmp : 0 + tmp;
			}
		}
		
		builder.append(' ').append(timeTmps[0]).append(':')
				.append(timeTmps[1]).append(':')
				.append(timeTmps[2]).append('.').append(timeTmps[3]);
		
		return format(builder.toString(), F_FULL);
	}
	
	public static final String F_yyyyMMdd = "yyyy-MM-dd";
	
	public static final String getDate() {
		return getTime(F_yyyyMMdd);
	}
	
	public static final String getTime(String style) {
		return format(new Date(), style);
	}
	
	public static final String format(Date date, String style) {
		return new SimpleDateFormat(style).format(date);
	}
	public static final Date format(String timeUTF, String style) {
		Date date = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat(style);
			Date xdate = format.parse(timeUTF);
			date = new Date(xdate.getTime());
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return date;
	}
	
	public static void main(String args[]){
		
		System.out.println(DateFormater.getDayInt(System.currentTimeMillis()));
	}

 
}

package tw.gov.twc.type;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 民國日期物件
 * 
 * @author Eric
 */
public final class TwDate implements Serializable {
	
	private static final long serialVersionUID = 5279806492726142350L;
	
	private static final String YYYY = "yyyy";
	private static final String YYYYMM = "yyyyMM";
	private static final String YYYYMMDD = "yyyyMMdd";
	private static final String YYYYMMDDHH = "yyyyMMddHH";
	private static final String YYYYMMDDHHMM = "yyyyMMddHHmm";
	private static final String TIME_STYLE= "yyyyMMddHHmmss";
	private static final String TW_Y_STYLE = "TTT";
	private static final String TW_YM_STYLE = "TTT/MM";
	private static final String TW_YMD_STYLE = "TTT/MM/dd";
	private static final String TW_TIME_STYLE= "TTT/MM/dd HH:mm:ss";
	
	private Calendar cal;
	private String original;
	private String pattern;
	
	/**
	 * 以系統時間建構民國日期物件
	 */
	public TwDate () {
		this.cal = Calendar.getInstance();
		this.pattern = YYYYMMDD;
	}
	
	/**
	 * 以西元日期文字建構民國日期物件，只能為年(4)、年月(6)、年月日(8)
	 * 
	 * @param date 西元日期文字
	 * @throws ParseException
	 */
	public TwDate (String date) throws ParseException {
		this(date, getFitPattern(date));
	}
	
	/**
	 * 以指定的模式解析西元日期文字建構民國日期物件
	 * 
	 * @param date 西元日期文字
	 * @param pattern 模式
	 * @throws ParseException
	 */
	public TwDate (String date, String pattern) throws ParseException {
		this(getDateFormat(pattern).parse(date));
		this.pattern = pattern;
	}
	
	/**
	 * 以日期物件建構民國日期物件
	 * 
	 * @param date 日期物件
	 */
	public TwDate (Date date) {
		this();
		this.cal.setTime(date);
	}
	
	/**
	 * 以時間戳物件建構民國日期物件
	 * 
	 * @param time 時間戳物件
	 */
	public TwDate (Timestamp time) {
		this.cal = Calendar.getInstance();
		this.cal.setTime(time);
		this.pattern = TIME_STYLE;
	}
	
	/**
	 * 以日曆物件建構民國日期物件
	 * 
	 * @param date 日曆物件
	 */
	public TwDate (Calendar date) {
		this.cal = date;
		this.pattern = YYYYMMDD;
	}
	
	/**
	 * 由日期文字轉換民國日期物件
	 * 
	 * @param date 日期文字
	 * @return 民國日期物件
	 */
	public static TwDate form(String date) {
		if (StringUtils.isNotBlank(date)) {
			if (date.length() % 2 == 0) {
				return formAD(date);
			} else {
				return formTW(date);
			}
		}
		return null;
	}
	
	/**
	 * 由民國日期文字轉換民國日期物件
	 * 
	 * @param tw
	 * @return 民國日期物件
	 */
	public static TwDate formTW(String tw) {
		if (StringUtils.isNotBlank(tw)) {
			if (!tw.startsWith("000") && !tw.startsWith("-00")) {
				if (tw.length() >= 3) {
					int twYear = Math.abs(Integer.parseInt(tw.substring(0, 3)));
					try {
						return new TwDate(
							String.valueOf(tw.startsWith("-") ? 1912 - twYear : 1911 + twYear) + tw.substring(3)
						);
					} catch (ParseException e) {
					}
				}
			}
			TwDate twDate = new TwDate((Calendar)null);
			twDate.original = tw;
			return twDate;
		}
		return null;
	}
	
	/**
	 * 由西元日期文字轉換民國日期物件
	 * 
	 * @param ad
	 * @return 民國日期物件
	 * @throws ParseException
	 */
	public static TwDate formAD(String ad) {
		if (StringUtils.isNotBlank(ad)) {
			try {
				return new TwDate(ad);
			} catch (ParseException e) {
				TwDate twDate = new TwDate((Calendar)null);
				twDate.original = ad;
				return twDate;
			}
		}
		return null;
	}
	
	/**
	 * 由日期物件轉換民國日期物件
	 * 
	 * @param date
	 * @return 民國日期物件
	 */
	public static TwDate form(Date date) {
		if (date != null) {
			return new TwDate(date);
		}
		return null;
	}
	
	/**
	 * 由時間戳物件轉換民國日期物件
	 * 
	 * @param timestamp
	 * @return 民國日期物件
	 */
	public static TwDate form(Timestamp timestamp) {
		if (timestamp != null) {
			return new TwDate(timestamp);
		}
		return null;
	}
	
	/**
	 * 設定為年模式
	 * 
	 * @return 民國日期物件
	 */
	public TwDate styleY() {
		this.pattern = YYYY;
		return this;
	}
	
	/**
	 * 設定為年月模式
	 * 
	 * @return 民國日期物件
	 */
	public TwDate styleYM() {
		this.pattern = YYYYMM;
		return this;
	}
	
	/**
	 * 設定為年月日模式
	 * 
	 * @return 民國日期物件
	 */
	public TwDate styleYMD() {
		this.pattern = YYYYMMDD;
		return this;
	}
	
	/**
	 * 轉換為日期物件
	 * 
	 * @return 日期物件
	 */
	public Date toDate() {
		return isValid() ? cal.getTime() : null;
	}
	
	/**
	 * 轉換為SQL日期物件
	 * 
	 * @return SQL日期物件
	 */
	public java.sql.Date toSQLDate() {
		return isValid() ? new java.sql.Date(cal.getTimeInMillis()) : null;
	}
	
	/**
	 * 轉換為時間戳物件
	 * 
	 * @return 時間戳物件
	 */
	public Timestamp toTimestamp() {
		return isValid() ? new Timestamp(cal.getTimeInMillis()) : null;
	}
	
	/**
	 * 轉換為日曆物件
	 * 
	 * @return 日曆物件
	 */
	public Calendar toCalendar() {
		return isValid() ? cal : null;
	}
	
	/**
	 * 轉換為西元日期文字
	 * 
	 * @return 西元日期文字
	 */
	public String toAD() {
		return isValid() ? format(pattern) : null;
	}
	
	/**
	 * 是否為可用的民國日期物件
	 * 
	 * @return boolean
	 */
	public boolean isValid() {
		return cal != null;
	}
	
	/**
	 * 是否為不可用的民國日期物件
	 * 
	 * @return boolean
	 */
	public boolean isNotValid() {
		return !isValid();
	}
	
	/**
	 * 取得原始的民國日期文字
	 * 
	 * @return 原始的民國日期文字
	 */
	public String toOriginalTwDate() {
		return original != null ? original : format(pattern.replace("yyyy", "TTT"));
	}
	
	/**
	 * 以預設的民國日期模式格式化
	 * 
	 * @return 民國日期文字
	 */
	public String format() {
		if (isNotValid()) {
			return StringUtils.EMPTY;
		} else if (YYYY.equals(pattern)) {
			return format(TW_Y_STYLE);
		} else if (YYYYMM.equals(pattern)) {
			return format(TW_YM_STYLE);
		} else if (YYYYMMDD.equals(pattern)) {
			return format(TW_YMD_STYLE);
		} else if (TIME_STYLE.equals(pattern)) {
			return format(TW_TIME_STYLE);
		}
		return format(pattern.replace("yyyy", "TTT"));
	}
	
	/**
	 * 以指定的日期模式格式化
	 * 
	 * @param pattern 日期模式
	 * @return 格式化字串
	 */
	public String format(String pattern) {
		if (isNotValid()) {
			return original;
		}
		StringBuffer buff = new StringBuffer ();
		Pattern p = Pattern.compile("T+");
		Matcher m = p.matcher(pattern);
		while (m.find()) {
			int yearAD = cal.get(Calendar.YEAR);
			int yearTW = yearAD > 1911 ? yearAD - 1911 : 1912 - yearAD;
			boolean negative = yearAD <= 1911;
			String flg = m.group();
			char[] chars = new char[Math.max(getIntLength(yearTW) + (negative ? 1 : 0), flg.length())];
			Arrays.fill(chars, '0');
			for (int i = chars.length - 1 ; i >= 0 ; i--) {
				chars[i] = Character.forDigit(yearTW % 10, 10);
				yearTW /= 10;
				if (yearTW == 0) {
					break;
				}
			}
			if (negative) {
				chars[0] = '-';
			}
			m.appendReplacement(buff, new String(chars));
		}
		m.appendTail(buff);
		
		return getDateFormat(buff.toString()).format(cal.getTime());
	}
	
	/**
	 * 指定西元年分
	 * 
	 * @param value 西元年
	 * @return 民國日期物件
	 */
	public TwDate setADYear(int value) {
		this.cal.set(Calendar.YEAR, value);
		return this;
	}
	
	/**
	 * 取得西元年分
	 * 
	 * @return 西元年分
	 */
	public int getADYear() {
		return cal.get(Calendar.YEAR);
	}
	
	/**
	 * 指定民國年分
	 * 
	 * @param value 民國年
	 * @return 民國日期物件
	 */
	public TwDate setTWYear(int value) {
		if (value == 0) {
			throw new RuntimeException("can not set 0 to tw year");
		}
		this.cal.set(Calendar.YEAR, value + (value > 0 ? 1911 : 1912));
		return this;
	}
	
	/**
	 * 取得民國年分
	 * 
	 * @return 民國年分
	 */
	public int getTWYear() {
		int year = cal.get(Calendar.YEAR);
		return year > 1911 ? year - 1911 : 1912 - year;
	}
	
	/**
	 * 增減民國日期物件年分
	 * 
	 * @param value 增減值
	 * @return 民國日期物件
	 */
	public TwDate addYear(int value) {
		this.cal.add(Calendar.YEAR, value);
		return this;
	}
	
	/**
	 * 取得月分
	 * 
	 * @return 月份[一月:1,二月:2...]
	 */
	public int getMonth() {
		return cal.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * 指定月分
	 * 
	 * @param value 月份[一月:1,二月:2...]
	 * @return 民國日期物件
	 */
	public TwDate setMonth(int value) {
		this.cal.set(Calendar.MONTH, value - 1);
		return this;
	}
	
	/**
	 * 增減民國日期物件月分
	 * 
	 * @param value 增減值
	 * @return 民國日期物件
	 */
	public TwDate addMonth(int value) {
		this.cal.add(Calendar.MONTH, value);
		return this;
	}
	
	/**
	 * 取得日
	 * 
	 * @return 日
	 */
	public int getDay() {
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 指定日
	 * 
	 * @param value 日
	 * @return 民國日期物件
	 */
	public TwDate setDay(int value) {
		this.cal.set(Calendar.DAY_OF_MONTH, value);
		return this;
	}
	
	/**
	 * 增減民國日期物件日
	 * 
	 * @param value 增減值
	 * @return 民國日期物件
	 */
	public TwDate addDay(int value) {
		this.cal.add(Calendar.DAY_OF_MONTH, value);
		return this;
	}
	
	/**
	 * 取得時
	 * 
	 * @return 時
	 */
	public int getHour() {
		return cal.get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 指定時
	 * 
	 * @param value 時
	 * @return 民國日期物件
	 */
	public TwDate setHour(int value) {
		this.cal.set(Calendar.HOUR_OF_DAY, value);
		return this;
	}
	
	/**
	 * 增減民國日期物件時
	 * 
	 * @param value 增減值
	 * @return 民國日期物件
	 */
	public TwDate addHour(int value) {
		this.cal.add(Calendar.DAY_OF_MONTH, value);
		return this;
	}
	
	/**
	 * 取得分
	 * 
	 * @return 分
	 */
	public int getMinute() {
		return cal.get(Calendar.MINUTE);
	}
	
	/**
	 * 指定分
	 * 
	 * @param value 分
	 * @return 民國日期物件
	 */
	public TwDate setMinute(int value) {
		this.cal.set(Calendar.MINUTE, value);
		return this;
	}
	
	/**
	 * 增減民國日期物件分
	 * 
	 * @param value 增減值
	 * @return 民國日期物件
	 */
	public TwDate addMinute(int value) {
		this.cal.add(Calendar.MINUTE, value);
		return this;
	}
	
	/**
	 * 取得秒
	 * 
	 * @return 秒
	 */
	public int getSecond() {
		return cal.get(Calendar.SECOND);
	}
	
	/**
	 * 指定秒
	 * 
	 * @param value 秒
	 * @return 民國日期物件
	 */
	public TwDate setSecond(int value) {
		this.cal.set(Calendar.SECOND, value);
		return this;
	}
	
	/**
	 * 增減民國日期物件秒
	 * 
	 * @param value 增減值
	 * @return 民國日期物件
	 */
	public TwDate addSecond(int value) {
		this.cal.add(Calendar.SECOND, value);
		return this;
	}
	
	/**
	 * 取得毫秒
	 * 
	 * @return 毫秒
	 */
	public int getMilliSecond() {
		return cal.get(Calendar.MILLISECOND);
	}
	
	/**
	 * 指定毫秒
	 * 
	 * @param value 毫秒
	 * @return 民國日期物件
	 */
	public TwDate setMilliSecond(int value) {
		this.cal.set(Calendar.MILLISECOND, value);
		return this;
	}
	
	/**
	 * 增減民國日期物件毫秒
	 * 
	 * @param value 增減值
	 * @return 民國日期物件
	 */
	public TwDate addMilliSecond(int value) {
		this.cal.add(Calendar.MILLISECOND, value);
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cal == null) ? 0 : cal.hashCode());
		result = prime * result
				+ ((original == null) ? 0 : original.hashCode());
		result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TwDate other = (TwDate) obj;
		if (cal == null) {
			if (other.cal != null)
				return false;
		} else if (!cal.equals(other.cal))
			return false;
		if (original == null) {
			if (other.original != null)
				return false;
		} else if (!original.equals(other.original))
			return false;
		if (pattern == null) {
			if (other.pattern != null)
				return false;
		} else if (!pattern.equals(other.pattern))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return format(pattern);
	}

	private static String getFitPattern(String date) {
		switch (date.length()) {
			case 4:
				return YYYY;
			case 6:
				return YYYYMM;
			case 8:
				return YYYYMMDD;
			case 10:
				return YYYYMMDDHH;
			case 12:
				return YYYYMMDDHHMM;
			case 14:
				return TIME_STYLE;
		}
		throw new RuntimeException("未知的日期長度");
	}
	
	private static int getIntLength(int val) {
		int length = 0;
		for ( ; ; ) {
			val /= 10;
			length++;
			if (val == 0) break;
		}
		return length;
	}
	
	private static SimpleDateFormat getDateFormat (String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		df.setLenient(false);
		return df;
	}
	
}

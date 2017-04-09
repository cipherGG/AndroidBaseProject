package com.android.base.domain;

/**
 * Created by gg on 2017/4/7.
 * 时间单位类
 */
public class TimeUnit {

    /* 毫秒与毫秒的倍数 */
    public static final long MSEC = 1;
    /* 秒与毫秒的倍数 */
    public static final long SEC = MSEC * 1000;
    /* 分与毫秒的倍数 */
    public static final long MIN = SEC * 60;
    /* 时与毫秒的倍数 */
    public static final long HOUR = MIN * 60;
    /* 天与毫秒的倍数 */
    public static final long DAY = HOUR * 24;
    /* 天与毫秒的倍数 */
    public static final long MONTH = DAY * 30;
    /* 天与毫秒的倍数 */
    public static final long YEAR = MONTH * 12;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    /* between是时间间隔 */
    public static TimeUnit getBetween(long between) {
        TimeUnit unit = new TimeUnit();
        // year
        int year = (int) (between / YEAR);
        unit.setYear(year);
        // month
        int mouth;
        long monthB;
        if (year > 0) {
            long yearB = between % year;
            mouth = (int) (yearB / MONTH);
            monthB = yearB % MONTH;
        } else {
            mouth = (int) (between / MONTH);
            monthB = between % MONTH;
        }
        unit.setMonth(mouth);
        // day
        int day;
        long dayB;
        if (mouth > 0) {
            day = (int) (monthB / DAY);
            dayB = monthB % DAY;
        } else {
            day = (int) (between / DAY);
            dayB = between % DAY;
        }
        unit.setDay(day);
        // hour
        int hour;
        long hourB;
        if (day > 0) {
            hour = (int) (dayB / HOUR);
            hourB = dayB % HOUR;
        } else {
            hour = (int) (between / HOUR);
            hourB = between % HOUR;
        }
        unit.setHour(hour);
        // minute
        int minute;
        long minuteB;
        if (hour > 0) {
            minute = (int) (hourB / MIN);
            minuteB = hourB % MIN;
        } else {
            minute = (int) (between / MIN);
            minuteB = between % MIN;
        }
        unit.setMinute(minute);
        // minute
        int second;
        if (minute > 0) {
            second = (int) (minuteB / SEC);
        } else {
            second = (int) (between / SEC);
        }
        unit.setSecond(second);
        return unit;
    }

    public String getStr1() {
        String yea = "";
        if (year > 0) {
            yea = year + "年";
        }
        String mon = "";
        if (month > 0) {
            mon = month + "月";
        }
        String day = "";
        if (this.day > 0) {
            day = this.day + "日";
        }
        String hou = "";
        if (hour > 0) {
            hou = hour + "时";
        }
        String min = "";
        if (minute > 0) {
            min = minute + "分";
        }
        String sec = "";
        if (second > 0) {
            sec = second + "秒";
        }
        return yea + mon + day + hou + min + sec;
    }

    public String getStr2() {
        String yea = "";
        if (year > 0) {
            yea = year + "年";
        }
        String mon = "";
        if (month > 0) {
            mon = month + "月";
        }
        String day = "";
        if (this.day > 0) {
            day = this.day + "天";
        }
        String hou = "";
        if (hour > 0) {
            hou = hour + "小时";
        }
        String min = "";
        if (minute > 0) {
            min = minute + "分钟";
        }
        return yea + mon + day + hou + min;
    }

    @Override
    public String toString() {
        return year + "年" +
                month + "月" +
                day + "日" +
                hour + "时" +
                minute + "分" +
                second + '秒';
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}

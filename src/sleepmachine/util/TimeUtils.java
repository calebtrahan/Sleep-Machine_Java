package sleepmachine.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by caleb on 11/6/15.
 */
public class TimeUtils {

    public static String getformattedhoursandminutes(int hours, int minutes) {
        StringBuilder text = new StringBuilder();
        if (hours > 0) {
            text.append(hours).append(" Hr");
            if (hours > 1) {text.append("s");}
        }
        if (minutes > 0 && hours > 0) {text.append(" & ");}
        if (minutes > 0) {
            text.append(minutes).append(" Min");
            if (minutes > 1) {text.append("s");}
        }
        return text.toString();
    }

    public static String getapproximateendtime(int totalminutes) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, totalminutes);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(cal.getTime());
    }

    public static String formatlengthshort(int sec) {
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        if (sec >= 3600) {hours = sec / 3600; sec -= hours * 3600;}
        if (sec >= 60) {minutes = sec / 60; sec -= minutes * 60;}
        seconds = sec;
        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    public static String formattedtimefromcalendar(Calendar calendar) {
        StringBuilder time = new StringBuilder();
        if (calendar.get(Calendar.HOUR_OF_DAY) != 0) {time.append(String.format("%d", calendar.get(Calendar.HOUR)));}
        else {time.append("12");}
        time.append(":");
        time.append(String.format("%02d", calendar.get(Calendar.MINUTE)));
        time.append(" ");
        int i = calendar.get(Calendar.AM_PM);
        if (i == 0) {time.append("AM");}
        else {time.append("PM");}
        return time.toString();
    }

    public static String formatlengthlong(int sec) {
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        if (sec >= 3600) {
            hours = sec / 3600;
            sec -= hours * 3600;
        }
        if (sec >= 60) {
            minutes = sec / 60;
            sec -= minutes * 60;
        }
        seconds = sec;
        boolean valid = hours > 0 || minutes > 0;
        StringBuilder text = new StringBuilder();
        if (valid) {
            if (hours > 0) {
                text.append(hours);
                text.append(" Hr");
                if (hours > 1) text.append("s");
            }
            if (minutes > 0) {
                text.append(minutes);
                text.append(" Min");
                if (minutes > 1) text.append("s");
            }
            return text.toString();
        } else {
            return null;
        }
    }

    public static int getMinutesTillCalendar(Calendar calendar) {
        Calendar now = Calendar.getInstance();
        long milliseconds = calendar.getTimeInMillis() - now.getTimeInMillis();
        return (int) (milliseconds / 1000) / 60;
    }
}

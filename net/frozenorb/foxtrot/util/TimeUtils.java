package net.frozenorb.foxtrot.util;

import java.util.concurrent.*;

public class TimeUtils
{
    public static String getDurationBreakdown(long millis) {
        if (millis < 0L) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }
        final long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        final StringBuilder sb = new StringBuilder(64);
        if (hours != 0L) {
            sb.append(hours);
            sb.append(" hours");
        }
        if (minutes != 0L) {
            sb.append(((hours != 0L) ? ", " : "") + minutes);
            sb.append(" minutes");
        }
        if (seconds != 0L) {
            sb.append(((minutes != 0L) ? ", " : "") + seconds);
            sb.append(" seconds");
        }
        return sb.toString();
    }
    
    public static String getConvertedTime(long i) {
        i = Math.abs(i);
        final int hours = (int)Math.floor(i / 3600L);
        final int remainder = (int)(i % 3600L);
        final int minutes = remainder / 60;
        final int seconds = remainder % 60;
        if (seconds == 0 && minutes == 0) {
            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + "0 seconds";
        }
        if (minutes == 0) {
            if (seconds == 1) {
                return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%s seconds", seconds);
            }
            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%s seconds", seconds);
        }
        else if (seconds == 0) {
            if (minutes == 1) {
                return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm", minutes);
            }
            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm", minutes);
        }
        else if (seconds == 1) {
            if (minutes == 1) {
                return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm %ss", minutes, seconds);
            }
            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm %ss", minutes, seconds);
        }
        else {
            if (minutes == 1) {
                return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + "" + String.format("%sm %ss", minutes, seconds);
            }
            final String toReturn = String.format("%sm %ss", minutes, seconds);
            return ((hours != 0) ? ((hours == 1) ? (hours + "h") : (hours + "h")) : "") + " " + toReturn;
        }
    }
    
    public static String getMMSS(final int seconds) {
        final int millis = seconds * 1000;
        final int sec = millis / 1000 % 60;
        final int min = millis / 60000 % 60;
        final int hr = millis / 3600000 % 24;
        return ((hr > 0) ? String.format("%02d", hr) : "") + String.format("%02d:%02d", min, sec);
    }
}

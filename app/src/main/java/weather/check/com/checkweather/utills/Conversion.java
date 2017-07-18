package weather.check.com.checkweather.utills;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Pial on 7/6/2017.
 */

public class Conversion {
    public static double KelvinToCelsius(double kelvin) {
        return Math.abs(kelvin - 273.15);
    }

    public static double KelvinToFahrenheit(double kelvin) {
        return ((9 / 5) * (kelvin - 273) + 32);
    }

    //Meter per second to miles per hour convertion
    public static double msTomph(double speed) {
        return speed * (3600.0 / 1609.3);
    }

    public static double msTokmh(double speed) {
        return speed * (3600.0 / 1000.0);
    }

    public String UnixTimestampToDate(int timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.format(new Date((long) timestamp * 1000L)); // 1000 is for converting seconds to miliseconds
    }

    public String UnixTimestampToDayofWeek(int timestamp) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
//        dateFormat.setTimeZone(TimeZone.getDefault());
//        return dateFormat.format(new Date(timestamp * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        sdf.setTimeZone(TimeZone.getDefault());
        Date netDate = (new Date((long) timestamp * 1000L));
        return sdf.format(netDate);

    }
}

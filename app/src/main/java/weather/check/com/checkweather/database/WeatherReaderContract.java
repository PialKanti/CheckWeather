package weather.check.com.checkweather.database;

import android.provider.BaseColumns;

/**
 * Created by Pial on 7/5/2017.
 */

public final class WeatherReaderContract {
    private WeatherReaderContract(){

    }

    public static class WeatherData implements BaseColumns{
        public static String TABLE_NAME = "weather_of_today";
        public static String COLUMN_CITY = "city";
        public static String COLUMN_COUNTRY = "country";
        public static String COLUMN_TEMPARATURE = "temp";
        public static String COLUMN_WEATHER_DESC = "weatherdesc";
        public static String COLUMN_WINDSPEED = "windspeed";
        public static String COLUMN_HUMIDITY = "humidity";
        public static String COLUMN_SUNRISE = "sunrise";
        public static String COLUMN_SUNSET = "sunset";
        public static String COLUMN_ICONCODE = "icon";

    }

    public static class ForecastData implements BaseColumns{
        public static String TABLE_NAME = "weather_forecast";
        public static String COLUMN_TEMPARATURE = "temp";
        public static String COLUMN_DAY = "day";
        public static String COLUMN_ICONCODE = "icon";
    }
}

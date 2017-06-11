package data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import POJO.WeatherDetails;
import POJO.WeatherForecast;

/**
 * Created by Pial on 5/20/2017.
 */

public class ParseJsonData {
    private String weatherDescription;
    private String CityName;
    private double Temperatue;
    private double WindSpeed;
    private int Humidity;
    private String CountryCode, CountryName;
    private String SunriseTime;
    private String SunsetTime;
    private String IconCode;

    public WeatherDetails parseMainJson(JSONObject mainJsonObject) {
        WeatherDetails wDetails = null;
        try {
            JSONArray weather = mainJsonObject.getJSONArray("weather");
            for (int i = 0; i < 1; i++) {
                JSONObject jsonObject = weather.getJSONObject(i);
                weatherDescription = jsonObject.getString("description");
                IconCode = jsonObject.getString("icon");
            }

            CityName = mainJsonObject.getString("name");

            JSONObject main = mainJsonObject.getJSONObject("main");
            Temperatue = KelvinToCelsius(main.getDouble("temp"));
            Humidity = main.getInt("humidity");

            JSONObject wind = mainJsonObject.getJSONObject("wind");
            WindSpeed = msTomph(wind.getDouble("speed"));

            JSONObject sys = mainJsonObject.getJSONObject("sys");
            CountryCode = sys.getString("country");
            Locale locale = new Locale("", CountryCode);
            CountryName = locale.getDisplayCountry();
            SunriseTime = UnixTimestampToDate(sys.getInt("sunrise"));
            SunsetTime = UnixTimestampToDate(sys.getInt("sunset"));


            wDetails = new WeatherDetails();
            wDetails.setWeatherDescription(weatherDescription);
            wDetails.setCityName(CityName);
            wDetails.setCountryName(CountryName);
            wDetails.setTemperatue(Temperatue);
            wDetails.setWindSpeed(WindSpeed);
            wDetails.setHumidity(Humidity);
            wDetails.setSunriseTime(SunriseTime);
            wDetails.setSunsetTime(SunsetTime);
            wDetails.setIconCode(IconCode);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Check", "Error parsing Main Json");
        }

        return wDetails;
    }

    public WeatherDetails parseForecastJson(JSONObject jsobject, WeatherDetails details) {
        WeatherDetails weatherDetails = details;
        ArrayList<WeatherForecast> forecasts = new ArrayList<>();
        String IconCode = "10d";
        Double Temperatue;
        try {
            JSONArray ForecastList = jsobject.getJSONArray("list");
            for (int i = 1; i < ForecastList.length(); i++) {
                JSONObject PerDayWeather = ForecastList.getJSONObject(i);

                JSONObject temp = PerDayWeather.getJSONObject("temp");
                Temperatue = KelvinToCelsius(temp.getDouble("day"));


                String DayofWeek = UnixTimestampToDayofWeek(PerDayWeather.getInt("dt"));

                JSONArray weather = PerDayWeather.getJSONArray("weather");
                for (int j = 0; j < 1; j++) {
                    JSONObject jsonObject = weather.getJSONObject(j);
                    IconCode = jsonObject.getString("icon");
                }

                WeatherForecast singleForecast = new WeatherForecast();
                singleForecast.setTemperature(Temperatue);
                singleForecast.setDay(DayofWeek);
                singleForecast.setIconCode(IconCode);

                forecasts.add(singleForecast);

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Check", "Error parsing Forecast Json");
        }

        weatherDetails.setForecasts(forecasts);

        return weatherDetails;
    }

    public double KelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }

    //Meter per second to miles per hour convertion
    public double msTomph(double speed) {
        return speed * (3600 / 1609.3);
    }

    public String UnixTimestampToDate(int timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.format(new Date((long)timestamp * 1000L)); // 1000 is for converting seconds to miliseconds
    }

    public String UnixTimestampToDayofWeek(int timestamp) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
//        dateFormat.setTimeZone(TimeZone.getDefault());
//        return dateFormat.format(new Date(timestamp * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        sdf.setTimeZone(TimeZone.getDefault());
        Date netDate = (new Date((long)timestamp * 1000L));
        return sdf.format(netDate);
    }

}

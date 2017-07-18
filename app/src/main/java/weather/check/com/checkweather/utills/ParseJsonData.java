package weather.check.com.checkweather.utills;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import weather.check.com.checkweather.POJO.WeatherDetails;
import weather.check.com.checkweather.POJO.WeatherForecast;

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
        Conversion conversion = new Conversion();
        try {
            JSONArray weather = mainJsonObject.getJSONArray("weather");
            for (int i = 0; i < 1; i++) {
                JSONObject jsonObject = weather.getJSONObject(i);
                weatherDescription = jsonObject.getString("description");
                IconCode = jsonObject.getString("icon");
            }

            CityName = mainJsonObject.getString("name");

            JSONObject main = mainJsonObject.getJSONObject("main");
            Temperatue = main.getDouble("temp");
            Humidity = main.getInt("humidity");

            JSONObject wind = mainJsonObject.getJSONObject("wind");
            WindSpeed = wind.getDouble("speed");

            JSONObject sys = mainJsonObject.getJSONObject("sys");
            CountryCode = sys.getString("country");
            Locale locale = new Locale("", CountryCode);
            CountryName = locale.getDisplayCountry();
            SunriseTime = conversion.UnixTimestampToDate(sys.getInt("sunrise"));
            SunsetTime = conversion.UnixTimestampToDate(sys.getInt("sunset"));


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

    public ArrayList<WeatherForecast> parseForecastJson(JSONObject jsobject) {
        Conversion conversion = new Conversion();
        ArrayList<WeatherForecast> forecasts = new ArrayList<>();
        String IconCode = "10d";
        Double Temperature;
        try {
            JSONArray ForecastList = jsobject.getJSONArray("list");
            for (int i = 1; i < ForecastList.length(); i++) {
                JSONObject PerDayWeather = ForecastList.getJSONObject(i);

                JSONObject temp = PerDayWeather.getJSONObject("temp");
                Temperature = temp.getDouble("day");



                String DayofWeek = conversion.UnixTimestampToDayofWeek(PerDayWeather.getInt("dt"));

                JSONArray weather = PerDayWeather.getJSONArray("weather");
                for (int j = 0; j < 1; j++) {
                    JSONObject jsonObject = weather.getJSONObject(j);
                    IconCode = jsonObject.getString("icon");
                }

                WeatherForecast singleForecast = new WeatherForecast();
                singleForecast.setTemperature(Temperature);
                singleForecast.setDay(DayofWeek);
                singleForecast.setIconCode(IconCode);

                forecasts.add(singleForecast);

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Check", "Error parsing Forecast Json");
        }


        return forecasts;
    }

}

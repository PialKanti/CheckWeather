package POJO;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Pial on 5/20/2017.
 */

public class WeatherDetails implements Serializable{
    private String weatherDescription;
    private String CityName;
    private String CountryName;
    private double Temperatue;
    private double WindSpeed;
    private double Humidity;
    private String SunriseTime;
    private String SunsetTime;
    private String IconCode;
    private ArrayList<WeatherForecast> forecasts;

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public double getTemperatue() {
        return Temperatue;
    }

    public void setTemperatue(double temperatue) {
        Temperatue = temperatue;
    }

    public double getWindSpeed() {
        return WindSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        WindSpeed = windSpeed;
    }

    public double getHumidity() {
        return Humidity;
    }

    public void setHumidity(double humidity) {
        Humidity = humidity;
    }

    public String getSunriseTime() {
        return SunriseTime;
    }

    public void setSunriseTime(String sunriseTime) {
        SunriseTime = sunriseTime;
    }

    public String getSunsetTime() {
        return SunsetTime;
    }

    public void setSunsetTime(String sunsetTime) {
        SunsetTime = sunsetTime;
    }

    public String getIconCode() {
        return IconCode;
    }

    public void setIconCode(String iconCode) {
        IconCode = iconCode;
    }

    public ArrayList<WeatherForecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(ArrayList<WeatherForecast> forecasts) {
        this.forecasts = forecasts;
    }
}

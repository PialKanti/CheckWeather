package POJO;

/**
 * Created by Pial on 5/20/2017.
 */

public class WeatherAPIKey {
    private final String appid = "44ebb60a8e65c4301ba295a2f0d990dd";
    private String url;
    public static String ImageURL = "http://openweathermap.org/img/w/";
    private String forecastURL;

    public String getUrl() {
        return url;
    }

    public void setUrl(String CityName) {
        this.url = "http://api.openweathermap.org/data/2.5/weather?q="+CityName+"&appid="+appid;
    }

    public void setForecastURL(String CityName){
        this.forecastURL = "http://api.openweathermap.org/data/2.5/forecast/daily?q="+CityName+"&cnt=6&appid="+appid;
    }

    public String getForecastURL() {
        return forecastURL;
    }
}

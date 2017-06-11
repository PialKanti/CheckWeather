package POJO;

import java.io.Serializable;

/**
 * Created by Pial on 5/21/2017.
 */

public class WeatherForecast implements Serializable {
    private double Temperature;
    private String Day;
    private String IconCode;

    public double getTemperature() {
        return Temperature;
    }

    public void setTemperature(double temperature) {
        Temperature = temperature;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getIconCode() {
        return IconCode;
    }

    public void setIconCode(String iconCode) {
        IconCode = iconCode;
    }
}

package weather.check.com.checkweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.text.WordUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;

import POJO.WeatherAPIKey;
import POJO.WeatherDetails;
import POJO.WeatherForecast;

public class WeatherShowActivity extends AppCompatActivity {

    private LinearLayout Main_background, top_left, top_right, bottom_left, bottom_right;
    private ImageView IconImage, WindSpeedImage, HumidityImage, SunriseImage, SunsetImage;
    private TextView CityName, WeatherDescription, Temperature;
    private TextView WindSpeedText, WindSpeedValue, HumidityText, HumidityValue, SunriseText, SunriseValue, SunsetText, SunsetValue;

    private LinearLayout Day1View,Day2View,Day3View,Day4View,Day5View;
    private ImageView Day1Image,Day2Image,Day3Image,Day4Image,Day5Image;
    private TextView Day1,Day2,Day3,Day4,Day5;
    private TextView Day1Temp,Day2Temp,Day3Temp,Day4Temp,Day5Temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_show);

        WeatherDetails Wdetails = (WeatherDetails) getIntent().getSerializableExtra("WeatherDetails");

        Main_background = (LinearLayout)findViewById(R.id.MainContent);

        CityName = (TextView) findViewById(R.id.CityName);
        WeatherDescription = (TextView) findViewById(R.id.WeatherDescription);

        IconImage = (ImageView) findViewById(R.id.IconImage);
        Temperature = (TextView) findViewById(R.id.Temperature);


        top_left = (LinearLayout) findViewById(R.id.top_left);
        top_right = (LinearLayout) findViewById(R.id.top_right);
        bottom_left = (LinearLayout) findViewById(R.id.bottom_left);
        bottom_right = (LinearLayout) findViewById(R.id.bottom_right);

        WindSpeedImage = (ImageView) top_left.findViewById(R.id.ContentImage);
        HumidityImage = (ImageView) top_right.findViewById(R.id.ContentImage);
        SunriseImage = (ImageView) bottom_left.findViewById(R.id.ContentImage);
        SunsetImage = (ImageView) bottom_right.findViewById(R.id.ContentImage);

        WindSpeedText = (TextView) top_left.findViewById(R.id.ContentText);
        WindSpeedValue = (TextView) top_left.findViewById(R.id.ContentTextValue);

        HumidityText = (TextView) top_right.findViewById(R.id.ContentText);
        HumidityValue = (TextView) top_right.findViewById(R.id.ContentTextValue);

        SunriseText = (TextView) bottom_left.findViewById(R.id.ContentText);
        SunriseValue = (TextView) bottom_left.findViewById(R.id.ContentTextValue);

        SunsetText = (TextView) bottom_right.findViewById(R.id.ContentText);
        SunsetValue = (TextView) bottom_right.findViewById(R.id.ContentTextValue);

        Day1View = (LinearLayout)findViewById(R.id.Day1);
        Day2View = (LinearLayout)findViewById(R.id.Day2);
        Day3View = (LinearLayout)findViewById(R.id.Day3);
        Day4View = (LinearLayout)findViewById(R.id.Day4);
        Day5View = (LinearLayout)findViewById(R.id.Day5);

        Day1Image = (ImageView)Day1View.findViewById(R.id.forecastImage);
        Day2Image = (ImageView)Day2View.findViewById(R.id.forecastImage);
        Day3Image = (ImageView)Day3View.findViewById(R.id.forecastImage);
        Day4Image = (ImageView)Day4View.findViewById(R.id.forecastImage);
        Day5Image = (ImageView)Day5View.findViewById(R.id.forecastImage);

        Day1 = (TextView)Day1View.findViewById(R.id.forecastDay);
        Day2= (TextView)Day2View.findViewById(R.id.forecastDay);
        Day3= (TextView)Day3View.findViewById(R.id.forecastDay);
        Day4= (TextView)Day4View.findViewById(R.id.forecastDay);
        Day5= (TextView)Day5View.findViewById(R.id.forecastDay);

        Day1Temp = (TextView)Day1View.findViewById(R.id.forecastTemp);
        Day2Temp = (TextView)Day2View.findViewById(R.id.forecastTemp);
        Day3Temp = (TextView)Day3View.findViewById(R.id.forecastTemp);
        Day4Temp = (TextView)Day4View.findViewById(R.id.forecastTemp);
        Day5Temp = (TextView)Day5View.findViewById(R.id.forecastTemp);

        //Setting all layout resources
        if(Wdetails.getIconCode().charAt(Wdetails.getIconCode().length()-1) == 'd'){
            Main_background.setBackground(getResources().getDrawable(R.drawable.dayimage));
        }else if(Wdetails.getIconCode().charAt(Wdetails.getIconCode().length()-1) == 'n'){
            Main_background.setBackground(getResources().getDrawable(R.drawable.nightimage));
        }else{
            Main_background.setBackground(getResources().getDrawable(R.drawable.dayimage));
        }

        CityName.setText(Wdetails.getCityName() + ", " + Wdetails.getCountryName());


        WeatherDescription.setText(WordUtils.capitalize(Wdetails.getWeatherDescription()));

        //Showing Image from URL to ImageView
        Picasso.with(getApplicationContext()).load(WeatherAPIKey.ImageURL + Wdetails.getIconCode() + ".png").into(IconImage);

        Temperature.setText(new DecimalFormat("#0.0").format(Wdetails.getTemperatue()) + " \u2103");

        WindSpeedImage.setImageResource(R.mipmap.ic_wind_speed);
        HumidityImage.setImageResource(R.mipmap.ic_humidity);
        SunriseImage.setImageResource(R.mipmap.ic_sunrise);
        SunsetImage.setImageResource(R.mipmap.ic_sunset);

        WindSpeedText.setText("Wind Speed");
        WindSpeedValue.setText(new DecimalFormat("#0.00").format(Wdetails.getWindSpeed()) + " mph");

        HumidityText.setText("Humidity");
        HumidityValue.setText(new DecimalFormat("#0").format(Wdetails.getHumidity()) + "%");

        SunriseText.setText("Sunrise");
        SunriseValue.setText(Wdetails.getSunriseTime());

        SunsetText.setText("Sunset");
        SunsetValue.setText(Wdetails.getSunsetTime());

        ArrayList<WeatherForecast> forecasts = Wdetails.getForecasts();

        Picasso.with(getApplicationContext()).load(WeatherAPIKey.ImageURL + forecasts.get(0).getIconCode() + ".png").into(Day1Image);
        Picasso.with(getApplicationContext()).load(WeatherAPIKey.ImageURL + forecasts.get(1).getIconCode() + ".png").into(Day2Image);
        Picasso.with(getApplicationContext()).load(WeatherAPIKey.ImageURL + forecasts.get(2).getIconCode() + ".png").into(Day3Image);
        Picasso.with(getApplicationContext()).load(WeatherAPIKey.ImageURL + forecasts.get(3).getIconCode() + ".png").into(Day4Image);
        Picasso.with(getApplicationContext()).load(WeatherAPIKey.ImageURL + forecasts.get(4).getIconCode() + ".png").into(Day5Image);

        Day1.setText(forecasts.get(0).getDay());
        Day2.setText(forecasts.get(1).getDay());
        Day3.setText(forecasts.get(2).getDay());
        Day4.setText(forecasts.get(3).getDay());
        Day5.setText(forecasts.get(4).getDay());

        Day1Temp.setText(new DecimalFormat("#0").format(forecasts.get(0).getTemperature())+ " \u2103");
        Day2Temp.setText(new DecimalFormat("#0").format(forecasts.get(1).getTemperature())+ " \u2103");
        Day3Temp.setText(new DecimalFormat("#0").format(forecasts.get(2).getTemperature())+ " \u2103");
        Day4Temp.setText(new DecimalFormat("#0").format(forecasts.get(3).getTemperature())+ " \u2103");
        Day5Temp.setText(new DecimalFormat("#0").format(forecasts.get(4).getTemperature())+ " \u2103");



    }
}

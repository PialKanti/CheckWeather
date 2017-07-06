package weather.check.com.checkweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import weather.check.com.checkweather.POJO.WeatherAPIKey;
import weather.check.com.checkweather.POJO.WeatherDetails;
import weather.check.com.checkweather.POJO.WeatherForecast;
import weather.check.com.checkweather.adapter.ForecastAdapter;
import weather.check.com.checkweather.database.DatabaseHelper;
import weather.check.com.checkweather.database.DatabaseOperations;
import weather.check.com.checkweather.singleton.MySingleton;
import weather.check.com.checkweather.utills.CheckingInternetConnection;
import weather.check.com.checkweather.utills.ParseJsonData;

public class WeatherShowActivity extends AppCompatActivity {

    private LinearLayout Main_background, top_left, top_right, bottom_left, bottom_right;
    private LinearLayout WeatherContent;
    private ImageView IconImage, WindSpeedImage, HumidityImage, SunriseImage, SunsetImage;
    private TextView CityName, CountryName, WeatherDescription, Temperature;
    private TextView WindSpeedText, WindSpeedValue, HumidityText, HumidityValue, SunriseText, SunriseValue, SunsetText, SunsetValue;
    private RecyclerView ForecastShow;
    private ProgressBar progressBar;
    private WeatherDetails Wdetails;
    private WeatherAPIKey key;
    private String city;
    private ForecastAdapter forecastAdapter;
    private DatabaseHelper mHelper;
    private SQLiteDatabase db;
    private DatabaseOperations dbOperations;
    private boolean loadDataFromInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_show);


        Intent intent = getIntent();
        if (intent.hasExtra("CityName")) {
            city = intent.getStringExtra("CityName");
            loadDataFromInternet = true;

            //Writing into SharedPreferences
            SharedPreferences preferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("City", city);
            editor.commit();
            Log.d("Debug", "Writing into SharedPreferences");
        } else {
            loadDataFromInternet = false;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.transparent_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        initialize();
        WeatherContent.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        //Checking if data exits in database or not
        if (loadDataFromInternet) {
            loadWeatherData(city, true);
        } else {
            setData();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.weather_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                if (CheckingInternetConnection.isIntenetOn(getApplicationContext())) {
                    SharedPreferences preferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE);
                    String cityName = preferences.getString("City", null);
                    WeatherContent.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    loadWeatherData(cityName, false);
                } else {
                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        mHelper.close();
        super.onDestroy();
    }

    public void initialize() {
        Main_background = (LinearLayout) findViewById(R.id.MainContent);
        WeatherContent = (LinearLayout) findViewById(R.id.WeatherContent);

        CityName = (TextView) findViewById(R.id.CityName);
        CountryName = (TextView) findViewById(R.id.CountryName);
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

        ForecastShow = (RecyclerView) findViewById(R.id.rv_forecast);
        progressBar = (ProgressBar) findViewById(R.id.pb_progressBar);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 5);
        ForecastShow.setLayoutManager(layoutManager);
        forecastAdapter = new ForecastAdapter();
        ForecastShow.setAdapter(forecastAdapter);
    }

    public void setData() {
        //Getting data from Database
        mHelper = new DatabaseHelper(getApplicationContext());
        db = mHelper.getReadableDatabase();
        dbOperations = new DatabaseOperations(db);
        WeatherDetails Wdetails = dbOperations.getDataFromWeatherTable();
        ArrayList<WeatherForecast> forecasts = dbOperations.getDataFromForecastTable();

        WeatherContent.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        if (Wdetails.getIconCode().charAt(Wdetails.getIconCode().length() - 1) == 'd') {
            Main_background.setBackgroundResource(R.mipmap.day_image);
            Temperature.setShadowLayer(1.5f, 5.0f, 5.0f, Color.parseColor("#3CA6A5"));
        } else if (Wdetails.getIconCode().charAt(Wdetails.getIconCode().length() - 1) == 'n') {
            Main_background.setBackgroundResource(R.mipmap.night_image);
            Temperature.setShadowLayer(1.5f, 5.0f, 5.0f, Color.parseColor("#343B75"));
        } else {
            Main_background.setBackgroundResource(R.mipmap.day_image);
            Temperature.setShadowLayer(1.5f, 5.0f, 5.0f, Color.parseColor("#3CA6A5"));
        }

        CityName.setText(Wdetails.getCityName() + ",");
        CountryName.setText(Wdetails.getCountryName());


        WeatherDescription.setText(WordUtils.capitalize(Wdetails.getWeatherDescription()));

        //Showing Image from URL to ImageView
        Picasso.with(getApplicationContext()).load(WeatherAPIKey.ImageURL + Wdetails.getIconCode() + ".png").into(IconImage);


        Temperature.setText(new DecimalFormat("#0").format(Wdetails.getTemperatue()) + " \u2103");

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


        /*GridLayoutManager layoutManager = new GridLayoutManager(this, 5);
        ForecastShow.setLayoutManager(layoutManager);

         = new ForecastAdapter();*/
        forecastAdapter.setForecastData(forecasts);
        /*ForecastShow.setAdapter(forecastAdapter);*/
    }

    public void loadWeatherData(String city, final boolean shouldInsert) {
        key = new WeatherAPIKey();
        key.setUrl(city);
        String url = key.getUrl();
        //Setting JsonObject Request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ParseJsonData parseData = new ParseJsonData();
                Wdetails = parseData.parseMainJson(response);
                loadForecastData(Wdetails.getCityName(), shouldInsert);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Check", "Something goes Wrong on JsonRequest");
                Toast.makeText(getApplicationContext(), "City not found", Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void loadForecastData(String CityName, final boolean shouldInsert) {
        key = new WeatherAPIKey();
        key.setForecastURL(CityName);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, key.getForecastURL(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ParseJsonData jsonData = new ParseJsonData();
                ArrayList<WeatherForecast> forecasts = jsonData.parseForecastJson(response);
                Log.d("Debug", Wdetails.getSunriseTime());


                //Initializing SqliteDatabase
                mHelper = new DatabaseHelper(getApplicationContext());
                db = mHelper.getWritableDatabase();
                dbOperations = new DatabaseOperations(db);

                if (shouldInsert) {
                    //Inserting into database
                    dbOperations.insertIntoWeatherTable(Wdetails);
                    dbOperations.insertIntoForecastTable(forecasts);
                } else {
                    dbOperations.updateWeatherTable(Wdetails);
                    dbOperations.updateForecastTable(forecasts);
                }

                setData();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Check", "Something goes Wrong on ForecastJsonRequest");
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(request);
    }
}

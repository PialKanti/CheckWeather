package weather.check.com.checkweather;

import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import POJO.WeatherAPIKey;
import POJO.WeatherDetails;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import data.CheckingInternetConnection;
import data.ParseJsonData;
import singleton.MySingleton;

public class SearchActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;

    @BindView(R.id.SearchBar)
    EditText SearchBar;

    @BindView(R.id.SearchButton)
    Button SearchButton;

    private String CityName, URL;
    WeatherDetails Wdetails;
    WeatherAPIKey key = new WeatherAPIKey();


    @OnClick(R.id.SearchButton)
    /**
     * When user click on Search button this methods takes the text from searchBar and pass it to OpenWeather API
     * using JsonObject Request
     * */
    void SearchButtonOnClick() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Check", "Hide keyboard exception");
        }
        if (CheckingInternetConnection.isIntenetOn(getApplicationContext())) {
            CityName = SearchBar.getText().toString();
            SearchBar.setText("");
            if (CityName != null && !CityName.isEmpty()) {
                key.setUrl(CityName);
                URL = key.getUrl();

                //Creating RequestQueue
                RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();

                //Setting JsonObject Request
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ParseJsonData parseData = new ParseJsonData();
                        Wdetails = parseData.parseMainJson(response);

                        parseForecastDetails();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Check", "Something goes Wrong on JsonRequest");
                        Toast.makeText(getApplicationContext(), "City not found", Toast.LENGTH_SHORT).show();
                    }
                });

                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

            } else {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Enter a city name", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        } else {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No connection", Snackbar.LENGTH_SHORT);
            snackbar.setAction("Connect", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
            });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.CoordinatorLayout);

        /*SearchBar = (EditText) findViewById(R.id.SearchBar);
        SearchButton = (Button) findViewById(R.id.SearchButton);

        SearchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("Check", "Hide keyboard exception");
                }
                if (CheckingInternetConnection.isIntenetOn(getApplicationContext())) {
                    CityName = SearchBar.getText().toString();
                    SearchBar.setText("");
                    if (CityName != null && !CityName.isEmpty()) {
                        key.setUrl(CityName);
                        URL = key.getUrl();

                        //Creating RequestQueue
                        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();

                        //Setting JsonObject Request
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                ParseJsonData parseData = new ParseJsonData();
                                Wdetails = parseData.parseMainJson(response);

                                parseForecastDetails();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("Check", "Something goes Wrong on JsonRequest");
                                Toast.makeText(getApplicationContext(), "City not found", Toast.LENGTH_SHORT).show();
                            }
                        });

                        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Enter a city name", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "No connection", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("Connect", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    });
                    snackbar.setActionTextColor(Color.RED);
                    snackbar.show();
                }
            }
        });*/
    }

    public void parseForecastDetails() {
        Log.i("Check", "IN");
        key.setForecastURL(Wdetails.getCityName());
        Log.i("Check", key.getForecastURL());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, key.getForecastURL(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ParseJsonData jsonData = new ParseJsonData();
                Wdetails = jsonData.parseForecastJson(response, Wdetails);

                Intent intent = new Intent(SearchActivity.this, WeatherShowActivity.class);
                intent.putExtra("WeatherDetails", Wdetails);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Check", "Something goes Wrong on ForecastJsonRequest");
            }
        });

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}

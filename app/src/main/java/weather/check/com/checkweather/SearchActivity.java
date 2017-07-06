package weather.check.com.checkweather;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import weather.check.com.checkweather.POJO.WeatherAPIKey;
import weather.check.com.checkweather.POJO.WeatherDetails;
import weather.check.com.checkweather.utills.CheckingInternetConnection;
import weather.check.com.checkweather.utills.GooglePlaceAPI;
import weather.check.com.checkweather.utills.ParseJsonData;
import weather.check.com.checkweather.singleton.MySingleton;


public class SearchActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton locationButton;

    AutoCompleteTextView AutoCompleteSearchBar;
    Button SearchButton;
    ArrayAdapter<String> autoCompleteAdapter;

    private String CityName;
    private ArrayList<String> CityPredictionData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        grantPermissions();

        initialize();

        AutoCompleteTextWatcher();

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchButtonOnClick();
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

    }


    public void initialize() {
        AutoCompleteSearchBar = (AutoCompleteTextView) findViewById(R.id.actv_SearchBar);
        AutoCompleteSearchBar.setThreshold(2);
        SearchButton = (Button) findViewById(R.id.b_SearchButton);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add a location");
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.CoordinatorLayout);
        locationButton = (FloatingActionButton) findViewById(R.id.fb_location);

    }

    public void grantPermissions() {
        int requestCode = 1;
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE
        };

        ArrayList<String> permissiomsTogranted = new ArrayList<>();

        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                permissiomsTogranted.add(permissions[i]);
            }
        }
        String[] requestForpermisssion = permissiomsTogranted.toArray(new String[permissiomsTogranted.size()]);
        ActivityCompat.requestPermissions(this, requestForpermisssion, requestCode);
    }

    public void AutoCompleteTextWatcher() {
        AutoCompleteSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    CityPredictionData = new ArrayList<String>();
                    AutoComplete(s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void AutoComplete(String input) {
        GooglePlaceAPI api = new GooglePlaceAPI();
        String url = api.getGooglePlaceURL();
        url += input;
        Log.d("Debug", url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Debug", "OK");
                try {
                    JSONArray jsonArray = response.getJSONArray("predictions");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String description = jsonArray.getJSONObject(i).getString("description");
                        CityPredictionData.add(description);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Debug", "Error Parsing Google Places data");
                }
                autoCompleteAdapter = new ArrayAdapter<String>(
                        getApplicationContext(),
                        android.R.layout.simple_list_item_1, CityPredictionData) {

                    @Override
                    public View getView(int position,
                                        View convertView, ViewGroup parent) {
                        View view = super.getView(position,
                                convertView, parent);
                        TextView text = (TextView) view
                                .findViewById(android.R.id.text1);
                        text.setTextColor(Color.BLACK);
                        return view;
                    }
                };
                AutoCompleteSearchBar.setAdapter(autoCompleteAdapter);
                autoCompleteAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("Debug", "Error getting Volley response in Google Places Api");
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * When user click on Search button this methods takes the text from searchBar and pass it to OpenWeather API
     * using JsonObject Request
     */

    void SearchButtonOnClick() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Check", "Hide keyboard exception");
        }
        if (CheckingInternetConnection.isIntenetOn(getApplicationContext())) {
            CityName = AutoCompleteSearchBar.getText().toString();
            AutoCompleteSearchBar.setText("");
            if (CityName != null && !CityName.isEmpty()) {
                Intent intent = new Intent(SearchActivity.this, WeatherShowActivity.class);
                intent.putExtra("CityName", CityName);
                startActivity(intent);
                finish();

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

    public void getLocation() {
        LocationManager lManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        boolean netEnabled = lManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (netEnabled) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            Location lastLocation = lManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Double Lat = lastLocation.getLatitude();
            Double Long = lastLocation.getLongitude();

            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(Lat, Long, 1);
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);

                Toast.makeText(getApplicationContext(), stateName, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package weather.check.com.checkweather;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

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
import weather.check.com.checkweather.utills.LocationTracker;
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
    private FusedLocationProviderClient locationProviderClient;
    double Lat, Long;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (requestForPermission()) {
            grantPermissions();
        }

        initialize();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean(getString(R.string.pref_autocomplete_edittext), true)) {
            AutoCompleteTextWatcher(); // If checkbox id checked then autocomplete EditText will work
        }

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

    public boolean requestForPermission() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE);
        String cityName = preferences.getString("City", null);
        if (cityName != null)
            return false;
        else
            return true;
    }


    public void initialize() {
        AutoCompleteSearchBar = (AutoCompleteTextView) findViewById(R.id.actv_SearchBar);
        AutoCompleteSearchBar.setThreshold(2);
        SearchButton = (Button) findViewById(R.id.b_SearchButton);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white_font));
        getSupportActionBar().setTitle("Add location");
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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
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
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

        boolean isGPSEnabled = lManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSEnabled) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

            locationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    try {
                        if (location != null) {
                            Lat = location.getLatitude();
                            Long = location.getLongitude();
                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(Lat, Long, 1);
                            String cityName = addresses.get(0).getLocality();
                            // Setting cityname to AutoComplete TextView
                            AutoCompleteSearchBar.setText(cityName);
                            AutoCompleteSearchBar.setSelection(AutoCompleteSearchBar.length());

                            Toast.makeText(getApplicationContext(), cityName, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Wait few seconds. Then click location button", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        } else {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "LocationTracker not enabled", Snackbar.LENGTH_SHORT);
            snackbar.setAction("Enable", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }
    }
}

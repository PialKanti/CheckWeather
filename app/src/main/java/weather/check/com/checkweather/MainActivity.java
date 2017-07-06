package weather.check.com.checkweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE);
        String cityName = preferences.getString("City", null);
        if (cityName != null) {
            Intent intent = new Intent(MainActivity.this, WeatherShowActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

package weather.check.com.checkweather.utills;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Pial on 7/18/2017.
 */

public class LocationTracker {
    public static double latitude;
    public static double longitude;
    LocationManager locationManager;
    LocationResult locationResult;
    Timer timer;

    public void getLocation(Context context, LocationResult result) {
        locationResult = result;
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listenerGPS);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        timer = new Timer();
        timer.schedule(new GetLastLocation(), 20000);
    }

    LocationListener listenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            timer.cancel();
            locationResult.getLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    class GetLastLocation extends TimerTask {

        @Override
        public void run() {

        }
    }

    public static abstract class LocationResult {
        public abstract void getLocation(Location location);
    }
}

package weather.check.com.checkweather.utills;

/**
 * Created by Pial on 7/3/2017.
 */

public class GooglePlaceAPI {
    private final String TAG = "Debug";
    private final String BASE_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json";
    private final String INPUT = "&input=";
    private final String TYPES = "?types=geocode&sensor=true";
    private final String API_KEY = "AIzaSyDniWi45O4jA2ZWYN99_B6Wa-jPLPCE95g";
    private final String Key_Query = "&key=";


    public String getGooglePlaceURL(){
        return BASE_URL+TYPES+Key_Query+API_KEY+INPUT;
    }
}

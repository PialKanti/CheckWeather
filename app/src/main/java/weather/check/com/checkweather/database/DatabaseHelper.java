package weather.check.com.checkweather.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;

/**
 * Created by Pial on 7/5/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CheckWeather.db";
    private static final String CREATE_WEATHER_OF_TODAY_TABLE = "CREATE TABLE " + WeatherReaderContract.WeatherData.TABLE_NAME
            + "(" + WeatherReaderContract.WeatherData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            WeatherReaderContract.WeatherData.COLUMN_CITY + " TEXT NOT NULL," +
            WeatherReaderContract.WeatherData.COLUMN_COUNTRY + " TEXT NOT NULL," +
            WeatherReaderContract.WeatherData.COLUMN_TEMPARATURE + " INTEGER NOT NULL," +
            WeatherReaderContract.WeatherData.COLUMN_WEATHER_DESC + " TEXT NOT NULL," +
            WeatherReaderContract.WeatherData.COLUMN_WINDSPEED + " REAL NOT NULL," +
            WeatherReaderContract.WeatherData.COLUMN_HUMIDITY + " REAL NOT NULL," +
            WeatherReaderContract.WeatherData.COLUMN_SUNRISE + " INTEGER NOT NULL," +
            WeatherReaderContract.WeatherData.COLUMN_SUNSET + " INTEGER NOT NULL," +
            WeatherReaderContract.WeatherData.COLUMN_ICONCODE + " TEXT NOT NULL)";

    private static final String CREATE_WEATHER_FORECAST_TABLE = "CREATE TABLE " + WeatherReaderContract.ForecastData.TABLE_NAME + "(" +
            WeatherReaderContract.ForecastData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            WeatherReaderContract.ForecastData.COLUMN_TEMPARATURE + " INTEGER NOT NULL," +
            WeatherReaderContract.ForecastData.COLUMN_DAY + " TEXT NOT NULL," +
            WeatherReaderContract.ForecastData.COLUMN_ICONCODE + " TEXT NOT NULL)";

    private static final String DELETE_WEATHER_OF_TODAY_TABLE = "DROP TABLE IF EXITS " + WeatherReaderContract.WeatherData.TABLE_NAME;
    private static final String DELETE_WEATHER_FORECAST_TABLE = "DROP TABLE IF EXITS " + WeatherReaderContract.ForecastData.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WEATHER_OF_TODAY_TABLE);
        db.execSQL(CREATE_WEATHER_FORECAST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_WEATHER_OF_TODAY_TABLE);
        db.execSQL(DELETE_WEATHER_FORECAST_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

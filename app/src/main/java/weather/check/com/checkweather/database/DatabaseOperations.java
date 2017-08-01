package weather.check.com.checkweather.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import weather.check.com.checkweather.POJO.WeatherDetails;
import weather.check.com.checkweather.POJO.WeatherForecast;

/**
 * Created by Pial on 7/5/2017.
 */

public class DatabaseOperations {
    SQLiteDatabase db;

    public DatabaseOperations(SQLiteDatabase db) {
        this.db = db;
    }

    public boolean insertIntoWeatherTable(WeatherDetails weatherData) {

        //Creating a new map of values
        ContentValues contentValues = new ContentValues();
        contentValues.put(WeatherReaderContract.WeatherData.COLUMN_CITY, weatherData.getCityName());
        contentValues.put(WeatherReaderContract.WeatherData.COLUMN_COUNTRY, weatherData.getCountryName());
        contentValues.put(WeatherReaderContract.WeatherData.COLUMN_TEMPARATURE, weatherData.getTemperatue());
        contentValues.put(WeatherReaderContract.WeatherData.COLUMN_WEATHER_DESC, weatherData.getWeatherDescription());
        contentValues.put(WeatherReaderContract.WeatherData.COLUMN_WINDSPEED, weatherData.getWindSpeed());
        contentValues.put(WeatherReaderContract.WeatherData.COLUMN_HUMIDITY, weatherData.getHumidity());
        contentValues.put(WeatherReaderContract.WeatherData.COLUMN_SUNRISE, weatherData.getSunriseTime());
        contentValues.put(WeatherReaderContract.WeatherData.COLUMN_SUNSET, weatherData.getSunsetTime());
        contentValues.put(WeatherReaderContract.WeatherData.COLUMN_ICONCODE, weatherData.getIconCode());

        long id = db.insert(WeatherReaderContract.WeatherData.TABLE_NAME, null, contentValues);

        if (id == -1)
            return false;
        return true;
    }

    public boolean insertIntoForecastTable(ArrayList<WeatherForecast> forecasts) {
        boolean isOK = true;
        for (int i = 0; i < forecasts.size(); i++) {
            WeatherForecast forecast = forecasts.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(WeatherReaderContract.ForecastData.COLUMN_DAY, forecast.getDay());
            contentValues.put(WeatherReaderContract.ForecastData.COLUMN_TEMPARATURE, forecast.getTemperature());
            contentValues.put(WeatherReaderContract.ForecastData.COLUMN_ICONCODE, forecast.getIconCode());

            long id = db.insert(WeatherReaderContract.ForecastData.TABLE_NAME, null, contentValues);
            if (id == -1)
                isOK = false;
        }

        return isOK;
    }

    public WeatherDetails getDataFromWeatherTable() {

        WeatherDetails weatherDetails = new WeatherDetails();

        String[] columnsTOreturn = {
                WeatherReaderContract.WeatherData.COLUMN_CITY,
                WeatherReaderContract.WeatherData.COLUMN_COUNTRY,
                WeatherReaderContract.WeatherData.COLUMN_TEMPARATURE,
                WeatherReaderContract.WeatherData.COLUMN_WEATHER_DESC,
                WeatherReaderContract.WeatherData.COLUMN_WINDSPEED,
                WeatherReaderContract.WeatherData.COLUMN_HUMIDITY,
                WeatherReaderContract.WeatherData.COLUMN_SUNRISE,
                WeatherReaderContract.WeatherData.COLUMN_SUNSET,
                WeatherReaderContract.WeatherData.COLUMN_ICONCODE
        };

        Cursor cursor = db.query(
                WeatherReaderContract.WeatherData.TABLE_NAME,
                columnsTOreturn,
                null,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            weatherDetails.setCityName(cursor.getString(cursor.getColumnIndexOrThrow(WeatherReaderContract.WeatherData.COLUMN_CITY)));
            weatherDetails.setCountryName(cursor.getString(cursor.getColumnIndexOrThrow(WeatherReaderContract.WeatherData.COLUMN_COUNTRY)));
            weatherDetails.setTemperatue(cursor.getInt(cursor.getColumnIndexOrThrow(WeatherReaderContract.WeatherData.COLUMN_TEMPARATURE)));
            weatherDetails.setWeatherDescription(cursor.getString(cursor.getColumnIndexOrThrow(WeatherReaderContract.WeatherData.COLUMN_WEATHER_DESC)));
            weatherDetails.setWindSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(WeatherReaderContract.WeatherData.COLUMN_WINDSPEED)));
            weatherDetails.setHumidity(cursor.getDouble(cursor.getColumnIndexOrThrow(WeatherReaderContract.WeatherData.COLUMN_HUMIDITY)));
            weatherDetails.setSunriseTime(cursor.getString(cursor.getColumnIndexOrThrow(WeatherReaderContract.WeatherData.COLUMN_SUNRISE)));
            weatherDetails.setSunsetTime(cursor.getString(cursor.getColumnIndexOrThrow(WeatherReaderContract.WeatherData.COLUMN_SUNSET)));
            weatherDetails.setIconCode(cursor.getString(cursor.getColumnIndexOrThrow(WeatherReaderContract.WeatherData.COLUMN_ICONCODE)));
        }

        cursor.close();

        return weatherDetails;
    }

    public ArrayList<WeatherForecast> getDataFromForecastTable() {

        ArrayList<WeatherForecast> forecasts = new ArrayList<>();
        String[] columnsTOreturn = {
                WeatherReaderContract.ForecastData.COLUMN_TEMPARATURE,
                WeatherReaderContract.ForecastData.COLUMN_DAY,
                WeatherReaderContract.ForecastData.COLUMN_ICONCODE
        };

        String sortOrder = WeatherReaderContract.ForecastData._ID;

        Cursor cursor = db.query(
                WeatherReaderContract.ForecastData.TABLE_NAME,
                columnsTOreturn,
                null,
                null,
                null,
                null,
                sortOrder
        );

        while (cursor.moveToNext()) {
            WeatherForecast forecast = new WeatherForecast();
            forecast.setTemperature(cursor.getInt(cursor.getColumnIndexOrThrow(WeatherReaderContract.ForecastData.COLUMN_TEMPARATURE)));
            forecast.setDay(cursor.getString(cursor.getColumnIndexOrThrow(WeatherReaderContract.ForecastData.COLUMN_DAY)));
            forecast.setIconCode(cursor.getString(cursor.getColumnIndexOrThrow(WeatherReaderContract.ForecastData.COLUMN_ICONCODE)));

            forecasts.add(forecast);
        }

        cursor.close();

        return forecasts;
    }

    public boolean updateWeatherTable(WeatherDetails weatherData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(WeatherReaderContract.WeatherData.COLUMN_CITY, weatherData.getCityName());
        contentValues.put(WeatherReaderContract.WeatherData.COLUMN_COUNTRY, weatherData.getCountryName());
        contentValues.put(WeatherReaderContract.WeatherData.COLUMN_TEMPARATURE, weatherData.getTemperatue());
        contentValues.put(WeatherReaderContract.WeatherData.COLUMN_WEATHER_DESC, weatherData.getWeatherDescription());
        contentValues.put(WeatherReaderContract.WeatherData.COLUMN_WINDSPEED, weatherData.getWindSpeed());
        contentValues.put(WeatherReaderContract.WeatherData.COLUMN_HUMIDITY, weatherData.getHumidity());
        contentValues.put(WeatherReaderContract.WeatherData.COLUMN_SUNRISE, weatherData.getSunriseTime());
        contentValues.put(WeatherReaderContract.WeatherData.COLUMN_SUNSET, weatherData.getSunsetTime());
        contentValues.put(WeatherReaderContract.WeatherData.COLUMN_ICONCODE, weatherData.getIconCode());

        String whereClause = WeatherReaderContract.WeatherData._ID + " LIKE ?";
        String[] whereArgs = {String.valueOf(1)};

        int count = db.update(
                WeatherReaderContract.WeatherData.TABLE_NAME,
                contentValues,
                whereClause,
                whereArgs
        );

        if (count > 0)
            return true;
        else
            return false;
    }

    public boolean updateForecastTable(ArrayList<WeatherForecast> forecasts) {

        boolean isOK = true;
        String whereClause = WeatherReaderContract.ForecastData._ID + " LIKE ?";

        for (int i = 0; i < forecasts.size(); i++) {
            WeatherForecast forecast = forecasts.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(WeatherReaderContract.ForecastData.COLUMN_DAY, forecast.getDay());
            contentValues.put(WeatherReaderContract.ForecastData.COLUMN_TEMPARATURE, forecast.getTemperature());
            contentValues.put(WeatherReaderContract.ForecastData.COLUMN_ICONCODE, forecast.getIconCode());

            int count = db.update(
                    WeatherReaderContract.ForecastData.TABLE_NAME,
                    contentValues,
                    whereClause,
                    new String[]{String.valueOf(i + 1)}
            );
            if (count <= 0)
                isOK = false;
        }

        return isOK;
    }

    public int getWeatherTableSize() {
        String query = "Select * from " + WeatherReaderContract.WeatherData.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        int size = cursor.getCount();
        cursor.close();
        return size;
    }

    public int getForecastTableSize() {
        String query = "Select * from " + WeatherReaderContract.ForecastData.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        int size = cursor.getCount();
        cursor.close();
        return size;
    }

    public void deleteWeatherTable() {
        db.delete(WeatherReaderContract.WeatherData.TABLE_NAME, null, null);
    }

    public void deleteForecastTable() {
        db.delete(WeatherReaderContract.ForecastData.TABLE_NAME, null, null);
    }
}

package weather.check.com.checkweather.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import weather.check.com.checkweather.POJO.WeatherAPIKey;
import weather.check.com.checkweather.POJO.WeatherForecast;
import weather.check.com.checkweather.R;
import weather.check.com.checkweather.utills.Conversion;

/**
 * Created by Pial on 7/3/2017.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private ArrayList<WeatherForecast> ForecastData;

    public void setForecastData(ArrayList<WeatherForecast> forecastData) {
        ForecastData = forecastData;
        notifyDataSetChanged();
    }

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.forecast_weather_item_row, parent, false);

        ForecastAdapterViewHolder viewHolder = new ForecastAdapterViewHolder(view, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (ForecastData == null)
            return 0;
        else
            return ForecastData.size();
    }

    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder {
        public ImageView ForecastImage;
        public TextView DayofMonth;
        public TextView Temperature;
        Context context;

        public ForecastAdapterViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            ForecastImage = (ImageView) itemView.findViewById(R.id.iv_forecastImage);
            DayofMonth = (TextView) itemView.findViewById(R.id.tv_forecastDay);
            Temperature = (TextView) itemView.findViewById(R.id.tv_forecastTemp);
        }

        void bind(int position) {
            WeatherForecast forecast = ForecastData.get(position);
            Picasso.with(context).load(WeatherAPIKey.ImageURL + forecast.getIconCode() + ".png").into(ForecastImage);
            DayofMonth.setText(forecast.getDay());

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String tempUnit = preferences.getString(context.getString(R.string.pref_temp_unit_key), context.getString(R.string.celsius_value_name));
            if (tempUnit.equals(context.getString(R.string.celsius_value_name)))
                Temperature.setText(new DecimalFormat("#0").format(Conversion.KelvinToCelsius(forecast.getTemperature())) + "\u2103");
            else if (tempUnit.equals(context.getString(R.string.fahrenheit_value_name)))
                Temperature.setText(new DecimalFormat("#0").format(Conversion.KelvinToFahrenheit(forecast.getTemperature())) + "\u2109");
        }
    }

}

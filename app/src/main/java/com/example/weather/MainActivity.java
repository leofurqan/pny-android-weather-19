package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.weather.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String url = "http://api.weatherapi.com/v1/forecast.json?q=lahore&key=abb0369475c54aeb8be120754230907";
    WeatherData weather;
    ArrayList<HourlyData> hourlyData;
    HourlyAdapter adapter;

    Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        weather = new WeatherData();
        hourlyData = new ArrayList<>();
        helper = new Helper(this);
        adapter = new HourlyAdapter(this, hourlyData);
        binding.rvHourly.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvHourly.setAdapter(adapter);
        loadWeatherData();
    }

    private void loadWeatherData() {
        helper.showProgressDialog("Loading", "Loading Weather Data...");
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("r_weather", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject locationObject = jsonObject.getJSONObject("location");
                    JSONObject currentObject = jsonObject.getJSONObject("current");
                    JSONObject conditionObject = currentObject.getJSONObject("condition");
                    JSONObject forecastObject = jsonObject.getJSONObject("forecast");
                    JSONArray forecastArray = forecastObject.getJSONArray("forecastday");
                    JSONObject forecastDayObject = forecastArray.getJSONObject(0);
                    JSONArray hourArray = forecastDayObject.getJSONArray("hour");

                    weather.setName(locationObject.getString("name"));
                    weather.setRegion(locationObject.getString("region"));
                    weather.setCountry(locationObject.getString("country"));
                    weather.setLocaltime(locationObject.getString("localtime"));

                    weather.setTemp_c(currentObject.getString("temp_c"));
                    weather.setCondition(conditionObject.getString("text"));
                    weather.setIcon("https://" + conditionObject.getString("icon"));

                    weather.setWind(currentObject.getString("wind_mph") + " m/h");
                    weather.setHumidity(currentObject.getString("humidity"));
                    weather.setPrecipitation(currentObject.getString("precip_mm") + " mm");

                    for(int i = 0; i < hourArray.length(); i++) {
                        HourlyData data = new HourlyData();
                        JSONObject hourObject = hourArray.getJSONObject(i);
                        String[] time = hourObject.getString("time").split(" ");
                        data.setTime(time[1]);
                        data.setIcon("https://" + hourObject.getJSONObject("condition").getString("icon"));
                        data.setTemp(hourObject.getString("temp_c"));

                        hourlyData.add(data);
                    }

                    showWeatherData();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("e_weather", error.toString());
            }
        });

        Volley.newRequestQueue(this).add(request);
    }

    private void showWeatherData() {
        binding.txtCity.setText(weather.getName() + ", " + weather.getRegion() + ", " + weather.getCountry());
        binding.txtDate.setText(weather.getLocaltime());
        binding.txtCurrentTemp.setText(weather.getTemp_c());
        binding.txtCurrentCondition.setText(weather.getCondition());
        binding.txtWind.setText(weather.getWind());
        binding.txtHumidity.setText(weather.getHumidity());
        binding.txtRain.setText(weather.getPrecipitation());

        adapter.notifyDataSetChanged();

        Glide.with(this).load(weather.getIcon()).into(binding.imgCondition);

        helper.dismissProgressDialog();
    }
}
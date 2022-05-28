package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class WeatherReportMainScreen extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText etCityName;
    ProgressDialog progressDialog;
    private MaterialButton btnShowReport;
    private TextView tvWeatherReport;

    private final String appid = "5b34c288d82869579f2ee0de90e43141";
    private final String url = "https://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}";

    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_report_main_screen);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Weather Report");
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        etCityName = findViewById(R.id.etCityName);
        btnShowReport = findViewById(R.id.btnShowReport);
        tvWeatherReport = findViewById(R.id.tvWeatherReport);

        btnShowReport.setEnabled(true);
        btnShowReport.setOnClickListener(this);

    }

    public void getWeatherDetails(View view) {
        progressDialog.show();
        String cityName = etCityName.getText().toString().toLowerCase().trim();
        String tempUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + appid;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                etCityName.setText("");
                String outPut = "";
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");

                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                    float pressure = jsonObjectMain.getInt("pressure");
                    int humidity = jsonObjectMain.getInt("humidity");

                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    String wind = jsonObjectWind.getString("speed");

                    JSONObject jsonObjectCloud = jsonResponse.getJSONObject("clouds");
                    String clouds = jsonObjectCloud.getString("all");

                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    String countryName = jsonObjectSys.getString("country");
                    String cityName = jsonResponse.getString("name");

                    //tvWeatherReport.setTextColor(Color.rgb(68, 134, 199));
                    tvWeatherReport.setTextColor(Color.rgb(0, 0, 0));
                    outPut += "Current weather of " + cityName + " (" + countryName + ")"
                            + "\n Temp: " + df.format(temp) + " *C"
                            + "\n Feels Like: " + df.format(feelsLike) + " *C"
                            + "\n Humidity: " + humidity + "%"
                            + "\n Description: " + description
                            + "\n Wind Speed: " + wind + "m/s (meters per second)"
                            + "\n Cloudiness: " + clouds + "%"
                            + "\n Pressure: " + pressure + " hPa";

                    tvWeatherReport.setText(outPut);
                    progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Enter city name correctly", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnShowReport:
                getWeatherDetails(view);
                break;
        }
    }
}
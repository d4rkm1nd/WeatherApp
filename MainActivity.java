package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView cityName;
    Button searchButton;
    TextView result;

    class Weather extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... adress) {

            try {
                URL url = new URL(adress[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int data = isr.read();
                String content = "";
                char ch;
                while (data != -1){
                    ch = (char) data;
                    content = content + ch;
                    data = isr.read();
                }

                return content;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    public void search (View view){
        cityName = findViewById(R.id.cityName);
        searchButton = findViewById(R.id.searchButton);
        result = findViewById(R.id.result);


        String cName = cityName.getText().toString();

        String content;
        Weather weather = new Weather();
        try {
            content = weather.execute("https://openweathermap.org/data/2.5/weather?q=" + cName + "&appid=439d4b804bc8187953eb36d2a8c26a02").get();

            JSONObject jsonObject = new JSONObject(content);
            String weatherData = jsonObject.getString("weather");
            String mainTemperature = jsonObject.getString("main");

            Log.w("weatherdata",weatherData);
            JSONArray array = new JSONArray(weatherData);

            String main ="";
            String description ="";
            String temperature ="";

            for (int i = 0; i<=(array.length()-1); i++){
                JSONObject weatherPart = array.getJSONObject(i);
                main = weatherPart.getString("main");
                description = weatherPart.getString("description");
            }

            JSONObject mainPart  = new JSONObject(mainTemperature);
            temperature = mainPart.getString("temp");

            Log.w("main", main);
            Log.w("description", description);

            result.setText("Main : "+ main + "\nDescription : "+ description + "\nTemperature : " + temperature);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}

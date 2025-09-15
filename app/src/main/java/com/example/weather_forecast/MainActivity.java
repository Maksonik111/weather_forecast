package com.example.weather_forecast;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView display;
    TextView display2;

    Button main_button;
    EditText enter_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        display = findViewById(R.id.display);
        display2 = findViewById(R.id.display2);
        main_button = findViewById(R.id.main_button);
        enter_city = findViewById(R.id.enter_city);

        main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(enter_city.getText().toString().trim().isEmpty()){
                    Toast.makeText(MainActivity.this, R.string.warning_text, Toast.LENGTH_LONG).show();
                }
                else{
                    String city = enter_city.getText().toString();
                    String key = "fc20c4f7067949bb6081669a9d0e917c";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=metric&lang=ru";

                    new GetURLData().execute(url);
                }
            }
        });
    }

    private class GetURLData extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            display.setText("Обработка...");

        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");

                }
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return null;

        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result == null){
                display.setText("Нет данных!");
                display2.setText("Возможно ошибка в городе!");
                Toast.makeText(MainActivity.this,"Ошибка соединения", Toast.LENGTH_LONG).show();

                return;
            }

            try {
                JSONObject obj = new JSONObject(result);

                display.setText("Температура:" + obj.getJSONObject("main").getDouble("temp"));

                JSONArray weatherArray = obj.getJSONArray("weather");
                if(weatherArray.length() > 0 ){
                    JSONObject firstWeather = weatherArray.getJSONObject(0);
                    String description = firstWeather.getString("description");
                    display2.setText(description);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
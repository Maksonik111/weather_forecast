package com.example.weather_forecast;

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

public class MainActivity extends AppCompatActivity {

    TextView display;
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

                    new GetURLData().execute();
                }
            }
        });
    }

    private class GetURLData extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            return "";
        }
    }
}
package com.example.admin.myweatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText city;
    TextView result;
    TextView temperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        city = (EditText) findViewById(R.id.city);
        result = (TextView) findViewById(R.id.result);
        temperature = (TextView) findViewById(R.id.temperature);

        final String baseUrl = "http://api.openweathermap.org/data/2.5/weather?q=";

        //Paste your API key inside the quotes in the lastUrl variable given below

        final String lastUrl = "";

        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city.setText(null);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = city.getText().toString().trim();
                Log.i("city",cityName);
                if(cityName.matches("Enter your city")||cityName.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter a city name", Toast.LENGTH_SHORT).show();

                }else{

                    String myUrl = baseUrl + city.getText().toString() + lastUrl;
                    Log.i("Url",myUrl);

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myUrl, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String weath = response.getString("weather");
                                        JSONArray ar = new JSONArray(weath);
                                        result.setText(ar.getJSONObject(0).getString("main"));

                                        String main = response.getString("main");
                                        JSONObject ob = new JSONObject(main);
                                        double temp = ob.getDouble("temp");
                                        temperature.setText(String.format("%.2f Celsius",temp-273.15));


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("Error",error.toString());
                                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                    MySingleton.getInstance(MainActivity.this).addToRequestQue(jsonObjectRequest);
                }

            }
        });

    }
}

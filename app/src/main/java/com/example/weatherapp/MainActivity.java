package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    String weather="", description="";
    TextView textView;
    EditText editText;
    String city;

    String endResult="";

    public void go(View View){

        try {

            city = editText.getText().toString();
            String encodedCity = URLEncoder.encode(city, "UTF-8");
            DownloadTask task = new DownloadTask();
            task.execute("https://api.openweathermap.org/data/2.5/weather?q=" + encodedCity +"&appid=4e6424586753582e76c8e00d37e53ba0");

            InputMethodManager mgr= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE) ;
            mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);

//            endResult= weather+" : "+description+ "\r\n";
//
//            textView.setText(endResult);

        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Couldn't find the Weather :( ", Toast.LENGTH_SHORT).show();

        }


    }

    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {

                String result="";

                URL url=new URL(urls[0]);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(inputStream);

                int data= reader.read();
                while(data!=-1){
                    char current= (char)data;
                    result+= current;
                    data= reader.read();
                }
                return result;

            }catch(Exception e){
                e.printStackTrace();
               // Toast.makeText(getApplicationContext(), "Couldn't find the Weather :( ", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                String weatherInfo= jsonObject.getString("weather");

                JSONArray arr=new JSONArray(weatherInfo);

                for(int i=0; i<arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);

                    weather = jsonPart.getString("main");
                    description = jsonPart.getString("description");

                    if(!weather.isEmpty() && !description.isEmpty()){

                        endResult = weather +" : "+ description +"\r\n";

                    }

                }
                if(!endResult.isEmpty()){
                    textView.setText(endResult);
                }else{
                    Toast.makeText(getApplicationContext(), "couldn't find the weather :(", Toast.LENGTH_SHORT).show();
                }


            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Couldn't find the Weather :( ", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView= findViewById(R.id.textView2);
        editText= (EditText) findViewById(R.id.cityName);

    }
}
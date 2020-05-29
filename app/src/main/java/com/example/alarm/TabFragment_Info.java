package com.example.alarm;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class TabFragment_Info extends Fragment {
    private LocationRequest mLocationRequest;
    private Location location = new Location("Weather");
    private String temperature = "";
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private static final String WEATHER_API_KEY = "5412590d46f72f52ec77c78bfe8951f1";
    private static final String NEWS_API_KEY = "e2093652e1aa43088baa69468713b523";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }
    public void checkPermission(Context context){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){//Can add more as per requirement

            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }
    public void getLastLocation(Context context) {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(context);

        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location _location) {
                        // GPS location can be null if GPS is switched off
                        if (_location != null) {
                            Log.d("", "LOCATION");
                            Log.d("", _location.toString());
                            location = _location;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = Objects.requireNonNull(getActivity());
        checkPermission(context);
        Log.d("", "START");
        getLastLocation(context);

        View view = inflater.inflate(R.layout.tab_fragment_info, null);
        // Header Button
        Button headerButton = (Button) view.findViewById(R.id.header_button);
        setHeaderButtonListener(headerButton);
        // Current Date to display
        TextView infoDate = (TextView) view.findViewById((R.id.info_date));
        setCurrentDate(infoDate);
        // Current Time to display
        TextView infoTime = (TextView) view.findViewById((R.id.info_time));
        setCurrentTime(infoTime);
        // Get Location & Get Weather using REST API
        TextView infoWeatherTemp = (TextView) view.findViewById((R.id.info_weathertempur));
        AsyncHttpClient client = new AsyncHttpClient();
        setWeather(client, location.getLatitude(), location.getLongitude(), infoWeatherTemp);
        // Current News to display

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        LinearLayout infoNews = (LinearLayout) view.findViewById(R.id.info_news);
        TextView infoNewsTitle = (TextView) view.findViewById((R.id.info_newstitle));
        TextView infoNewsBody = (TextView) view.findViewById((R.id.info_newsbody));
        AppCompatImageView infoNewsImg = (AppCompatImageView) view.findViewById((R.id.info_newsimg));
        setNews(client, infoNews, infoNewsTitle, infoNewsBody, infoNewsImg);
        return view;
    }

    private void setNews(AsyncHttpClient client, final LinearLayout infoNews, final TextView infoNewsTitle, final TextView infoNewsBody, final AppCompatImageView infoNewsImg){

        client.get(generateNewsUrl(), new JsonHttpResponseHandler() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                String _title = "", _body = "", imgSrc = "";
                try {
                    // Get Json Array
                    JSONArray main = response.getJSONArray("articles");
                    // Image Set
                    URL url = new URL(main.getJSONObject(0).getString("urlToImage"));
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    infoNewsImg.setImageBitmap(bmp);
                    // Description Set
                    infoNewsBody.setText(main.getJSONObject(0).getString("description").substring(0, 15)+"...");
                    // Title Set
                    infoNewsTitle.setText(main.getJSONObject(0).getString("title").substring(0, 10)+"...");
                    class NewsOnClickListener implements View.OnClickListener
                    {

                        private String url;
                        public NewsOnClickListener(String _url) {
                            this.url = _url;
                        }

                        @Override
                        public void onClick(View v)
                        {
                            Intent browserIntent = null;
                            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.url));
                            startActivity(browserIntent);
                        }

                    };
                    infoNews.setOnClickListener(new NewsOnClickListener(main.getJSONObject(0).getString("url")));
                    } catch (JSONException | MalformedURLException e) {
                    Log.d("", "ERROR");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void setWeather(AsyncHttpClient client, Double latitude, Double longitude, final TextView infoWeatherTemp){
        client.get(generateWeatherUrl(latitude, longitude), new JsonHttpResponseHandler() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                JSONObject main = null;
                String _temperature = "";
                try {
                    main = response.getJSONObject("main");
                    _temperature = main.getString("temp");
                } catch (JSONException e) {
                    Log.d("", "ERROR");
                }
                Log.d("", _temperature);
                temperature = _temperature;
                infoWeatherTemp.setText(temperature.substring(0, 4)+"ºc");
            }

        });
    }
    private String generateWeatherUrl(Double latitude, Double longitude) {
        return "http://api.openweathermap.org/data/2.5/weather?lat="+latitude+
                "&lon="+longitude+"&APPID="+WEATHER_API_KEY+"&units=metric";
    }
    private String generateNewsUrl() {
        return "http://newsapi.org/v2/top-headlines?country=kr&apiKey="+NEWS_API_KEY;
    }

    private void setHeaderButtonListener(Button headerButton) {
        View.OnClickListener HeaderButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MyApp","I am here");
            }
        };
        headerButton.setOnClickListener(HeaderButtonListener);
    }
    private void setCurrentTime(TextView infoTime){
        infoTime.setText(getReminingTime());
    }
    private void setCurrentDate(TextView infoDate){
        infoDate.setText(getReminingDate());
        Log.d("", getReminingDate());
    }
    // get formatted Date
    private String getReminingDate() {
        String delegate = "MMM dd";
        return (String) DateFormat.format(delegate,Calendar.getInstance());
    }
    // get formatted Time
    private String getReminingTime() {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate,Calendar.getInstance());
    }
}

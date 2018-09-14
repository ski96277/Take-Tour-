package com.example.imransk.bitmproject.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imransk.bitmproject.Activity.Add_ForeCast_Adapter;
import com.example.imransk.bitmproject.Api.Weather_Service;
import com.example.imransk.bitmproject.ModelClass.ForeCast_Weather_Response;
import com.example.imransk.bitmproject.ModelClass.Weather_Response;
import com.example.imransk.bitmproject.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Current_Weather_F extends Fragment {

    String TAG = "current weather";

    TextView current_Temp_TV;
    TextView max_Temp_TV;
    TextView min_Temp_TV;
    ImageView icon_image_View;
    Button current_Btn;
    Button forecast_Btn;
    TextView address_TV;

    ListView forecast_Weather_listView;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Geocoder geocoder;
    private List<Address> addresses = null;
    String cityName = "";
    String countryName = "";

    public static String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    public static String BASE_URL_FORECAST = "https://samples.openweathermap.org/data/2.5/";
    public double latitude;
    private double longitude;
    private String units = "metric";
    private String units2 = "Imperial";

    String weather_app_id = "ab1f919d021e76086bbdf2761777438d";

    String stringUrl;

    View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Weather");

        return inflater.inflate(R.layout.current_weather_f, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;
        initialize();
        getMyLocation_And_Geocoder_Address();


        current_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //set the menu action Visible
                setHasOptionsMenu(false);

                forecast_Weather_listView.setVisibility(View.GONE);

                icon_image_View.setVisibility(View.VISIBLE);
                current_Temp_TV.setVisibility(View.VISIBLE);
                max_Temp_TV.setVisibility(View.VISIBLE);
                min_Temp_TV.setVisibility(View.VISIBLE);
                address_TV.setVisibility(View.VISIBLE);
            }
        });
        forecast_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //set the menu action Visible
                setHasOptionsMenu(true);

                forecast_Weather_listView.setVisibility(View.VISIBLE);

                getMyLocation_And_Geocoder_Address_Forecast();

                icon_image_View.setVisibility(View.GONE);
                current_Temp_TV.setVisibility(View.GONE);
                max_Temp_TV.setVisibility(View.GONE);
                min_Temp_TV.setVisibility(View.GONE);
                address_TV.setVisibility(View.GONE);
            }
        });

    }//end OnviewCreated Method

    private void getMyLocation_And_Geocoder_Address() {


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)//After 10 second
                .setFastestInterval(500); //processing time

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                for (Location location : locationResult.getLocations()) {

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

//get complete weather url
                    stringUrl = String.format("weather?lat=%f&lon=%f&units=%s&appid=%s", latitude, longitude, units, weather_app_id);

                    /*Log.e("string url = =  = ", "onViewCreated: " + stringUrl);*/
//call the weather information method
                    get_weather_Information();

//get city name and country name

                    try {
                        /*Log.e(TAG, "onLocationResult: " + latitude + "," + longitude);*/
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        cityName = addresses.get(0).getAdminArea();
                        countryName = addresses.get(0).getCountryName();

                        address_TV.setText(countryName + "\n" + cityName);

                    } catch (IOException e) {
                        Toast.makeText(getContext(), "failed geocoder", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
            }
        };

//alow permission after marshmallow
        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            return;
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
// End Latitude & Longitude
    }

    private void getMyLocation_And_Geocoder_Address_Forecast() {


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)//After 10 second
                .setFastestInterval(500); //processing time

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                for (Location location : locationResult.getLocations()) {

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

//get complete weather url
                    stringUrl = String.format("forecast/daily?lat=%f&lon=%f&cnt=10&appid=%s", latitude, longitude, weather_app_id);

                    /*Log.e("string url ForeCast = ", " ForeCast: " + stringUrl);*/
//call the weather information method
                    get_weather_Information_ForeCast();

//get city name and country name

                    try {
                        /*Log.e(TAG, "onLocationResult: " + latitude + "," + longitude);*/
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        cityName = addresses.get(0).getAdminArea();
                        countryName = addresses.get(0).getCountryName();

                        address_TV.setText(countryName + "\n" + cityName);

                    } catch (IOException e) {
                        Toast.makeText(getContext(), "failed geocoder", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
            }
        };

//alow permission after marshmallow
        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            return;
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
// End Latitude & Longitude
    }

    private void initialize() {
        current_Btn = view.findViewById(R.id.current_weather_btn);
        forecast_Btn = view.findViewById(R.id.forecast_weather_btn);

        icon_image_View = view.findViewById(R.id.temp_icon_image_View);
        current_Temp_TV = view.findViewById(R.id.current_tempture_TV);
        max_Temp_TV = view.findViewById(R.id.current_tempture_Max_TV);
        min_Temp_TV = view.findViewById(R.id.current_tempture_MIN_TV);
        forecast_Weather_listView = view.findViewById(R.id.forecast_weather_list);
        address_TV = view.findViewById(R.id.address_ID_TV);
//initial geocoder
        geocoder = new Geocoder(getContext());
//get Latitude & Longitude

    }

    public void get_weather_Information() {


//call retrofit and save value to textView
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Weather_Service weather_service = retrofit.create(Weather_Service.class);
        final Call<Weather_Response> weather_responseCall = weather_service.getAllWeather(stringUrl);

        weather_responseCall.enqueue(new Callback<Weather_Response>() {
            @Override
            public void onResponse(Call<Weather_Response> call, Response<Weather_Response> response) {

                if (response.code() == 200) {
                    Weather_Response weather_response = response.body();


                    /*Log.e("Temp  - - - -", "onResponse: " + weather_response.getMain().getTemp());*/
                    current_Temp_TV.setText("Temp : " + weather_response.getMain().getTemp() + "° C");
                    max_Temp_TV.setText("Temp Max : " + weather_response.getMain().getTempMax() + "° C");
                    min_Temp_TV.setText("Temp Min : " + weather_response.getMain().getTempMin() + "° C");
                    String imageUrl = "https://openweathermap.org/img/w/" + weather_response.getWeather().get(0).getIcon() + ".png";
                    Picasso.get().load(imageUrl).into(icon_image_View);
                }
            }

            @Override
            public void onFailure(Call<Weather_Response> call, Throwable t) {

            }
        });

// End Call retrofit and save value to textView
    }

    public void get_weather_Information_ForeCast() {


//call retrofit and save value to textView
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_FORECAST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Weather_Service weather_service = retrofit.create(Weather_Service.class);
        final Call<ForeCast_Weather_Response> weather_responseCall = weather_service.getAllWeather_foreCast(stringUrl);

        weather_responseCall.enqueue(new Callback<ForeCast_Weather_Response>() {
            @Override
            public void onResponse(Call<ForeCast_Weather_Response> call, Response<ForeCast_Weather_Response> response) {
                if (response.code() == 200) {
                    ForeCast_Weather_Response example = response.body();

                    if (getContext() != null) {

                        Add_ForeCast_Adapter foreCast_adapter = new Add_ForeCast_Adapter(getActivity(), example.getList(), example.getCity());
                        forecast_Weather_listView.setAdapter(foreCast_adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<ForeCast_Weather_Response> call, Throwable t) {

            }
        });

// End Call retrofit and save value to textView
    }

    //set the menu Item Visible
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(true);
    }

//set the menu Item Action

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_search) {
            searchLocation();
        }
        return super.onOptionsItemSelected(item);
    }

    private void searchLocation() {
        Toast.makeText(getContext(), "search the location", Toast.LENGTH_SHORT).show();
    }

}

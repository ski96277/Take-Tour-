package com.example.imransk.bitmproject.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imransk.bitmproject.Adapter.Add_ForeCast_Adapter;
import com.example.imransk.bitmproject.Api.Weather_Service;
import com.example.imransk.bitmproject.ModelClass.ForeCast_Weather_Response;
import com.example.imransk.bitmproject.ModelClass.Weather_Response;
import com.example.imransk.bitmproject.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;


public class Current_Weather_F extends Fragment implements View.OnClickListener {

    String TAG = "current weather";

    TextView current_Temp_TV;
    TextView max_Temp_TV;
    TextView min_Temp_TV;
    ImageView icon_image_View;
    Button current_Btn;
    Button forecast_Btn;
    TextView address_TV;
    ProgressBar progressBar;

    ListView forecast_Weather_listView;
    ListView forecast_Weather_listView_search_place;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Geocoder geocoder;
    private List<Address> addresses = null;
    String cityName = "";
    String countryName = "";

    public static String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    public static String BASE_URL_FORECAST = "https://api.openweathermap.org/data/2.5/";
    public double latitude;
    private double longitude;
    private String units = "metric";
    private String units2 = "Imperial";

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    String weather_app_id = "d0b941339d6f075686460c7fe0912041";

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

        current_Btn.setOnClickListener(this);
        forecast_Btn.setOnClickListener(this);
    }//end OnviewCreated Method

    // Location and make url for current weather
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

    //get Current weather tem in text view
    public void get_weather_Information() {


//call retrofit and save value to textView
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Weather_Service weather_service = retrofit.create(Weather_Service.class);
        final Call<Weather_Response> weather_responseCall = weather_service.getAllWeather(stringUrl);
        Log.e("string url = =  = ", "onViewCreated: " + stringUrl);
        weather_responseCall.enqueue(new Callback<Weather_Response>() {
            @Override
            public void onResponse(Call<Weather_Response> call, Response<Weather_Response> response) {

                if (response.code() == 200) {
                    Weather_Response weather_response = response.body();


                    /*Log.e("Temp  - - - -", "onResponse: " + weather_response.getMain().getTemp());*/
                    current_Temp_TV.setText("Temp : " + weather_response.getMain().getTemp() + "° C");
                    max_Temp_TV.setText("Temp Max : " + weather_response.getMain().getTempMax() + "° C");
                    min_Temp_TV.setText("Temp Min : " + weather_response.getMain().getTempMin() + "° C");
                    progressBar.setVisibility(View.GONE);
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

    //get Location and make 16 days forcast weather
    private void getMyLocation_And_Geocoder_Address_Forecast() {


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)//After 10 second
                .setFastestInterval(500); //processing time

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                for (Location location : locationResult.getLocations()) {

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

//get complete weather url
                    stringUrl = String.format("forecast/daily?lat=%f&lon=%f&cnt=16&appid=%s", latitude, longitude, weather_app_id);

                    Log.e("string url ForeCast = ", " ForeCast: " + stringUrl);
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
        forecast_Weather_listView_search_place = view.findViewById(R.id.forecast_weather_list_from_search_place);
        address_TV = view.findViewById(R.id.address_ID_TV);
        progressBar = view.findViewById(R.id.progress_bar_ID);
//initial geocoder
        geocoder = new Geocoder(getContext());
//get Latitude & Longitude

    }


    //    get current weather 16 days forcast
    public void get_weather_Information_ForeCast() {
progressBar.setVisibility(View.VISIBLE);

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
                    ForeCast_Weather_Response foreCastWeatherResponse = response.body();

                    if (getContext() != null) {

                        Add_ForeCast_Adapter foreCast_adapter = new Add_ForeCast_Adapter(getActivity(), foreCastWeatherResponse.getList(), foreCastWeatherResponse.getCity());
                        forecast_Weather_listView.setAdapter(foreCast_adapter);
                        progressBar.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void onFailure(Call<ForeCast_Weather_Response> call, Throwable t) {

            }
        });

// End Call retrofit and save value to textView
    }

    //get search place weather Forcast
    public void get_weather_Information_ForeCast_search_Place(String stringUrl) {


//call retrofit and save value to textView
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_FORECAST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Weather_Service weather_service = retrofit.create(Weather_Service.class);
        final Call<ForeCast_Weather_Response> weather_responseCall = weather_service.getAllWeather_foreCast(stringUrl);

        Log.e(TAG, "get_weather_Information_ForeCast_search_Place: " + stringUrl);

        weather_responseCall.enqueue(new Callback<ForeCast_Weather_Response>() {
            @Override
            public void onResponse(Call<ForeCast_Weather_Response> call, Response<ForeCast_Weather_Response> response) {
                if (response.code() == 200) {
                    ForeCast_Weather_Response foreCastWeatherResponse = response.body();

                    if (getContext() != null) {

                        Add_ForeCast_Adapter foreCast_adapter = new Add_ForeCast_Adapter(getActivity(), foreCastWeatherResponse.getList(), foreCastWeatherResponse.getCity());
                        forecast_Weather_listView.setVisibility(View.GONE);
                        forecast_Weather_listView_search_place.setAdapter(foreCast_adapter);
                        progressBar.setVisibility(View.GONE);
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
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult: ok******");
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                forecast_Weather_listView_search_place.setVisibility(View.VISIBLE);

                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;

//get complete weather url
                stringUrl = String.format("forecast/daily?lat=%f&lon=%f&cnt=16&appid=%s", latitude, longitude, weather_app_id);

                Log.e(TAG, "onActivityResult: " + stringUrl);

                get_weather_Information_ForeCast_search_Place(stringUrl);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.current_weather_btn:

                progressBar.setVisibility(View.VISIBLE);
                //set the menu action Visible
                setHasOptionsMenu(false);
                forecast_Weather_listView.setVisibility(View.GONE);
                forecast_Weather_listView_search_place.setVisibility(View.GONE);

                icon_image_View.setVisibility(View.VISIBLE);
                current_Temp_TV.setVisibility(View.VISIBLE);
                max_Temp_TV.setVisibility(View.VISIBLE);
                min_Temp_TV.setVisibility(View.VISIBLE);
                address_TV.setVisibility(View.VISIBLE);

                break;

            case R.id.forecast_weather_btn:
                progressBar.setVisibility(View.VISIBLE);

                //set the menu action Visible
                setHasOptionsMenu(true);

                getMyLocation_And_Geocoder_Address_Forecast();

                icon_image_View.setVisibility(View.GONE);
                current_Temp_TV.setVisibility(View.GONE);
                max_Temp_TV.setVisibility(View.GONE);
                min_Temp_TV.setVisibility(View.GONE);
                address_TV.setVisibility(View.GONE);

                forecast_Weather_listView_search_place.setVisibility(View.GONE);

                forecast_Weather_listView.setVisibility(View.VISIBLE);
                break;
        }
    }
}

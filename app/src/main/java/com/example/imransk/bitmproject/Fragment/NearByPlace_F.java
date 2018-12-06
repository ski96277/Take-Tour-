package com.example.imransk.bitmproject.Fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.imransk.bitmproject.Api.MapService;
import com.example.imransk.bitmproject.NearByPlace_Pojo_Class.Example;
import com.example.imransk.bitmproject.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NearByPlace_F extends Fragment implements OnMapReadyCallback, BottomNavigationView.OnNavigationItemSelectedListener {

    GoogleMap map;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    Geocoder geocoder;
    List<Address> addresses;

    //get LikelyhoodPlace Place
    GeoDataClient geoDataClient;
    PlaceDetectionClient placeDetectionClient;


    private int PROXIMITY_RADIUS = 10000;

    String cityName;
    String countryName;
    double lat_my;
    double lng_my;
    LatLng latLng_my = null;

    String TAG = "NearByPlace_F";
    BottomNavigationView bottomNavigationView;
    ProgressBar progressBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.near_by_place_f, null);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomNavigationView = view.findViewById(R.id.bottom_nav_ID);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        progressBar = view.findViewById(R.id.progressbar_Map_id);

        geoDataClient = Places.getGeoDataClient(getActivity());
        placeDetectionClient = Places.getPlaceDetectionClient(getActivity());

        //Load map on FramLayout
        GoogleMapOptions options = new GoogleMapOptions();
        options.zoomControlsEnabled(true);
        options.mapType(GoogleMap.MAP_TYPE_NORMAL);

        SupportMapFragment mapFragment = SupportMapFragment.newInstance(options);

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.map_container, mapFragment);
        fragmentTransaction.commit();
//map load in background
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getContext(), "load map", Toast.LENGTH_SHORT).show();
        this.map = googleMap;
        checkPermission();
        map.setMyLocationEnabled(true);
        //get My location
        myLocationAndSetMarker();
    }

    //get My Location
    private void myLocationAndSetMarker() {

        progressBar.setVisibility(View.VISIBLE);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        geocoder = new Geocoder(getContext());

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)//After 10 second
                .setFastestInterval(500); //processing time

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                for (Location location : locationResult.getLocations()) {

                    lat_my = location.getLatitude();
                    lng_my = location.getLongitude();

//get city name and country name
                    try {
                        addresses = geocoder.getFromLocation(lat_my, lng_my, 1);
                        cityName = addresses.get(0).getAdminArea();
                        countryName = addresses.get(0).getCountryName();
                        MarkerOptions markerOptions = new MarkerOptions();

                        latLng_my = new LatLng(lat_my, lng_my);
                        markerOptions.title("My Place\n" + countryName);
                        markerOptions.snippet(cityName);
                        markerOptions.position(latLng_my);
                        map.addMarker(markerOptions);

                        progressBar.setVisibility(View.GONE);


                    } catch (IOException e) {
                        Toast.makeText(getContext(), "failed geocoder", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_my, 11f));
            }
        };
        checkPermission();
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;
        }
    }

    //bottom navigation Action
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.likelyhoodPlace_ID:
                getlikelyHoodPlace();
                break;
            case R.id.bank_ID:
                get_near_Location_response("bank");
                break;
            case R.id.resturent_ID:

                get_near_Location_response("restaurant");
                break;
        }
        return true;
    }

    private void get_near_Location_response(String type) {
        progressBar.setVisibility(View.VISIBLE);
        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MapService service = retrofit.create(MapService.class);

        Call<Example> call = service.getNearbyPlaces(type, lat_my + "," + lng_my, PROXIMITY_RADIUS);

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                try {
                    map.clear();
                    //set marker in my Location
                    MarkerOptions markerOptions = new MarkerOptions();
                    latLng_my = new LatLng(lat_my, lng_my);
                    markerOptions.title("My Place\n" + countryName);
                    markerOptions.snippet(cityName);
                    markerOptions.position(latLng_my);
                    map.addMarker(markerOptions);
                    //End marker in my Location

                    Log.e(TAG, "onResponse: size -" + response.body().getResults().size());
                    // This loop will go through all the results and add marker on each location.
                    for (int i = 0; i < response.body().getResults().size(); i++) {
                        Double near_place_lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();
                        Double near_place_lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();
                        String placeName = response.body().getResults().get(i).getName();
                        String vicinity = response.body().getResults().get(i).getVicinity();
                        MarkerOptions markerOptions_near_place = new MarkerOptions();
                        LatLng latLng_near_place = new LatLng(near_place_lat, near_place_lng);
                        // Position of Marker on Map
                        markerOptions_near_place.position(latLng_near_place);
                        // Adding Title to the Marker
                        markerOptions_near_place.title(placeName + " : " + vicinity);
                        // Adding Marker to the Camera.
                        map.addMarker(markerOptions_near_place);
                        // Adding colour to the marker
                        markerOptions_near_place.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        // move map camera
                        map.moveCamera(CameraUpdateFactory.newLatLng(latLng_near_place));
                        map.animateCamera(CameraUpdateFactory.zoomTo(11));
                        progressBar.setVisibility(View.GONE);

                    }
                    response.body().getResults().clear();

                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }

        });

    }

    //get same place
    private void getlikelyHoodPlace() {
        progressBar.setVisibility(View.VISIBLE);
        checkPermission();
        placeDetectionClient.getCurrentPlace(null).addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                if (task.isSuccessful()) {

                    PlaceLikelihoodBufferResponse response = task.getResult();
                    int count = response.getCount();
                    Log.e(TAG, "onComplete: " + count);

                    int i = 0;
                    String[] placeNames = new String[count];
                    String[] placeAddresses = new String[count];
                    LatLng[] latLngs = new LatLng[count];

                    for (PlaceLikelihood placeLikelihood : response) {
                        // Build a list of likely places to show the user.
                        placeNames[i] = (String) placeLikelihood.getPlace().getName();
                        placeAddresses[i] = (String) placeLikelihood.getPlace()
                                .getAddress();

                        latLngs[i] = placeLikelihood.getPlace().getLatLng();
                        i++;
                        if (i > (count - 1)) {
                            break;
                        }
                    }
                    response.release();

                    map.clear();

                    //set marker in my Location
                    MarkerOptions markerOptions = new MarkerOptions();
                    latLng_my = new LatLng(lat_my, lng_my);
                    markerOptions.title("My Place\n" + countryName);
                    markerOptions.snippet(cityName);
                    markerOptions.position(latLng_my);
                    map.addMarker(markerOptions);
                    //End marker in my Location
                    progressBar.setVisibility(View.GONE);
                    open_Places_List_Dialog(placeNames, placeAddresses, latLngs);

                }
            }
        });
    }

    //same place list in alert dialog
    private void open_Places_List_Dialog(final String[] placeNames, final String[] placeAddresses, final LatLng[] latLngs) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                map.clear();
                //set marker in my Location
                MarkerOptions markerOptions = new MarkerOptions();
                latLng_my = new LatLng(lat_my, lng_my);
                markerOptions.title("My Place\n" + countryName);
                markerOptions.snippet(cityName);
                markerOptions.position(latLng_my);
                map.addMarker(markerOptions);
                //End marker in my Location

                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = latLngs[which];
                String markerSnippet = placeAddresses[which];

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                map.addMarker(new MarkerOptions()
                        .title(placeNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        11f));
            }
        };

        // Display the dialog.
        dialog.setTitle("Place List");
        dialog.setItems(placeNames, listener);
        dialog.show();
    }
}

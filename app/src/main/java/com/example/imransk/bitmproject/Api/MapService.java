package com.example.imransk.bitmproject.Api;

import com.example.imransk.bitmproject.NearByPlace_Pojo_Class.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapService {

    /*@GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyDN7RJFmImYAca96elyZlE5s_fhX-MMuhk")*/
    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyDMzaDjtUQrP8vGP90TkJLX6jhZ3mbBfMU")
    Call<Example> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);

}

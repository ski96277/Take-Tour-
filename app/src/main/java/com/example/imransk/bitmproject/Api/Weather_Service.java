package com.example.imransk.bitmproject.Api;

import com.example.imransk.bitmproject.ModelClass.ForeCast_Weather_Response;
import com.example.imransk.bitmproject.ModelClass.Weather_Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Weather_Service {


    @GET()
    Call<Weather_Response>getAllWeather(@Url String stringUrl);

    @GET()
    Call<ForeCast_Weather_Response>getAllWeather_foreCast(@Url String stringUrl);
}

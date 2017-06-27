package com.example.duc.mapdemo.Retrofit.Api;

import com.example.duc.mapdemo.Model.Direction.Direction;
import com.example.duc.mapdemo.Model.Direction.Distance.Distance;
import com.example.duc.mapdemo.Model.Direction.GeoCoder.GeoCoding;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by DUC on 03/05/2017.
 */

public interface DirectionApi {
    @GET("maps/api/geocode/json")
    Call<GeoCoding> requestGet(@Query("address") String address, @Query("key") String key);

    @GET("maps/api/geocode/json?key=AIzaSyAtkY17Wds2Gu43umulgXyS-X-8aExDGAw")
    Call<GeoCoding> requestLocation(@Query("address") String address);

    @GET("maps/api/geocode/json?key=AIzaSyAtkY17Wds2Gu43umulgXyS-X-8aExDGAw")
    Call<GeoCoding> reverseLocation(@Query("latlng") String address);

    @GET("maps/api/directions/json?key=AIzaSyAtkY17Wds2Gu43umulgXyS-X-8aExDGAw")
    Call<Direction> requestDirection(@Query("origin")String origin, @Query("destination")String destination);

    @GET("maps/api/distancematrix/json?key=AIzaSyAtkY17Wds2Gu43umulgXyS-X-8aExDGAw")
    Call<Distance> calculateDistance(@Query("origins")String origin, @Query("destinations")String destination);
}

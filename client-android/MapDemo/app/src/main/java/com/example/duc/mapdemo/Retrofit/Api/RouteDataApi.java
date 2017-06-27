package com.example.duc.mapdemo.Retrofit.Api;

import com.example.duc.mapdemo.Model.Direction.RouteData.ResponseRouteData;
import com.example.duc.mapdemo.Model.Direction.RouteData.RouteData;
import com.example.duc.mapdemo.Model.Direction.RouteData.TempRouteData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by DUC on 16/05/2017.
 */

public interface RouteDataApi {
    @POST("routedata")
    @FormUrlEncoded
    Call<RouteData> postRouteData(@Field("Duong") String duong,
                                  @Field("Phuong") String phuong,
                                  @Field("Quan") String quan,
                                  @Field("vantoc") Double vantocTB);
    @POST("temproute")
    @FormUrlEncoded
    Call<TempRouteData> postTempRoute(@Field("route_data_id") String routeId,
                                      @Field("vantoc") Double vantoc);

    @POST("routedata/getroute")
    @FormUrlEncoded
    Call<ResponseRouteData> getRouteData(@Field("Duong") String duong,
                                         @Field("Phuong") String phuong,
                                         @Field("Quan") String quan);
}

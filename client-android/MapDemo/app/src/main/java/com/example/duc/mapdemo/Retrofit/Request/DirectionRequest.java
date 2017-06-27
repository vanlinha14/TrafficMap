package com.example.duc.mapdemo.Retrofit.Request;

import com.example.duc.mapdemo.Model.Direction.Direction;
import com.example.duc.mapdemo.Retrofit.Api.DirectionApi;
import com.example.duc.mapdemo.Retrofit.HandleRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by DUC on 03/05/2017.
 */

public class DirectionRequest {
    public static void requestDirection(String origin, String destination) {

        DirectionApi client = HandleRequest.createService(DirectionApi.class);
        Call<Direction> call = client.requestDirection(origin,destination);
        call.enqueue(new Callback<Direction>() {
            @Override
            public void onResponse(Call<Direction> call, Response<Direction> response) {

            }

            @Override
            public void onFailure(Call<Direction> call, Throwable t) {

            }
        });
    }
}

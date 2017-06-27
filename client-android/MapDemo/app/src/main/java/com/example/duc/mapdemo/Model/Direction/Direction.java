package com.example.duc.mapdemo.Model.Direction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DUC on 03/05/2017.
 */

public class Direction {
    @SerializedName("routes")
    @Expose
    private List<Route> routes = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

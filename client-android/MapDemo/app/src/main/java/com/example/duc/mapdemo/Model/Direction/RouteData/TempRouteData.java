package com.example.duc.mapdemo.Model.Direction.RouteData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DUC on 16/05/2017.
 */

public class TempRouteData {
    @SerializedName("route_data_id")
    @Expose
    private Integer routeDataId;
    @SerializedName("vantoc")
    @Expose
    private Integer vantoc;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getRouteDataId() {
        return routeDataId;
    }

    public void setRouteDataId(Integer routeDataId) {
        this.routeDataId = routeDataId;
    }

    public Integer getVantoc() {
        return vantoc;
    }

    public void setVantoc(Integer vantoc) {
        this.vantoc = vantoc;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}

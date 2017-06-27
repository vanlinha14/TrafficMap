package com.example.duc.mapdemo.Model.Direction.Distance;

import com.example.duc.mapdemo.Model.Direction.DisDur;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DUC on 12/05/2017.
 */

public class Element {
    @SerializedName("distance")
    @Expose
    private DisDur distance;
    @SerializedName("duration")
    @Expose
    private DisDur duration;
    @SerializedName("status")
    @Expose
    private String status;

    public DisDur getDistance() {
        return distance;
    }

    public void setDistance(DisDur distance) {
        this.distance = distance;
    }

    public DisDur getDuration() {
        return duration;
    }

    public void setDuration(DisDur duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

package com.example.duc.mapdemo.Model.Direction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DUC on 03/05/2017.
 */

public class Leg {
    @SerializedName("distance")
    @Expose
    private DisDur distance;
    @SerializedName("duration")
    @Expose
    private DisDur duration;
    @SerializedName("end_address")
    @Expose
    private String endAddress;
    @SerializedName("end_location")
    @Expose
    private Location endLocation;
    @SerializedName("start_address")
    @Expose
    private String startAddress;
    @SerializedName("start_location")
    @Expose
    private Location startLocation;
    @SerializedName("steps")
    @Expose
    private List<Step> steps = null;
    @SerializedName("traffic_speed_entry")
    @Expose
    private List<Object> trafficSpeedEntry = null;
    @SerializedName("via_waypoint")
    @Expose
    private List<Object> viaWaypoint = null;

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

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public List<Object> getTrafficSpeedEntry() {
        return trafficSpeedEntry;
    }

    public void setTrafficSpeedEntry(List<Object> trafficSpeedEntry) {
        this.trafficSpeedEntry = trafficSpeedEntry;
    }

    public List<Object> getViaWaypoint() {
        return viaWaypoint;
    }

    public void setViaWaypoint(List<Object> viaWaypoint) {
        this.viaWaypoint = viaWaypoint;
    }
}

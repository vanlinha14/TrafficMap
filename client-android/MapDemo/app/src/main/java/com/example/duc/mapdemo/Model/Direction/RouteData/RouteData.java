package com.example.duc.mapdemo.Model.Direction.RouteData;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Created by DUC on 16/05/2017.
 */

public class RouteData implements Serializable {

    @SerializedName("id")
    @Expose
    Integer id;

    @SerializedName("Duong")
    @Expose
    String tenDuong;

    @SerializedName("Phuong")
    @Expose
    String tenPhuong;

    @SerializedName("Quan")
    @Expose
    String tenQuan;

    @SerializedName("VantocTB")
    @Expose
    Double vanTocTB;

    @SerializedName("tempData")
    @Expose
    List<TempRouteData> tempRouteDatas;

    LatLng startLocation;

    public RouteData() {
    }

    public RouteData(LatLng startLocation) {
        this.startLocation = startLocation;
    }

    public LatLng getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(LatLng startLocation) {
        this.startLocation = startLocation;
    }

    public String getTenDuong() {
        return tenDuong;
    }

    public void setTenDuong(String tenDuong) {
        this.tenDuong = tenDuong;
    }

    public String getTenQuan() {
        return tenQuan;
    }

    public void setTenQuan(String tenQuan) {
        this.tenQuan = tenQuan;
    }

    public String getTenPhuong() {
        return tenPhuong;
    }

    public void setTenPhuong(String tenPhuong) {
        this.tenPhuong = tenPhuong;
    }

    public Double getVantoc() {
        return vanTocTB;
    }

    public void setVantoc(Double vantoc) {
        this.vanTocTB = vantoc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TempRouteData> getTempRouteDatas() {
        return tempRouteDatas;
    }

    public void setTempRouteDatas(List<TempRouteData> tempRouteDatas) {
        this.tempRouteDatas = tempRouteDatas;
    }

    @Override
    public String toString() {
        return getTenDuong() + "," + getTenPhuong()  + "," + getTenQuan() + ", VanTocTB:"+ getVantoc() + ", StartLocation:" + getLocation();
    }

    public String getLocation()
    {
        return startLocation.latitude + "," + startLocation.longitude;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RouteData){
            if(((RouteData) obj).getTenDuong().equals(this.getTenDuong())
                    && ((RouteData) obj).getTenPhuong().equals(this.getTenPhuong())
                    && ((RouteData) obj).getTenQuan().equals(this.getTenQuan()))
                return true;
            else return false;
        }
        return false;
    }

}

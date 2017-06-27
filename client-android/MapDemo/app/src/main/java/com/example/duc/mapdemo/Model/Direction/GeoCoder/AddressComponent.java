package com.example.duc.mapdemo.Model.Direction.GeoCoder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DUC on 12/05/2017.
 */

public class AddressComponent {
    @SerializedName("long_name")
    @Expose
    private String longName;

    @SerializedName("short_name")
    @Expose
    private String shortName;

    @SerializedName("types")
    @Expose
    private List<String> types = null;

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public List<String> getTypes() {
        return types;
    }

    public String getType(int i)
    {
        return types.get(i);
    }

    public String getType(String type)
    {
        for (String i:
             types) {
            if(i.equals(type))
                return i;
        }
        return null;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}

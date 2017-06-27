package com.example.duc.mapdemo.Model.Direction.GeoCoder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DUC on 12/05/2017.
 */

public class GeoCoding {
    @SerializedName("results")
    @Expose
    private List<ResultGeoCoder> results = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<ResultGeoCoder> getResults() {
        return results;
    }

    public ResultGeoCoder getResult(int i)
    {
        if(results.size() != 0)
            return results.get(i);
        return null;
    }

    public void setResults(List<ResultGeoCoder> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

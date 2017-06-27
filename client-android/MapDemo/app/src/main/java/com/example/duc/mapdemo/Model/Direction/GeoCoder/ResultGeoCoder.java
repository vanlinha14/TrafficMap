package com.example.duc.mapdemo.Model.Direction.GeoCoder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DUC on 12/05/2017.
 */

public class ResultGeoCoder {
    @SerializedName("address_components")
    @Expose
    private List<AddressComponent> addressComponents = null;

    @SerializedName("geometry")
    @Expose
    private Geometry geometry = null;

    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;

    @SerializedName("place_id")
    @Expose
    private String placeId;

    @SerializedName("types")
    @Expose
    private List<String> types = null;

    public List<AddressComponent> getAddressComponents() {
        return addressComponents;
    }

    public AddressComponent getAddressComponent(int i)
    {
        return addressComponents.get(i);
    }

    public AddressComponent getAddressComponentByType(String type)
    {
        for (AddressComponent address : addressComponents)
        {
            if(address.getType(type) != null){
                return address;
            }
        }
        return null;
    }

    public void setAddressComponents(List<AddressComponent> addressComponents) {
        this.addressComponents = addressComponents;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }


    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}

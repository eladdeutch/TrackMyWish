package com.andro.obe.trackmywish.Models;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Brkmo on 17/05/2017.
 */

public class WishPlace {
    private String placeId;
    private String placeName;
    private String plaseAddress;
    private double latitude;
    private double longitude;

    public WishPlace() {
    }

    public WishPlace(Place place) {
        placeId = place.getId();
        placeName = place.getName().toString();
        plaseAddress = place.getAddress().toString();
        latitude = place.getLatLng().latitude;
        longitude = place.getLatLng().longitude;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getPlaseAddress() {
        return plaseAddress;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getPlaceName() {
        return placeName;
    }
}

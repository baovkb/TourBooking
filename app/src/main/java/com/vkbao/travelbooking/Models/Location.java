package com.vkbao.travelbooking.Models;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

public class Location {
    private String location_id;
    private String location;

    public Location() {
    }

    public Location(String location_id, String location) {
        this.location_id = location_id;
        this.location = location;
    }

    @Override
    public String toString() {
        return location;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String loc) {
        location = loc;
    }
}

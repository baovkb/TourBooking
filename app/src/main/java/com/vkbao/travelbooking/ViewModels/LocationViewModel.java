package com.vkbao.travelbooking.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vkbao.travelbooking.Helper.Callback;
import com.vkbao.travelbooking.Models.Location;
import com.vkbao.travelbooking.Repositories.LocationRepository;

import java.util.List;

public class LocationViewModel extends AndroidViewModel {
    private LiveData<List<Location>> locationListLiveData;
    private LocationRepository locationRepository;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        locationRepository = new LocationRepository();
        locationListLiveData = locationRepository.getAllLocation();
    }

    public LiveData<List<Location>> getAllLocation() {
        return locationListLiveData;
    }

    public void getLocationByID(String locationID, Callback<Location> callback) {
        locationRepository.getLocationByID(locationID, callback);
    }
}

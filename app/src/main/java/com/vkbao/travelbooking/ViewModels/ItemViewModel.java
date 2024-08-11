package com.vkbao.travelbooking.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vkbao.travelbooking.Helper.Callback;
import com.vkbao.travelbooking.Models.Item;
import com.vkbao.travelbooking.Repositories.ItemRepository;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {
    private ItemRepository itemRepository;
    private LiveData<List<Item>> allItems;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        itemRepository = new ItemRepository();
        allItems = itemRepository.getAllItems();
    }

    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    public LiveData<List<Item>> getItemsByLocationID(String locationID) {
        return itemRepository.getItemsByLocationID(locationID);
    }

    public LiveData<List<Item>> getItemsByLocationIdCategoryId(@NonNull String locationId,@NonNull String categoryId) {
        return itemRepository.getItemsByLocationIdCategoryId(locationId, categoryId);
    }

    public void getItemByID(String itemID, Callback<Item> callback) {
        itemRepository.getItemByID(itemID, callback);
    }
}

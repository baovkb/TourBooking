package com.vkbao.travelbooking.ViewModels;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vkbao.travelbooking.Helper.Callback;
import com.vkbao.travelbooking.Models.Item;
import com.vkbao.travelbooking.Repositories.ItemRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    public CompletableFuture<Boolean> updateItem(Item newItem) {
        return itemRepository.updateItem(newItem);
    }

    public CompletableFuture<String> uploadBanner(Uri imageUri, String bannerID) {
        return itemRepository.uploadBanner(imageUri, bannerID);
    }

    public String createID() {
        return itemRepository.createID();
    }

    public CompletableFuture<Boolean> deleteItemByID(String itemID) {
        return itemRepository.deleteItemByID(itemID);
    }

    public CompletableFuture<Boolean> deleteBannerByID(String itemID){
        return itemRepository.deleteBannerByID(itemID);
    }
}

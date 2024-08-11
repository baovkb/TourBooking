package com.vkbao.travelbooking.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vkbao.travelbooking.Helper.Callback;
import com.vkbao.travelbooking.Models.Category;
import com.vkbao.travelbooking.Repositories.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private CategoryRepository categoryRepository;
    private LiveData<List<Category>> categoryLiveData;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository();
        categoryLiveData = categoryRepository.getCategoryLiveData();
    }

    public LiveData<List<Category>> getAllCategory() {
        return categoryLiveData;
    }

    public void getCategoryByID(String categoryID, Callback<Category> callback) {
        categoryRepository.getCategoryByID(categoryID, callback);
    }
}

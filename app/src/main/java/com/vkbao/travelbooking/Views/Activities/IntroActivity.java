package com.vkbao.travelbooking.Views.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.vkbao.travelbooking.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {
    private ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("intro", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        boolean isFirst = sharedPreferences.getBoolean("first", true);
        if (!isFirst) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            editor.putBoolean("first", false);
            editor.apply();
        }

        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.introBtn.setOnClickListener((view -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }));
    }
}
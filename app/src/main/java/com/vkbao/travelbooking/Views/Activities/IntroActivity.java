package com.vkbao.travelbooking.Views.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.vkbao.travelbooking.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {
    private ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.introBtn.setOnClickListener((view -> {
            Log.d("test", "intro is clicked");
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }));
    }
}
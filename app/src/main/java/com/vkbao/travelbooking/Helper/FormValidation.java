package com.vkbao.travelbooking.Helper;

import android.app.Activity;
import android.content.Context;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vkbao.travelbooking.Models.Account;
import com.vkbao.travelbooking.R;

import java.util.ArrayList;
import java.util.List;

public class FormValidation {
    private Context context;

    public FormValidation(Context context) {
        this.context = context;
    }

    public String checkEmail(TextView emailTextView, TextView emailNotifyTextView) {
        String email = emailTextView.getText().toString().trim();

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTextView.setBackgroundResource(R.drawable.login_field_input);
            emailNotifyTextView.setVisibility(View.GONE);

            return null;
        } else {
            emailNotifyTextView.setVisibility(View.VISIBLE);
            emailNotifyTextView.setText(R.string.email_invalid);
            emailTextView.setBackgroundResource(R.drawable.red_bg);

            return emailTextView.getId() + ":" + context.getString(R.string.email_invalid);
        }
    }

    public String checkMinLength(TextView minLengthTextView, TextView minLengthNotifyEditText, int minLength) {
        String text = minLengthTextView.getText().toString().trim();
        if (text.length() >= minLength) {
            minLengthTextView.setBackgroundResource(R.drawable.login_field_input);
            minLengthNotifyEditText.setVisibility(View.GONE);

            return null;
        } else {
            minLengthTextView.setBackgroundResource(R.drawable.red_bg);

            String message = context.getString(R.string.password_min_length, minLength);
            minLengthNotifyEditText.setText(message);
            minLengthNotifyEditText.setVisibility(View.VISIBLE);

            return minLengthTextView.getId() + ":" + message;
        }
    }


    public String checkMatch(TextView compareTextView, TextView comparedTextView, TextView comparedNotifyTextView) {
        String compare = compareTextView.getText().toString().trim();
        String compared = comparedTextView.getText().toString().trim();

        if (compared.equals(compare)) {
            comparedTextView.setBackgroundResource(R.drawable.login_field_input);
            comparedNotifyTextView.setVisibility(View.GONE);
            return null;
        } else {
            comparedTextView.setBackgroundResource(R.drawable.red_bg);
            comparedNotifyTextView.setText(R.string.signup_password_no_match);
            comparedNotifyTextView.setVisibility(View.VISIBLE);

            return comparedTextView.getId() + ":" + context.getString(R.string.signup_password_no_match);
        }
    }
}


package com.aiemail.superemail.Activities;



import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.aiemail.superemail.MyApplication;
import com.aiemail.superemail.R;
import com.aiemail.superemail.databinding.ActivityPrivacyPolicyActivtyBinding;
import com.aiemail.superemail.utilis.Helpers;

import java.util.HashMap;
import java.util.Map;

public class PrivacyPolicyActivty extends AppCompatActivity {

    private static  boolean IS_SPLASH =false ;
    ActivityPrivacyPolicyActivtyBinding binding;
    boolean isCheckedpp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Utils.setLightStatusBarColor(getWindow().getDecorView(), this);
        if (MyApplication.instance.isNightModeEnabled()) {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        binding = ActivityPrivacyPolicyActivtyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        IS_SPLASH = true;
        String thisLink = getString(R.string.menu_item_pp);
        String yourString = getString(R.string.intro_tc, thisLink);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(yourString);
        int startIndex = yourString.indexOf(thisLink);
        int lastIndex = startIndex + thisLink.length();
        spannableStringBuilder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Helpers.Companion.OpenLink(PrivacyPolicyActivty.this);
            }
        }, startIndex, lastIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.chkByClick.setText(spannableStringBuilder);
        binding.chkByClick.setMovementMethod(LinkMovementMethod.getInstance());
        binding.chkByClick.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isCheckedpp = isChecked;
            binding.btnGetStarted.setEnabled(isChecked);
            binding.btnGetStarted.setBackgroundResource(isChecked ? R.drawable.rounded_select_corner_grey_bg : R.drawable.rounded_corner_grey_bg);
            binding.btnGetStarted.setTextColor(ContextCompat.getColor(PrivacyPolicyActivty.this, isChecked ? R.color.textColorPrimary : R.color.black));
        });

        binding.btnGetStarted.setOnClickListener(v -> {
            // callStep 2



//            setSlider();
            if (isCheckedpp) {
                MyApplication.instance.preferenceManager.SetBoolean( "PRIVACY_CHECKED", true);

                startActivity(new Intent(this,LoginActivity.class));
                finish();

            } else {
                Toast.makeText(this, "Please accept privacy policy", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onBackPressed() {
      finish();
    }
}
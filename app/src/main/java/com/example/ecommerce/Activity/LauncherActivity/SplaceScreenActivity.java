package com.example.ecommerce.Activity.LauncherActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.ecommerce.Activity.user.UserMainActivity;
import com.example.ecommerce.Activity.vendor.VendorMainActivity;
import com.example.ecommerce.R;
import com.example.ecommerce.utils.PreferenceManager;

public class SplaceScreenActivity extends AppCompatActivity {

    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splace_screen);
        preferenceManager=new PreferenceManager(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run(){
                if (preferenceManager.getLoginVendor()) {
                    Intent i = new Intent(SplaceScreenActivity.this, VendorMainActivity.class);
                     startActivity(i);
                     finish();
                 }
                else if (preferenceManager.getLoginUser())
                {
                    Intent i = new Intent(SplaceScreenActivity.this, UserMainActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Intent i = new Intent(SplaceScreenActivity.this, SelectTypeActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        },2000);
    }
}

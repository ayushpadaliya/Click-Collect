package com.example.ecommerce.Activity.LauncherActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.example.ecommerce.utils.PreferenceManager;

public class SelectTypeActivity extends AppCompatActivity {

    Button vendor_btn,user_btn;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_type);
        vendor_btn=findViewById(R.id.vendorBtn);
        user_btn=findViewById(R.id.userBtn);
        preferenceManager=new PreferenceManager(this);
//        preferenceManager.loginUser(false);

        vendor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(SelectTypeActivity.this, LoginActivity.class);
                i.putExtra("vendor","vendor_login");
                startActivity(i);
                }
        });
        user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SelectTypeActivity.this,LoginActivity.class);
                i.putExtra("user","user_login");
                startActivity(i);
            }
        });
    }
}
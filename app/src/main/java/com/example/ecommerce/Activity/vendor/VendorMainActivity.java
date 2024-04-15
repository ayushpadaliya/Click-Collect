package com.example.ecommerce.Activity.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.Activity.LauncherActivity.SelectTypeActivity;
import com.example.ecommerce.Activity.LauncherActivity.SplaceScreenActivity;
import com.example.ecommerce.R;
import com.example.ecommerce.utils.PreferenceManager;

public class VendorMainActivity extends AppCompatActivity {
    CardView category,subcategory,products;
    ImageView logout,userOrder;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_main);
        category=findViewById(R.id.category);
        preferenceManager=new PreferenceManager(this);
        subcategory=findViewById(R.id.subcategory);
        userOrder=findViewById(R.id.user_orders);
        products=findViewById(R.id.product);
        logout=findViewById(R.id.user_logout);
        userOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VendorMainActivity.this,OrderActivity.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VendorMainActivity.this);
                builder.setMessage("Are you sure you want to logout?");
                builder.setTitle(" LOGOUT !");
                Drawable mydrawable = getResources().getDrawable(R.drawable.checker);
                builder.setIcon(mydrawable);
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    preferenceManager.loginVendor(false);
                    startActivity(new Intent(VendorMainActivity.this, SelectTypeActivity.class));
                    finish();

                });
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog= builder.create();
                alertDialog.setOnShowListener(dialog -> {
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.lightgreen));
                    alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.lightgreen));
                });
                alertDialog.show();
            }
        });
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VendorMainActivity.this, VendorCategoryList.class));
            }
        });
        subcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VendorMainActivity.this, VendorSubCategoryListActivity.class));
            }
        });
        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VendorMainActivity.this, ProductListActivity.class));

            }
        });
    }
}
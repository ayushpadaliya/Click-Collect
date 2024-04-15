package com.example.ecommerce.Activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ecommerce.Activity.Fragments.UserHomeFragment;
import com.example.ecommerce.Activity.Fragments.UserOrderFragment;
import com.example.ecommerce.Activity.Fragments.UserProfileFragment;
import com.example.ecommerce.Activity.adapter.BroadCasters.CheckInternet;
import com.example.ecommerce.Activity.adapter.Vendorside.ViewpagerAdpater;
import com.example.ecommerce.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class UserMainActivity extends AppCompatActivity {
    ViewPager2 viewPager;
    ViewpagerAdpater viewpagerAdpater;
    UserHomeFragment userHomeFragment;
    UserOrderFragment userOrderFragment;
    UserProfileFragment userProfileFragment;
    MenuItem home,order,profile;
    BottomNavigationView bottomNavigationView;
    ArrayList<Fragment> FragmentList=new ArrayList<>();
    CheckInternet checkInternet;
    BroadcastReceiver broadcastReceiver;
    LinearLayout linearLayout;
    androidx.appcompat.widget.AppCompatButton refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        userHomeFragment=new UserHomeFragment();
        userOrderFragment=new UserOrderFragment();
        userProfileFragment=new UserProfileFragment();
        linearLayout=findViewById(R.id.internet);
        broadcastReceiver=new CheckInternet(this);
        InternetStatus();
        refresh=findViewById(R.id.refresh);
        viewPager=(ViewPager2) findViewById(R.id.viewpage);
        FragmentList.add(userHomeFragment);
        FragmentList.add(userOrderFragment);
        FragmentList.add(userProfileFragment);
        bottomNavigationView=findViewById(R.id.navigation);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId(); // Get the ID of the selected item

                // Define constant values to match your menu item IDs
                final int HOME_ID = R.id.home;
                final int ORDER_ID = R.id.order;
                final int PROFILE_ID = R.id.profile;

                if (itemId == HOME_ID) {
                    viewPager.setCurrentItem(0);
                    return true;
                } else if (itemId == ORDER_ID) {
                    viewPager.setCurrentItem(1);
                    return true;
                } else if (itemId == PROFILE_ID) {
                    viewPager.setCurrentItem(2);
                    return true;
                }
                return false;
            };
        });
        viewPager.setUserInputEnabled(false);
        viewPager.setAdapter(new ViewpagerAdpater(this,FragmentList));
        boolean openOrderFragment = getIntent().getBooleanExtra("openOrderFragment", false);
        if (openOrderFragment) {
            viewPager.setCurrentItem(1); // Change to the order fragment
        }

    }
    public void InternetStatus()
    {
        registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public class CheckInternet extends BroadcastReceiver {

         Context context;


        public CheckInternet(Context context) {
            this.context = context;
        }
        public  Boolean getNetworkInfo()
        {

            ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if (networkInfo!=null)
            {
                return  true;
            }
            else {
                return false;
            }

        }
        @Override
        public void onReceive(Context context, Intent intent) {
            Boolean isConnected= getNetworkInfo();
            if (isConnected)
            {
                Toast.makeText(context, "connected", Toast.LENGTH_SHORT).show();
            }
            else
            {
                bottomNavigationView.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                Toast.makeText(context, "not connected", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
package com.example.ecommerce.Activity.Fragments;

import static android.content.Intent.ACTION_BATTERY_CHANGED;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.Activity.adapter.BroadCasters.BatteryBroadcast;
import com.example.ecommerce.Activity.adapter.BroadCasters.BluetoothBroadcast;
import com.example.ecommerce.Activity.adapter.BroadCasters.LocationBroadcast;
import com.example.ecommerce.Activity.user.ReminderTaskActivity;
import com.example.ecommerce.Activity.user.ScanQrActivity;
import com.example.ecommerce.R;


public class UserProfileFragment extends Fragment {



ImageView imageView,QrCode;
    public UserProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_user_profile, container, false);

        imageView=view.findViewById(R.id.bookEvent);
        BroadcastReceiver br= new BluetoothBroadcast();
        QrCode=view.findViewById(R.id.user_log);
        QrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ScanQrActivity.class));
            }
        });
        BroadcastReceiver br1=new BatteryBroadcast();
        BroadcastReceiver broadcastReceiver=new LocationBroadcast(getActivity());
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        IntentFilter filter2 = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        IntentFilter filter1=new IntentFilter(ACTION_BATTERY_CHANGED);
        getActivity().registerReceiver(br,filter);
        getActivity().registerReceiver(br1,filter1);
        getActivity().registerReceiver(broadcastReceiver,filter2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ReminderTaskActivity.class));
            }
        });
        return view;
    }


}
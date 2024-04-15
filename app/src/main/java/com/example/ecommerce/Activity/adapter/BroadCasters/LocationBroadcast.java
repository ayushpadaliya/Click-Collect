package com.example.ecommerce.Activity.adapter.BroadCasters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class LocationBroadcast extends BroadcastReceiver {

     static Context context;

    public LocationBroadcast(Context context) {
        LocationBroadcast.context = context;
    }

    public static Boolean isLocationOn()
    {
        LocationManager locationManager= (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
        return false;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Boolean location=isLocationOn();
        if (location)
        {
            Toast.makeText(context, "location is on ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "location is off", Toast.LENGTH_SHORT).show();
        }
    }
}

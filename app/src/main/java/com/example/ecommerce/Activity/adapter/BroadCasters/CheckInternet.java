package com.example.ecommerce.Activity.adapter.BroadCasters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.widget.Toast;

public class CheckInternet extends BroadcastReceiver {

    static Context context;


    public CheckInternet(Context context) {
        CheckInternet.context = context;
    }
    public static Boolean getNetworkInfo()
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
           Toast.makeText(context, "not connected", Toast.LENGTH_SHORT).show();
       }
    }
}

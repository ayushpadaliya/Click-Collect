package com.example.ecommerce.Activity.adapter.BroadCasters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

public class BatteryBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        if (action!=null && action.equals(Intent.ACTION_BATTERY_CHANGED))
        {
            int level=intent.getIntExtra("level",0);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            if (level!=-1 && scale!=-1)
            {
                float batteryPct = level * 100 / (float)scale;
                Log.d("battery","level"+batteryPct);
//                Toast.makeText(context, " battery "+String.valueOf(batteryPct)+"%", Toast.LENGTH_SHORT).show();
            }

        }
    }
}

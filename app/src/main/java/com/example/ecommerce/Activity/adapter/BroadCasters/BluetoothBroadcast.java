package com.example.ecommerce.Activity.adapter.BroadCasters;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BluetoothBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        if (action!=null && action.equals(BluetoothAdapter.ACTION_STATE_CHANGED))
        {
            int state=intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.ERROR);
            switch (state)
            {
                case BluetoothAdapter.STATE_OFF:
                {
                    Toast.makeText(context, "bluetooth off", Toast.LENGTH_SHORT).show();
                    break;
                }
                case BluetoothAdapter.STATE_ON:
                {
                    Toast.makeText(context, "bluetooth on", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }
}

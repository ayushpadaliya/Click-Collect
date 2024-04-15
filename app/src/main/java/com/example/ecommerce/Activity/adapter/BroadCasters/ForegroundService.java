package com.example.ecommerce.Activity.adapter.BroadCasters;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.ServiceCompat;

import com.example.ecommerce.R;

public class ForegroundService extends Service {
    NotificationCompat.Builder builder;
    CountDownTimer countDownTimer;
    NotificationManager notificationManager;
    int notificationId = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        builder = new NotificationCompat.Builder(this, "456")
                .setContentTitle("TIMER")
                .setAutoCancel(true)
                .setOngoing(true)
                .setSmallIcon(R.drawable.iconbg)
                .setPriority(NotificationCompat.PRIORITY_MAX);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int minute = intent.getIntExtra("setTimer", 0);
        long millisecond =  minute * 60 * 1000;


        startForeground(notificationId, builder.build());
        countDownTimer = new CountDownTimer(millisecond, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                builder.setContentText(formatTime(millisUntilFinished));
                 notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(notificationId, builder.build());
            }

            @Override
            public void onFinish() {
                stopSelf();
            }
        };
        countDownTimer.start();

        return START_STICKY;
    }

    private String formatTime(long millis) {
        long remainingSeconds = millis / 1000;
        long remainingMin = remainingSeconds / 60;
        remainingSeconds %= 60;
        return String.format("%02d:%02d", remainingMin, remainingSeconds);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            builder = new NotificationCompat.Builder(this, "456")
                    .setContentText("time over !")
                    .setContentTitle("TIMER")
                    .setAutoCancel(true)
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.iconbg)
                    .setPriority(NotificationCompat.PRIORITY_MAX);
            notificationManager.notify(1,builder.build());
        }
    }
}


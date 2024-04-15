package com.example.ecommerce.Activity.adapter.BroadCasters;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.ecommerce.Activity.user.ReminderTaskActivity;
import com.example.ecommerce.R;

public class AlarmBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("123", "reminder", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        Intent notificationIntent = new Intent(context, ReminderTaskActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(notificationIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        String message=intent.getStringExtra("name");
        Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.accept);
        Uri alarmSound = RingtoneManager.getActualDefaultRingtoneUri(context,R.raw.coinflip);
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.notification);

        AudioAttributes audioAttributes= new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).setUsage(AudioAttributes.USAGE_ALARM).build();
        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(context, "456")
                .setContentTitle("Reminder")
                .setContentText(message)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setOngoing(true)
                .setSound(uri)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(bitmap))
                .setSmallIcon(R.drawable.iconbg)
                        .setPriority(NotificationCompat.PRIORITY_MAX);

        mNotifyBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        Toast.makeText(context, "notification sent", Toast.LENGTH_SHORT).show();
        notificationManager.notify(1, mNotifyBuilder.build());
    }
}

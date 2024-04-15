package com.example.ecommerce.Activity.user;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommerce.Activity.adapter.BroadCasters.ForegroundService;
import com.example.ecommerce.R;

import java.util.Calendar;

public class ServiceActivity extends AppCompatActivity {

    androidx.appcompat.widget.AppCompatButton button;
    androidx.appcompat.widget.AppCompatButton timePick;
    int setMinuteTimer=0,Hour_cal=0,Minute_cal=0;
   Boolean isTimerRunning=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_service);
//        notificationChannel();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        timePick=findViewById(R.id.timerPicker);
        timePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(ServiceActivity.this, R.style.dialogTheme1, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar calendar = Calendar.getInstance();
                        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                        int currentMinute = calendar.get(Calendar.MINUTE);

                        if (hourOfDay < currentHour || (hourOfDay == currentHour && minute <= currentMinute)) {
                            // Selected time is in the past
                            Toast.makeText(ServiceActivity.this, "Please select a future time", Toast.LENGTH_SHORT).show();
                        } else {
                            // Selected time is in the future
                            timePick.setText(hourOfDay + ":" + minute);
                            int Hour_set = view.getHour();
                            int Minute_set = view.getMinute();
                            setMinuteTimer = (Hour_set * 60 + Minute_set) - (currentHour * 60 + currentMinute);
                        }
                    }
                }, 0, 0, true);
                timePickerDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        timePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.lightgreen));
                        timePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.lightgreen));
                    }
                });
                timePickerDialog.show();
            }
        });


        button=findViewById(R.id.setTimer);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (setMinuteTimer != 0) {
                    if (!isTimerRunning) {
                        Intent i = new Intent(ServiceActivity.this, ForegroundService.class);
                        i.putExtra("setTimer", setMinuteTimer);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(i);
                            isTimerRunning = true;

                            // Disable the button to prevent starting the timer again
                            button.setEnabled(false);

                            // Start a countdown to re-enable the button after setMinuteTimer minutes
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Enable the button after setMinuteTimer minutes
                                    button.setEnabled(true);
                                    isTimerRunning = false;
                                }
                            }, setMinuteTimer * 60 * 1000);
                        }
                    } else {
                        Toast.makeText(ServiceActivity.this, "Please wait until the current timer finishes", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    public void notificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("456", "reminder", NotificationManager.IMPORTANCE_DEFAULT);
            Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://" + ServiceActivity.this.getPackageName() + "/"+R.raw.coinflip);
            AudioAttributes audioAttributes= new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).setUsage(AudioAttributes.USAGE_ALARM).build();
            channel.setSound(uri,audioAttributes);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }
}
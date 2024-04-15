package com.example.ecommerce.Activity.user;

import static android.app.PendingIntent.getActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommerce.Activity.Fragments.DialogFragment;
import com.example.ecommerce.Activity.adapter.BroadCasters.AlarmBroadCast;
import com.example.ecommerce.R;
import com.example.ecommerce.utils.DbHandler;
import com.example.ecommerce.utils.VariableBag;

import java.sql.Timestamp;
import java.util.Calendar;

public class ReminderTaskActivity extends AppCompatActivity  {

    TextView setBtnDate;
    TextView setBtnTime;
    EditText editText;
    DbHandler dbHandler;

    androidx.appcompat.widget.AppCompatButton reminder;
/*
    DatePicker datePicker;
    TimePicker timePicker;
*/
    int Year=0,Date=0,Month=0,Hour=0,Minute=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reminder_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        notificationChannel();
        dbHandler=new DbHandler(this);
        editText=findViewById(R.id.editEvent);
        reminder=findViewById(R.id.setButton);
        setBtnDate=findViewById(R.id.setButtonDate);
        setBtnTime=findViewById(R.id.setButtonTime);
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int date=calendar.get(Calendar.DATE);
        setBtnDate.setText(date+"-"+month+"-"+year);
/*        datePicker=findViewById(R.id.datePick);
        timePicker=findViewById(R.id.timePick);*/

        setBtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatePickerDialog datePickerDialog=new DatePickerDialog(ReminderTaskActivity.this,R.style.dialogTheme);
                datePickerDialog.setCanceledOnTouchOutside(true);
                datePickerDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.lightgreen));
                        datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.lightgreen));
                    }
                });
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Year=datePicker.getYear();
                        Month=datePicker.getMonth();
                        Date=datePicker.getDayOfMonth();
                                setBtnDate.setText(String.format("%d-%d-%d", i2, i1, i));
                    }
                });

                datePickerDialog.show();
            }
        });
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        setBtnTime.setText(hour+":"+minute);
        setBtnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(ReminderTaskActivity.this,R.style.dialogTheme1,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            setBtnTime.setText(hourOfDay+":"+minute);
                            Hour=view.getHour();
                            Minute=view.getMinute();
                    }
                }, hour, minute, false);
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
        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                Calendar selectedTime = Calendar.getInstance();
                selectedTime.set(Year, Month, Date, Hour, Minute, 0);

                if (Year != 0 && Month != 0 && Date != 0 && Hour != 0 && Minute != 0) {
                    if (selectedTime.before(currentTime)) {
                        Toast.makeText(ReminderTaskActivity.this, "Selected time is in the past", Toast.LENGTH_SHORT).show();
                    } else {
                        dbHandler.addUser(editText.getText().toString(), String.valueOf(selectedTime.getTimeInMillis()));
                        Intent i = new Intent(ReminderTaskActivity.this, AlarmBroadCast.class);
                        Toast.makeText(ReminderTaskActivity.this, " " + editText.getText().toString(), Toast.LENGTH_SHORT).show();
                        i.putExtra("name", editText.getText().toString());
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(ReminderTaskActivity.this, 0, i, PendingIntent.FLAG_IMMUTABLE);
                        AlarmManager alarmManager = (AlarmManager) ReminderTaskActivity.this.getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, selectedTime.getTimeInMillis(), pendingIntent);
                        Toast.makeText(ReminderTaskActivity.this, "Event set ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ReminderTaskActivity.this, "Please select both date and time", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
    public void notificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("456", "reminder", NotificationManager.IMPORTANCE_DEFAULT);
            Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://" + ReminderTaskActivity.this.getPackageName() + "/"+R.raw.coinflip);
            AudioAttributes audioAttributes= new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).setUsage(AudioAttributes.USAGE_ALARM).build();
            channel.setSound(uri,audioAttributes);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }
}
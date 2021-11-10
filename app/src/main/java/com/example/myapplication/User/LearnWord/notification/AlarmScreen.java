package com.example.myapplication.User.LearnWord.notification;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.User.LearnWord.notification.source.AlarmReceiver;
import com.example.myapplication.User.LearnWord.notification.source.AlarmSqliteHelper;
import com.example.myapplication.User.LearnWord.notification.source.DayItem;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import java.util.Calendar;


public class AlarmScreen extends AppCompatActivity {

    private AlarmSqliteHelper alarmSqliteHelper;
    private ArrayList<DayItem>dayItems;
    private DayButtonAdapter adapter ;
    private MaterialTimePicker picker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Button save,cancel,pickTime;
    private TextView txt_gio,txtphut;
    ToggleButton stateToggle;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_screen);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChanel();
        }
        alarmSqliteHelper = new AlarmSqliteHelper(getBaseContext());
        dayItems = new ArrayList<>();

        setControl();
        setEvent();

    }

    private void setEvent() {
        alarmSqliteHelper.fetchData(dayItems,txt_gio,txtphut,stateToggle);
        if(dayItems.size()==0){
            alarmSqliteHelper.addDefaultFirstTime();
            alarmSqliteHelper.fetchData(dayItems,txt_gio,txtphut,stateToggle);
        }
        adapter.notifyDataSetChanged();

        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shotTimePicker();
            }
        });
        stateToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             if(isChecked){
                 setAlarm();
                 alarmSqliteHelper.editToggle(1);
             }
             else{
                 Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
                 pendingIntent = PendingIntent.getBroadcast(getBaseContext(),0,intent,0);

                 if(alarmManager == null){
                     alarmManager = (AlarmManager)   getSystemService(Context.ALARM_SERVICE);
                 }

                 alarmManager.cancel(pendingIntent);
                 Toast.makeText(getBaseContext(), "alarm canceled", Toast.LENGTH_SHORT).show();

             }
            }
        });
//
    }

    private void setControl() {
        stateToggle = findViewById(R.id.alarm_toggle_state);
//        save=findViewById(R.id.alarm_btnSave);
//        cancel=findViewById(R.id.alarm_btnCancel);
        pickTime=findViewById(R.id.alarm_btnTimePickerButton);
        txt_gio = findViewById(R.id.alarm_txt_gio);
        txtphut = findViewById(R.id.alarm_txt_phut);

        RecyclerView rcl=(RecyclerView)findViewById(R.id.alarm_rclButton);
        // tao ra mot doi tuong adapter
       adapter = new DayButtonAdapter(getBaseContext(),dayItems);
        //manager de custom hien thi len recycle view
        LinearLayoutManager manager = new GridLayoutManager(getBaseContext(),4);
        //set cac gia tri len recycler
        rcl.setItemAnimator(new DefaultItemAnimator());// Gán hiệu ứng cho Recyclerview
        rcl.setAdapter(adapter);
        rcl.setLayoutManager(manager);
    }

    private void setAlarm() {
        try {


            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, intent, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    alarmManager.INTERVAL_DAY, pendingIntent);
            Toast.makeText(getBaseContext(), "alarm set successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            Toast.makeText(getBaseContext(), "xin ban chon thoi gian truoc khi cai dat", Toast.LENGTH_SHORT).show();
        }

    }

    private void shotTimePicker() {
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("select time you want to learn")
                .build();

        picker.show(getSupportFragmentManager(),"foxandroid");
        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txt_gio.setText(picker.getHour()+"" );
                txtphut.setText(picker.getMinute()+"");
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,picker.getHour());
                calendar.set(Calendar.MINUTE,picker.getMinute());
                calendar.set(Calendar.SECOND,0);
//                calendar.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
                calendar.set(Calendar.MILLISECOND,0);
                int tempCount = 0;
                for (DayItem item:dayItems) {
                    if(item.getStatus()==1){
                        tempCount++;
                        calendar.set(Calendar.DAY_OF_WEEK, item.getId());
                    }
                }
                alarmSqliteHelper.editTime(picker.getHour()+"_"+picker.getMinute());
            if(tempCount==0){
                AlertDialog alertDialog = new AlertDialog.Builder(getBaseContext()).create();
                alertDialog.setTitle("bạn chưa chọn ngày muốn thông báo ");
                alertDialog.setMessage("mac dinh thong bao sẽ không hoạt hoạt đông");

            }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChanel() {
        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.oniichan_2);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes. CONTENT_TYPE_SONIFICATION )
                .setUsage(AudioAttributes. USAGE_NOTIFICATION )
                .build() ;

        CharSequence name = "foxandroidReminder";
        String description = "channel for alarm";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("foxandroid",name,importance);
        channel.setDescription(description);
        channel.enableVibration( true ) ;
        channel.setSound(soundUri,audioAttributes);
        channel.setVibrationPattern( new long []{ 100 , 200 , 300 , 400 , 500 , 400 , 300 , 200 , 400 }) ;
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

    }
}
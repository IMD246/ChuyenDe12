package com.example.myapplication.User.LearnWord.notification.source;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.R;
import com.example.myapplication.User.LearnWord.vocubulary.VocabularyScreen;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, VocabularyScreen.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,i,0);
        Uri sound = Uri. parse (ContentResolver. SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/oniichan_2.mp3" ) ;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"foxandroid")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("learning english notifycation")
                .setContentText("time for learning bruh")
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(PendingIntent.getActivity(context,0
                        ,new Intent(context.getApplicationContext(),VocabularyScreen.class),PendingIntent.FLAG_UPDATE_CURRENT))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        long[] pattern = {100,200,300,400,500,600,700,800,900};
        builder.setVibrate(pattern);
        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getApplicationContext().getPackageName() + "/" + R.raw.oniichan_2);

        builder.setSound(soundUri);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(123,builder.build());
    }
}

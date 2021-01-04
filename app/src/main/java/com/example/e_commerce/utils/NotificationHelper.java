package com.example.e_commerce.utils;

import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.e_commerce.R;

public class NotificationHelper {
    private Context context;
    private static final int id = 100;

    public NotificationHelper (Context context) {
        this.context = context;
    }

    public void tiggerNotification(String title, String body){

        NotificationCompat.Builder builder= new NotificationCompat.Builder(context,utils.id)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(id,builder.build());

    }
}

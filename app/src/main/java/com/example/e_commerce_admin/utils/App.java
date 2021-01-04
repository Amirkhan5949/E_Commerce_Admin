package com.example.e_commerce_admin.utils;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;


import androidx.annotation.RequiresApi;

import com.cloudinary.android.MediaManager;

import ss.com.bannerslider.Slider;

public class App extends Application {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        MediaManager.init(this);
        Slider.init(new PicassoImageLoadingService(this));

         if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
         {
             NotificationChannel channel = new NotificationChannel(utils.id, utils.name, NotificationManager.IMPORTANCE_DEFAULT);
             channel.setDescription(utils.description);
             NotificationManager manager = getSystemService(NotificationManager.class);
             manager.createNotificationChannel(channel);
         }


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }
}

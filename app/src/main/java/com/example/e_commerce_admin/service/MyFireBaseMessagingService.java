package com.example.e_commerce_admin.service;

import androidx.annotation.NonNull;

import com.example.e_commerce_admin.utils.NotificationHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFireBaseMessagingService extends FirebaseMessagingService {



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title=remoteMessage.getNotification().getTitle();
        String body=remoteMessage.getNotification().getBody();

        NotificationHelper helper=new NotificationHelper(this);
        helper.tiggerNotification(title,body);
    }
}

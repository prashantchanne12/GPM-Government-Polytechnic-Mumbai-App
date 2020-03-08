package com.prashantchanne.chatbox;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String messageTitle = remoteMessage.getNotification().getTitle();
        String messageBody = remoteMessage.getNotification().getBody();

        String messagePayload = remoteMessage.getData().get("message");
        String messageFromPayload = remoteMessage.getData().get("from_user_id");

        String click_action = remoteMessage.getNotification().getClickAction();




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel1 = new NotificationChannel(
                    "channel1",
                    "channel 1",
                    NotificationManager.IMPORTANCE_HIGH);



            channel1.setDescription("Channel 1");
            channel1.enableLights(true);
            channel1.setLightColor(Color.BLUE);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);

            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                    "channel1")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setColor(ContextCompat.getColor(this,R.color.colorPrimary))
                    .setContentTitle(messageTitle)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(messageBody)
                            .setBigContentTitle(messageTitle)
                            )
                    .setContentText(messageBody)
                    .setSound(sound)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);


            int notificationIds = (int) System.currentTimeMillis();
            notificationManager.notify(notificationIds, builder.build());

        } else {

            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setColor(ContextCompat.getColor(this,R.color.colorPrimary))
                            .setContentTitle(messageTitle)
                            .setContentText(messageBody)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(messageBody)
                                    .setBigContentTitle(messageTitle)
                            )
                            .setSound(sound)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_HIGH);

            Intent notificationACtivityIntent = new Intent(click_action);
            notificationACtivityIntent.putExtra("message", messagePayload);
            notificationACtivityIntent.putExtra("from_user_id", messageFromPayload);

            PendingIntent notActivityPendingIntent = PendingIntent.getActivity(this, 0, notificationACtivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(notActivityPendingIntent);

            int notificationId = (int) System.currentTimeMillis();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(notificationId, builder.build());
        }
    }
}

package com.example.ordo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "Ordo_Notification_Channel";
    private static final String CHANNEL_NAME = "Alerte Rutină Ordo";

    @Override
    public void onReceive(Context context, Intent intent) {
        String activityName = intent.getStringExtra("ACTIVITY_NAME");
        String startTime = intent.getStringExtra("START_TIME");

        if (activityName == null) {
            activityName = "Activitate necunoscută";
            startTime = "Timp neprecizat";
        }

        // 1. Creează Notification Manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // 2. Creează Channel (necesar pentru Android Oreo - API 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        // 3. Creează Notificarea
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Schimbă cu o iconiță reală a aplicației, dacă ai una
                .setContentTitle("⏰ Esti Gata? Rutina Începe Acum!")
                .setContentText(activityName + " (La ora: " + startTime + ")")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // ID-ul notificării este unic pentru fiecare alarmă
        int notificationId = (int) System.currentTimeMillis();
        notificationManager.notify(notificationId, builder.build());
    }
}
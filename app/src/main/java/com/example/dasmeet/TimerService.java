package com.example.dasmeet;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.greenrobot.eventbus.EventBus;

public class TimerService extends Service {

    private static final long DURATION_ONE_DAY = 24 * 60 * 60 * 1000; // Duración de un día en milisegundos
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "timer_channel";

    private CountDownTimer countDownTimer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(DURATION_ONE_DAY, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Aquí puedes enviar eventos de broadcast, notificar a la actividad, etc.
                //TimerEvent timerEvent = new TimerEvent(millisUntilFinished);
                EventBus.getDefault().post(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                showNotification();
                stopSelf();
            }
        };

        countDownTimer.start();
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void showNotification() {
        createNotificationChannel();

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                //.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Temporizador de un día")
                .setContentText("El temporizador ha finalizado")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            //notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Timer Channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel for Timer Notifications");
            channel.setLightColor(Color.BLUE);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}

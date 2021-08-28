package com.example.a100daysofcode;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundService extends Service {

    public static final String CHANNEL_ID = "mynotification1";
    public static final String COUNTDOWN_BR = "com.example.a100daysofcode";
    public static final String COUNT = "count";

    Intent localIntent = new Intent(COUNTDOWN_BR);
    CountDownTimer timer = null;
    ArrayList<Problem> problems;

    public static int count;

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences preferences = getSharedPreferences(MainActivity.PREFERENCES, MODE_PRIVATE);
        count = preferences.getInt(COUNT, 0);

        updateNotification(0);
        retrieveData();
        timer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long l) {
                localIntent.setAction("counter");
                localIntent.putExtra("remaining", l);
                sendBroadcast(localIntent);
            }

            @Override
            public void onFinish() {
                count++;
                if (count == 4) {
                    updateData(count);
                    stopSelf();
                } else {
                    saveCount();
                    updateData(count);
                    updateNotification(count);
                    startTimer();
                }
            }
        };
        startTimer();
        return START_NOT_STICKY;
    }

    public void updateData(int index) {
        Problem problem = problems.get(index-1);
        ProblemsDatabase db = new ProblemsDatabase(this);
        db.update(new Problem(problem.getTitle(), problem.getStatement(), problem.getAnswer(), problem.getDifficulty(), 2), index);
    }

    public void retrieveData() {
        ProblemsDatabase database = new ProblemsDatabase(this);
        problems = database.getEveryone(ProblemsDatabase.TABLE);
    }

    public void saveCount() {
        SharedPreferences preferences = getSharedPreferences(MainActivity.PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(COUNT, count);
        editor.apply();
    }

    public void startTimer() {
        timer.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void updateNotification(int day) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent intent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification[] = {new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Day "+String.valueOf(day+1))
                .setContentText("Not Completed")
                .setSmallIcon(R.drawable.ic_ac)
                .setContentIntent(intent)
                .build()
        };

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Counter Service", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        startForeground(1, notification[0]);
    }
}

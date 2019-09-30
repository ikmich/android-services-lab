package org.ikmich.servicelab.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class MainService extends Service {
    private static final long LOOP_DELAY = 1000;
    private String tag = "MainService";

    Timer timer;
    TimerTask timerTask;

    public MainService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    void startAction() {
        timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < 10; i++) {
                    sb.append(i);
                }
                Log.d(tag, sb.toString());
            }
        };

        timer.scheduleAtFixedRate(timerTask, 200, LOOP_DELAY);
    }

    void stopAction() {
        timer.cancel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(tag, "MainService started");
        startAction();
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(tag, "MainService stopped");
        stopAction();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

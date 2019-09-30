package org.ikmich.servicelab.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;

public class BoundService extends Service {
    private final IBinder binder = new LocalBinder();
    private final Random mGenerator = new Random();

    public BoundService() {
    }

    public class LocalBinder extends Binder {
        public BoundService getService() {
            // Return this instance of LocalService so clients can call public methods
            return BoundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * method for clients
     */
    public int getRandomNumber() {
        return mGenerator.nextInt(100);
    }
}

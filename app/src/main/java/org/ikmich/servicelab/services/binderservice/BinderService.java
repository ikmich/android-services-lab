package org.ikmich.servicelab.services.binderservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;

public class BinderService extends Service implements Api {
    private final IBinder binder = new LocalBinder();
    private final Random mGenerator = new Random();

    public BinderService() {
    }

    public class LocalBinder extends Binder {
        public Api getApi() {
            // Return this instance of LocalService so clients can call public methods
            // Or, return an api interface that this service implements
            return BinderService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * method for clients
     */
    @Override
    public int getRandomNumber() {
        return mGenerator.nextInt(100);
    }
}

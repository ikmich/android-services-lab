package org.ikmich.servicelab;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import org.ikmich.servicelab.databinding.ActivityMainBinding;
import org.ikmich.servicelab.services.binderservice.Api;
import org.ikmich.servicelab.services.binderservice.BinderService;

class IBinderServiceHelper implements LifecycleObserver {

    private AppCompatActivity activity;
    private boolean mBound;
    private Api serviceApi;
    private ActivityMainBinding bnd;

    IBinderServiceHelper(MainActivity activity) {
        this.activity = activity;
        this.bnd = activity.bnd;
        activity.getLifecycle().addObserver(this);
    }

    void init() {
        bnd.btnBindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doBind();
            }
        });

        bnd.btnUnbindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBound) {
                    activity.unbindService(connection);
                    mBound = false;
                }
            }
        });

        bnd.btnCallBoundServiceWithBinderMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBound) {
                    int randomNumber = serviceApi.getRandomNumber();
                    Toast.makeText(activity, "Result: " + randomNumber, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void doBind() {
        Intent intent = new Intent(activity, BinderService.class);
        activity.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BinderService.LocalBinder binder = (BinderService.LocalBinder) iBinder;
            serviceApi = binder.getApi();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        doBind();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        activity.unbindService(connection);
        mBound = false;
    }
}

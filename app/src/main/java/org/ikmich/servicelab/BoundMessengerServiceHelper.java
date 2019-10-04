package org.ikmich.servicelab;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import org.ikmich.servicelab.databinding.ActivityMainBinding;
import org.ikmich.servicelab.services.messengerservice.Api;
import org.ikmich.servicelab.services.messengerservice.MessengerService;

import java.lang.ref.WeakReference;

class BoundMessengerServiceHelper implements LifecycleObserver {
    private AppCompatActivity activity;
    private ActivityMainBinding bnd;

    private Messenger mServiceMessenger = null;
    private Messenger replyMessenger;
    private boolean bound;

    BoundMessengerServiceHelper(MainActivity activity) {
        this.activity = activity;
        bnd = activity.bnd;
        activity.getLifecycle().addObserver(this);
        weakRef = new WeakReference<>(this);
    }

    void init() {
        ClickListener clickListener = new ClickListener();
        bnd.btnBindServiceWithMessenger.setOnClickListener(clickListener);
        bnd.btnUnbindServiceWithMessenger.setOnClickListener(clickListener);
        bnd.btnCallBoundServiceWithMessengerMethod.setOnClickListener(clickListener);
    }

    private WeakReference<BoundMessengerServiceHelper> weakRef;

    static class ClientHandler extends Handler {
        WeakReference<BoundMessengerServiceHelper> weakRef;

        ClientHandler(WeakReference<BoundMessengerServiceHelper> weakRef) {
            this.weakRef = weakRef;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case Api.REPLY:
                    Toast.makeText(weakRef.get().activity, "Response received!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mServiceMessenger = new Messenger(iBinder);
            replyMessenger = new Messenger(new ClientHandler(weakRef));
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mServiceMessenger = null;
            bound = false;
        }
    };

    private void callServiceSayHello(View v) {
        if (!bound) return;

        // Create and send a message to the service, using a supported 'what' value
        Message msg = Message.obtain(null, Api.MSG_SAY_HELLO, 0, 0);
        msg.replyTo = replyMessenger;

        try {
            mServiceMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view == bnd.btnBindServiceWithMessenger) {
                doBind();
            } else if (view == bnd.btnUnbindServiceWithMessenger) {
                doUnbind();
            } else if (view == bnd.btnCallBoundServiceWithMessengerMethod) {
                callServiceSayHello(view);
            }
        }
    }

    private void doBind() {
        activity.bindService(new Intent(activity, MessengerService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    private void doUnbind() {
        activity.unbindService(mConnection);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStart() {
        // Bind to the service
        doBind();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop() {
        // Unbind from the service
        if (bound) {
            doUnbind();
            bound = false;
        }
    }
}

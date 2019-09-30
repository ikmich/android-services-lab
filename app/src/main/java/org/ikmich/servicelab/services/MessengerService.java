package org.ikmich.servicelab.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;


public class MessengerService extends Service {
    /**
     * Command to the service to display a message
     */
    public static final int MSG_SAY_HELLO = 1;
    public static final int REPLY = 2;

    /**
     * Handler of incoming messages from clients.
     */
    static class IncomingHandler extends Handler {
        private Context applicationContext;

        IncomingHandler(Context context) {
            applicationContext = context.getApplicationContext();
        }

        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    Toast.makeText(applicationContext, "hello!", Toast.LENGTH_SHORT).show();
                    final Messenger replyTo = msg.replyTo;

                    final Runnable action = new Runnable() {
                        @Override
                        public void run() {
                            Message responseMessage = Message.obtain(IncomingHandler.this, REPLY);
                            Bundle data = new Bundle();
                            data.putString("name", "Ikenna Agbasimalo");
                            responseMessage.setData(data);

                            try {
                                replyTo.send(responseMessage);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    postDelayed(action, 2500);

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    Messenger mMessenger;

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        IncomingHandler incomingHandler = new IncomingHandler(this);
        mMessenger = new Messenger(incomingHandler);
        return mMessenger.getBinder();
    }
}
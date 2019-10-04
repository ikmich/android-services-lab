package org.ikmich.servicelab.services.messengerservice;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

/**
 * Handler of incoming messages from clients.
 */
class IncomingHandler extends Handler implements Api {
    private Context applicationContext;

    IncomingHandler(Context context) {
        applicationContext = context.getApplicationContext();
    }

    @Override
    public void handleMessage(final Message msg) {
        switch (msg.what) {
            case MSG_SAY_HELLO:
                sayHello(msg);
                break;
            default:
                super.handleMessage(msg);
        }
    }

    @Override
    public void sayHello(Message msg) {
        Toast.makeText(applicationContext, "hello!", Toast.LENGTH_SHORT).show();
        final Messenger replyTo = msg.replyTo;

        final Runnable action = new Runnable() {
            @Override
            public void run() {
                // Send response
                Message responseMessage = Message.obtain(null, REPLY, 0, 0);
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

        // Send response after some time
        postDelayed(action, 2500);
    }
}

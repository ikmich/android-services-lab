package org.ikmich.servicelab.services.messengerservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;
import android.widget.Toast;


public class MessengerService extends Service {

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
package org.ikmich.servicelab.services.messengerservice;

import android.os.Message;

public interface Api {
    int MSG_SAY_HELLO = 1;
    int REPLY = 2;

    void sayHello(Message msg);
}

package com.sulca.sulcaw_u8;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by William_ST on 17/12/17.
 */

public class ReceptorSMS extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, ServicioMusica.class);
        context.startService(i);
        NotificationUtil.showNotification(context);
    }

}
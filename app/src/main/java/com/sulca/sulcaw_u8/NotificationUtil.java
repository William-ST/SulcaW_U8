package com.sulca.sulcaw_u8;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

/**
 * Created by William_ST on 17/12/17.
 */

public class NotificationUtil {

    public static final int ID_NOTIFICACION_CREAR = 1;

    public static void showNotification(Context context) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "default")
                .setContentTitle("Creando un servicio de música")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentText("Información adicional")
                .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.jojojo))
                .setVibrate(new long[] { 0,100,200,300 });

        PendingIntent intencionPendiente = PendingIntent
                .getActivity( context, 0, new Intent(context, SecondActivity.class), 0);
        notification.setContentIntent(intencionPendiente);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_NOTIFICACION_CREAR, notification.build());
    }


}

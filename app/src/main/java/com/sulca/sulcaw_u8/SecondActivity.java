package com.sulca.sulcaw_u8;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViewById(R.id.btn_stopmusic).setOnClickListener(this);
        findViewById(R.id.btn_gotomap).setOnClickListener(this);
    }

    public void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NotificationUtil.ID_NOTIFICACION_CREAR);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_stopmusic) {
            Intent i = new Intent(this, ServicioMusica.class);
            stopService(i);
            clearNotification();
        } else if (v.getId() == R.id.btn_gotomap) {
            onBackPressed();
        }
    }
}

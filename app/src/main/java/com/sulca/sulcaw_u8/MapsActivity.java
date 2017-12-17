package com.sulca.sulcaw_u8;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final int SOLICITUD_PERMISO_LOCATION = 10;

    private GoogleMap mMap;
    private LocationManager manejador;
    private String proveedor;
    private DetectCallingBroadCastReceiver broadCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        manejador = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criterio = new Criteria();
        criterio.setCostAllowed(false);
        criterio.setAltitudeRequired(false);
        criterio.setAccuracy(Criteria.ACCURACY_FINE);
        proveedor = manejador.getBestProvider(criterio, true);


        broadCastReceiver = new DetectCallingBroadCastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filtro = new IntentFilter("android.intent.action.PHONE_STATE");
        filtro.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(broadCastReceiver, filtro);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadCastReceiver);
    }

    public class DetectCallingBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) { // Sacamos información de la intención
            String numero = "";
            Bundle extras = intent.getExtras();
            if (extras != null && extras.containsKey(TelephonyManager.EXTRA_INCOMING_NUMBER)) {
                numero = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                if (mMap != null && !TextUtils.isEmpty(numero)) {
                    if (ContextCompat.checkSelfPermission(MapsActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        Location localizacion = manejador.getLastKnownLocation(proveedor);
                        if (localizacion != null) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(localizacion.getLatitude(), localizacion.getLongitude()))
                                    .title(numero));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        initUI();
    }

    public void initUI() {
        checkMapPermission();
    }

    public void checkMapPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            currentPositionMap();
        } else {
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION, "Sin el permiso" +
                    " de posición no se puede posicionar en el mapa.", SOLICITUD_PERMISO_LOCATION, this);
        }
    }

    public static void solicitarPermiso(final String permiso, String justificacion, final int requestCode, final Activity actividad) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad, permiso)) {
            new AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ActivityCompat.requestPermissions(actividad,
                                    new String[]{permiso}, requestCode);
                        }
                    }).show();
        } else {
            ActivityCompat.requestPermissions(actividad, new String[]{permiso}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == SOLICITUD_PERMISO_LOCATION) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                currentPositionMap();
            } else {
                Toast.makeText(this, "Sin el permiso, no puedo realizar la " +
                        "acción", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void currentPositionMap() {
        mMap.setMyLocationEnabled(true);
        Location localizacion = manejador.getLastKnownLocation(proveedor);
        if (localizacion != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(localizacion.getLatitude(), localizacion.getLongitude()), 14));
        } else {
            Toast.makeText(this, "not has lastKnownLocation", Toast.LENGTH_SHORT).show();
        }
    }
}

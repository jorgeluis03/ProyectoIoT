package com.example.proyecto_iot.alumno;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.proyecto_iot.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Delegado_select_map_activity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private Marker selectedLocationMarker;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    // Variable para controlar si se debe mostrar el cuadro de diálogo
    private boolean showInputDialogOnMarkerClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delegado_select_map);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#0A0E19"));
        }

        Log.d("MapaDelegadoActividad", "Iniciando actividad MapaDelegadoActividad");

        // Inicializar el mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.selectmap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Solicitar permiso de ubicación
        requestLocationPermission();
        // Habilitar la capa de ubicación si se tiene permiso
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }

        // Centrar el mapa en una ubicación específica (por ejemplo, una coordenada LatLng)
        LatLng centroDelMapa = new LatLng(-12.073008686469358, -77.08190335245192);

        // Puedes personalizar el marcador para esta ubicación si lo deseas
        MarkerOptions markerOptions = new MarkerOptions()
                .position(centroDelMapa)
                .title("Telecomunicaciones")
                .snippet("PUCP")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        // Agrega el marcador inicial
        googleMap.addMarker(markerOptions);

        // Mueve la cámara para centrarse en la ubicación
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(centroDelMapa));

        // Dentro del método onMapClick
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (selectedLocationMarker != null) {
                    selectedLocationMarker.remove(); // Elimina el marcador anterior si existe
                }

                // Crea un nuevo marcador en la ubicación seleccionada
                selectedLocationMarker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Seleccionar Lugar")
                        .snippet("Haga clic aquí para seleccionar este lugar"));
                selectedLocationMarker.showInfoWindow();

                // Muestra un cuadro de diálogo solo si se establece showInputDialogOnMarkerClick en true
                if (showInputDialogOnMarkerClick) {
                    showInputDialog(latLng);
                    showInputDialogOnMarkerClick = false; // Restablece el valor
                }
            }
        });

        // Cambia el método setOnMarkerClickListener a setOnInfoWindowClickListener
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker.equals(selectedLocationMarker)) {
                    showInputDialog(selectedLocationMarker.getPosition());
                }
            }
        });

        Log.d("MapaDelegadoActividad", "Mapa listo y centrado");
    }

    // Método para mostrar un cuadro de diálogo para ingresar el nombre del lugar
    // Cambia el método showInputDialog para incluir las coordenadas al hacer clic en "Enviar"
    private void showInputDialog(final LatLng latLng) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Indique el nombre de este lugar:");

        // Agrega un campo de entrada de texto al cuadro de diálogo
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String lugarNombre = input.getText().toString();
                if (!lugarNombre.trim().equals("")){
                    Map<String, Object> datosLugar = new HashMap<>();
                    //GeoPoint geoPoint = new GeoPoint(latLng.latitude, latLng.longitude);
                    datosLugar.put("latitud", latLng.latitude);
                    datosLugar.put("longitud", latLng.longitude);
                    datosLugar.put("nombre", lugarNombre);
                    Log.d("msg-test", "textocheck"+datosLugar.get("nombre").toString());
                    Intent intent = new Intent();
                    intent.putExtra("lugar", (Serializable) datosLugar);
                    setResult(RESULT_OK, intent);
                    finish();
                }else {
                    Snackbar.make(Delegado_select_map_activity.this.getCurrentFocus(), "Debe ingresar un nombre al lugar.", Snackbar.LENGTH_SHORT).show();
                }

            }
        });


        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
        Log.d("MapaDelegadoActividad", "Solicitando permiso de ubicación");
    }
}

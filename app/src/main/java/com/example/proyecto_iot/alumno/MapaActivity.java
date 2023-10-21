package com.example.proyecto_iot.alumno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.proyecto_iot.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Marker miUbicacionMarker;
    private LatLng miUbicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#0A0E19"));
        }

        // Obtener los datos de latitud y longitud pasados desde AlumnoDonacionConsultaActivity
        Intent intent = getIntent();
        double latitudDestino = intent.getDoubleExtra("latitud", 0.0);
        double longitudDestino = intent.getDoubleExtra("longitud", 0.0);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Inicializar el mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);

        // Verificar si tienes permiso para acceder a la ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true); // Habilitar la capa de ubicación
            obtenerUbicacionActual();
        } else {
            requestLocationPermission(); // Solicitar permiso si no está habilitado
        }

        // Obtener los datos de latitud y longitud pasados desde AlumnoDonacionConsultaActivity
        Intent intent = getIntent();
        double latitud = intent.getDoubleExtra("latitud", 0.0);
        double longitud = intent.getDoubleExtra("longitud", 0.0);

        LatLng destino = new LatLng(latitud, longitud);

        // Agregar marcador para el destino
        Marker destinoMarker = googleMap.addMarker(new MarkerOptions()
                .position(destino)
                .title("Destino"));

        // Muestra el título del marcador
        destinoMarker.showInfoWindow();

        // Centrar el mapa en el destino
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(destino));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(19)); // Establece el nivel de zoom

        // Configura el listener para el clic en el marcador
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.equals(destinoMarker)) {
                    abrirVistaRuta(destino);
                }
                return false;
            }
        });

        // Llamar a abrirVistaRuta al abrir la página
        abrirVistaRuta(destino);
    }

    private void abrirVistaRuta(LatLng destino) {
        // Crear un URI para la navegación en Google Maps
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destino.latitude + "," + destino.longitude);

        // Crear un Intent con la acción ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps"); // Establece el paquete de Google Maps

        // Comprueba si la aplicación de Google Maps está instalada en el dispositivo
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            // Lanza el Intent para abrir Google Maps con la ruta de navegación
            startActivity(mapIntent);
        }
    }
    private void obtenerUbicacionActual() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        double latitud = location.getLatitude();
                        double longitud = location.getLongitude();
                        Log.d("UbicacionActual", "Latitud: " + latitud + ", Longitud: " + longitud);

                        // Crear un marcador para representar tu ubicación actual
                        miUbicacion = new LatLng(latitud, longitud);
                        if (miUbicacionMarker != null) {
                            miUbicacionMarker.remove(); // Eliminar el marcador anterior
                        }
                        miUbicacionMarker = googleMap.addMarker(new MarkerOptions()
                                .position(miUbicacion)
                                .title("Mi Ubicación"));

                        // Centrar el mapa en tu ubicación actual con un zoom de 20
                        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miUbicacion, 20));
                    }
                }
            });
        }
    }
    private void obtenerRuta(LatLng origen, LatLng destino) {
        // Obtener la clave de API desde el contexto de la aplicación
        String apiKey = "AIzaSyAfL_QEGRZ8k5faL21rRYl3egNWOe5aTys"; // Reemplaza con tu clave de API
        // Crea una instancia de DirectionsApi y establece tus credenciales de API
        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey(apiKey) // Utiliza la clave de API obtenida desde BuildConfig
                .build();

        // Realiza la solicitud de direcciones
        DirectionsApiRequest request = DirectionsApi.getDirections(geoApiContext,
                origen.latitude + "," + origen.longitude,
                destino.latitude + "," + destino.longitude);

        try {
            DirectionsResult result = request.await();
            // Aquí puedes procesar los resultados y trazar la ruta en el mapa
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para solicitar permiso de ubicación si no está habilitado
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    // Agregar cualquier otro código necesario para el manejo de permisos y otras funcionalidades.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacionActual();
            }
        }
    }
}

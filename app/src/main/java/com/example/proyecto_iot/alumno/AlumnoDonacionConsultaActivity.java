package com.example.proyecto_iot.alumno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.text.style.AbsoluteSizeSpan;


import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.KitRecojo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AlumnoDonacionConsultaActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<KitRecojo> ListRecojoKit = new ArrayList<>();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_donacion_consulta);
        // Cambiar el color de la barra de estado aquí
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#0A0E19"));
        }
        Intent intent = getIntent();
        String nombreDonacion = intent.getStringExtra("nombreDonacion");
        String montoDonacion = intent.getStringExtra("montoDonacion");
        String fechaDonacion = intent.getStringExtra("fechaDonacion");
        String horaDonacion = intent.getStringExtra("horaDonacion");
        String codigoAlumno = intent.getStringExtra("codigoAlumno");
        String rolAlumnoFromDonacion = intent.getStringExtra("rolDonacion");
        String donacionesTotales = intent.getStringExtra("donacionesTotales");


        // Encontrar el TextView
        TextView donacionInfoTextView = findViewById(R.id.donacionInfoTextView);

        // Centrar el texto
        donacionInfoTextView.setGravity(Gravity.CENTER);

        // Crear el texto con diferentes colores y estilos
        SpannableStringBuilder builder = new SpannableStringBuilder();

        String monto = "S/." + montoDonacion + "\n ";
        SpannableString montoSpannable= new SpannableString(monto);
        montoSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#56E846")), 0, monto.length(), 0); // Verde neon
        montoSpannable.setSpan(new AbsoluteSizeSpan(30, true), 0, monto.length(), 0); // 16sp

        String nombre = nombreDonacion;
        SpannableString nombreSpannable = new SpannableString(nombre);
        nombreSpannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, nombre.length(), 0); // Blanco
        nombreSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, nombre.length(), 0); // Negrita
        nombreSpannable.setSpan(new AbsoluteSizeSpan(18, true), 0, nombre.length(), 0); // 24sp

        builder.append(montoSpannable);
        builder.append(nombreSpannable);

        // Asignar el texto coloreado al TextView
        donacionInfoTextView.setText(builder, TextView.BufferType.SPANNABLE);

        TextView fechaTextView = findViewById(R.id.fechaTextView);
        TextView horaTextView = findViewById(R.id.horaTextView);
        fechaTextView.setText(fechaDonacion);
        horaTextView.setText(horaDonacion);

        LinearLayout kitRecojoLayout = findViewById(R.id.KitRecojo);

        Button recogoKitButton = findViewById(R.id.button);
        final AtomicReference<GeoPoint> lugarRef = new AtomicReference<>(); // Variable final tipo AtomicReference

        if ("egresado".equals(rolAlumnoFromDonacion)) {
            if(Double.parseDouble(donacionesTotales) > 100.00) {
                // Cambiar la información de la fecha
                CollectionReference donacionesRef = db.collection("KitRecojo");
                Log.d("FirebaseData", "Código de Alumno: " + codigoAlumno);
                donacionesRef.document(codigoAlumno)
                        .get()
                        .addOnCompleteListener(task -> {
                            Log.d("FirebaseData", "Consulta exitosa");
                            if (task.isSuccessful()) {
                                // Acceder a los datos del documento aquí
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    GeoPoint lugar = document.getGeoPoint("Lugar");
                                    lugarRef.set(lugar);
                                    String estado = document.getString("estado");
                                    Timestamp fechaHora = document.getTimestamp("fechaHora");

                                    // Trabaja con los datos obtenidos
                                    Log.d("FirebaseData", "Lugar: " + lugar.getLatitude() + ", " + lugar.getLongitude());
                                    Log.d("FirebaseData", "Estado: " + estado);
                                    Log.d("FirebaseData", "FechaHora: " + fechaHora.toDate());


                                    if ("no recogido".equals(estado)) {
                                        // Acceder a los TextView por ID
                                        recogoKitButton.setText("No recogido");
                                        TextView fechaRecojoKitTextView = findViewById(R.id.FechaRecojoKit);
                                        // Establecer el valor en el TextView
                                        fechaRecojoKitTextView.setText(fechaHora.toDate().toString());
                                    } else if ("recogido".equals(estado)) {
                                        recogoKitButton.setText("Entregado");
                                        kitRecojoLayout.setVisibility(View.INVISIBLE);
                                    }

                                } else {
                                    Log.d("FirebaseData", "El documento no existe");
                                }
                            } else {
                                Log.d("FirebaseData", "Error al obtener el documento: " + task.getException());
                            }
                        });
            }else if(Double.parseDouble(donacionesTotales) <= 100.00){
                recogoKitButton.setText("NO APLICA");
                kitRecojoLayout.setVisibility(View.INVISIBLE);
            }
        }else {
            recogoKitButton.setText("NO APLICA");
            kitRecojoLayout.setVisibility(View.INVISIBLE);
        }
        Button buttonMaps = findViewById(R.id.ButtonMaps);
        buttonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeoPoint lugar = lugarRef.get(); // Obtener el valor de lugar desde lugarRef
                if (lugar != null) {
                    // Acceder a la latitud y longitud desde lugar
                    double latitud = lugar.getLatitude();
                    double longitud = lugar.getLongitude();
                    // Crear un Intent para abrir la nueva actividad
                    Intent intent = new Intent(AlumnoDonacionConsultaActivity.this, MapaActivity.class);
                    // Pasar los datos de latitud y longitud a la nueva actividad
                    intent.putExtra("latitud", latitud);
                    intent.putExtra("longitud", longitud);
                    startActivity(intent); // Iniciar la nueva actividad
                }
                // Ahora puedes usar latitud y longitud como desees, por ejemplo, abrir un mapa.
                // Puedes agregar aquí la lógica para abrir un mapa o realizar otras acciones.
            }
        });

        Button selectLugar = findViewById(R.id.SelectCoords);
        final AtomicReference<LugarSeleccionado> lugarRef2 = new AtomicReference<>();

        selectLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Botón", "Botón presionado");

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference lugaresRef = db.collection("lugares");

                lugaresRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("Firestore", "Consulta exitosa");
                            List<String> opciones = new ArrayList<>();
                            final List<String> nombres = new ArrayList<>();
                            final List<GeoPoint> lugares = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                GeoPoint lugar = document.getGeoPoint("coordenadas");
                                String nombre = document.getString("nombre");

                                if (lugar != null && nombre != null) {
                                    lugares.add(lugar);
                                    nombres.add(nombre);
                                    opciones.add(nombre);
                                }
                            }

                            if (opciones.isEmpty()) {
                                Log.d("Opciones", "No hay ubicaciones disponibles");
                                // Manejar el caso en el que no hay ubicaciones disponibles
                                // Puedes mostrar un mensaje o tomar otra acción aquí.
                            } else {
                                Log.d("Opciones", "Mostrando cuadro de diálogo");
                                AlertDialog.Builder builder = new AlertDialog.Builder(AlumnoDonacionConsultaActivity.this);
                                builder.setTitle("Seleccionar lugar");
                                builder.setItems(opciones.toArray(new String[0]), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        GeoPoint selectedLocation = lugares.get(which);
                                        String selectedNombre = nombres.get(which);
                                        LugarSeleccionado lugarSeleccionado = new LugarSeleccionado(selectedLocation, selectedNombre);
                                        lugarRef2.set(lugarSeleccionado);

                                        // Cambiar el texto del botón al lugar seleccionado
                                        selectLugar.setText(selectedNombre);

                                        // Puedes realizar acciones adicionales aquí, como mostrar el nombre en la interfaz de usuario.
                                    }
                                });
                                builder.show();
                            }
                        } else {
                            Log.e("Firestore", "Error en la consulta: " + task.getException().getMessage());
                            // Manejar el error si la consulta no fue exitosa.
                        }
                    }
                });
            }
        });
        //Logica de mostrar ruta más cerca
        Button Comollegar = findViewById(R.id.Comollegar);
        Comollegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LugarSeleccionado lugarSeleccionado = lugarRef2.get();
                if (lugarSeleccionado != null) {
                    GeoPoint lugar = lugarSeleccionado.coordenadas;
                    if (lugar != null) {
                        // Ahora puedes acceder a la latitud y longitud desde "lugar"
                        double latitud = lugar.getLatitude();
                        double longitud = lugar.getLongitude();

                        // Crear un Intent para abrir la nueva actividad
                        Intent intent = new Intent(AlumnoDonacionConsultaActivity.this, MapaActivity.class);
                        // Pasar los datos de latitud y longitud a la nueva actividad
                        intent.putExtra("latitud", latitud);
                        intent.putExtra("longitud", longitud);
                        startActivity(intent); // Iniciar la nueva actividad
                    }
                }
                // Puedes agregar aquí la lógica para manejar el caso en el que no se haya seleccionado un lugar.
            }
        });

    }
}
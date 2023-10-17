package com.example.proyecto_iot.alumno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.text.style.AbsoluteSizeSpan;


import com.example.proyecto_iot.R;

public class AlumnoDonacionConsultaActivity extends AppCompatActivity {

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

        // Encontrar el TextView
        TextView donacionInfoTextView = findViewById(R.id.donacionInfoTextView);

        // Centrar el texto
        donacionInfoTextView.setGravity(Gravity.CENTER);

        // Crear el texto con diferentes colores y estilos
        SpannableStringBuilder builder = new SpannableStringBuilder();

        String monto = montoDonacion + "\n ";
        SpannableString montoSpannable= new SpannableString(monto);
        montoSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#56E846")), 0, monto.length(), 0); // Verde neon
        montoSpannable.setSpan(new AbsoluteSizeSpan(28, true), 0, monto.length(), 0); // 16sp

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

    }
}
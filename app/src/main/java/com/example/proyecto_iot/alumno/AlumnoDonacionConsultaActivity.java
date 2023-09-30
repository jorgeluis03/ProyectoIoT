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
        String horaDonacion = intent.getStringExtra("horaDonacion");
        String montoDonacion = intent.getStringExtra("montoDonacion");
        String fechaDonacion = intent.getStringExtra("fechaDonacion");

        // Encontrar el TextView
        TextView donacionInfoTextView = findViewById(R.id.donacionInfoTextView);

// Centrar el texto
        donacionInfoTextView.setGravity(Gravity.CENTER);

// Crear el texto con diferentes colores y estilos
        SpannableStringBuilder builder = new SpannableStringBuilder();

        String monto = montoDonacion + "\n ";
        SpannableString montoSpannable= new SpannableString(monto);
        montoSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#39FF14")), 0, monto.length(), 0); // Verde neon
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


        // Encontrar el TextView para mostrar la información de la donación
        TextView fechaInfoTextView = findViewById(R.id.fechaInfoTextView);

// Centrar el texto
        fechaInfoTextView.setGravity(Gravity.CENTER);

// Crear el texto con diferentes colores y estilos
        SpannableStringBuilder builder2 = new SpannableStringBuilder();

// Formatear la fecha en negrita y color blanco
        String fecha = "Fecha de la donación: " + fechaDonacion + "\n ";
        SpannableString fechaSpannable = new SpannableString(fecha);
        fechaSpannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, fecha.length(), 0); // Blanco
        fechaSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, fecha.length(), 0); // Negrita
        fechaSpannable.setSpan(new AbsoluteSizeSpan(18, true), 0, fecha.length(), 0); // 18sp

// Formatear la hora en negrita y color blanco
        String hora = "Hora de la donación: " + horaDonacion;
        SpannableString horaSpannable = new SpannableString(hora);
        horaSpannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, hora.length(), 0); // Blanco
        horaSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, hora.length(), 0); // Negrita
        horaSpannable.setSpan(new AbsoluteSizeSpan(18, true), 0, hora.length(), 0); // 18sp

// Agregar el texto formateado de fecha y hora al TextView
        builder2.append(fechaSpannable);
        builder2.append("\n"); // Agregar un salto de línea
        builder2.append(horaSpannable);

// Asignar el texto coloreado al TextView
        fechaInfoTextView.setText(builder2, TextView.BufferType.SPANNABLE);

        /*// Encontrar los TextViews
        TextView nombreDonacionTextView = findViewById(R.id.nombreDonacionTextView);
        TextView horaDonacionTextView = findViewById(R.id.horaDonacionTextView);
        TextView montoDonacionTextView = findViewById(R.id.montoDonacionTextView);*/
/*
        // Asignar los datos a los TextViews
        nombreDonacionTextView.setText(nombreDonacion);
        horaDonacionTextView.setText(horaDonacion);
        montoDonacionTextView.setText(montoDonacion);*/
    }
}
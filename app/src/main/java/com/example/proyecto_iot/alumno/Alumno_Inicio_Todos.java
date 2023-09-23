package com.example.proyecto_iot.alumno;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.proyecto_iot.R;

public class Alumno_Inicio_Todos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_inicio_todos);
        // Obteniendo referencias a los TextView
        TextView txtTodos = findViewById(R.id.txtTodos);
        TextView txtPersonal = findViewById(R.id.txtPersonal);
        // Cambiar el texto
        txtTodos.setText("NuevoTextoTodos");
        txtPersonal.setText("NuevoTextoPersonal");
        // Aplicar el subrayado
        txtTodos.setPaintFlags(txtTodos.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtPersonal.setPaintFlags(txtPersonal.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }
}

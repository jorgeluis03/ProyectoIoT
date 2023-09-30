package com.example.proyecto_iot.delegadoGeneral;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.ActivityDgCrearActividadBinding;

public class CrearActividadActivity extends AppCompatActivity {
    ActivityDgCrearActividadBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityDgCrearActividadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbarNuevaactividadesDg;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EditText editText = binding.editTextNombreActividadDg;
        Button button = binding.buttonCrearActDg;
        //editar
        Intent intentRecib = getIntent();
        if(intentRecib!=null){
            toolbar.setTitle("Editar actividad");
            String position = intentRecib.getStringExtra("position");
            String nombreAntiguoAct = intentRecib.getStringExtra("nameActAntiguo");

            editText.setText(nombreAntiguoAct);
            button.setText("Actualizar");

            button.setOnClickListener(view -> {
                Intent intentActualizar = new Intent();
                String nombreActNuevo= editText.getText().toString();
                if(!nombreActNuevo.equals("")){
                    intentActualizar.putExtra("nombreActNuevo",nombreActNuevo);
                    intentActualizar.putExtra("position",position);
                    setResult(RESULT_OK,intentActualizar);
                    Log.d("msg-nuevo",position+' '+nombreActNuevo);
                    finish();
                }else {
                    Toast.makeText(CrearActividadActivity.this,"Debe llenar el campo",Toast.LENGTH_SHORT).show();
                }

            });

            Log.d("msg-antiguo",position+' '+nombreAntiguoAct);

        }


        //Para crear
        button.setOnClickListener(view -> {
            Intent intentCrear = new Intent();
            String nombreActicidad = editText.getText().toString();
            if(!nombreActicidad.equals("")){
                intentCrear.putExtra("nombreActividad",nombreActicidad);
                setResult(RESULT_OK,intentCrear);
                finish();
            }else {
                Toast.makeText(CrearActividadActivity.this,"Debe llenar el campo",Toast.LENGTH_SHORT).show();
            }

        });



    }


    //Flecha para regrasar al inicio
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
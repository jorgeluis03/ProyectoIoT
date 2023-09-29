package com.example.proyecto_iot.delegadoGeneral;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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

        binding.buttonCrearActDg.setOnClickListener(view -> {
            Intent intent = new Intent();
            EditText editText = binding.editTextNombreActividadDg;
            String nombreActicidad = editText.getText().toString();
            if(!nombreActicidad.equals("")){
                intent.putExtra("nombreActividad",nombreActicidad);
                setResult(RESULT_OK,intent);
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
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
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.example.proyecto_iot.delegadoGeneral.entity.ActividadesDao;
import com.example.proyecto_iot.delegadoGeneral.entity.ActividadesDataBase;

public class CrearActividadActivity extends AppCompatActivity {
    ActivityDgCrearActividadBinding binding;
    ActividadesDataBase actividadesDataBase;
    ActividadesDao actividadesDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityDgCrearActividadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Toolbar
        Toolbar toolbar = binding.toolbarNuevaactividadesDg;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //====================================

        actividadesDataBase = ActividadesDataBase.getDataBase(this);
        actividadesDao = actividadesDataBase.actividadesDao();


        EditText editText = binding.editTextNombreActividadDg;
        Button button = binding.buttonCrearActDg;

        //Para crear
        button.setOnClickListener(view -> {

            Intent intentCrear = new Intent();
            String nombreActivi = editText.getText().toString();

            if(!nombreActivi.equals("")){
                intentCrear.putExtra("nombreActividad",nombreActivi);

                Actividades actividad = new Actividades(0,nombreActivi,"abierto");
                actividadesDao.insert(actividad);
                setResult(RESULT_OK,intentCrear);
                Toast.makeText(CrearActividadActivity.this,"Agregado",Toast.LENGTH_SHORT).show();
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
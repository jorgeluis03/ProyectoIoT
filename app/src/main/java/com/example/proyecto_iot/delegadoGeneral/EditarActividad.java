package com.example.proyecto_iot.delegadoGeneral;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.ActivityEditarActividadBinding;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaDelegadosAdapter;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EditarActividad extends AppCompatActivity {
    ActivityEditarActividadBinding binding;
    TextInputLayout nombreAntiguo;
    TextView delegadoAntiguo;
    Alumno user_delegado;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditarActividadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //Toolbar
        Toolbar toolbar = binding.toolbarEditarActividadesDg;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //====================================


        Actividades actividadAEditar = (Actividades) getIntent().getSerializableExtra("actividadActual");

        nombreAntiguo = binding.textFieldNombreActividadAntiguo;
        relativeLayout = binding.rlAsig;

        nombreAntiguo.getEditText().setText(actividadAEditar.getNombre());
        delegadoAntiguo = binding.tvnombreDelegado;
        delegadoAntiguo.setText(actividadAEditar.getDelegadoActividad().getNombre()+' '+actividadAEditar.getDelegadoActividad().getApellidos());



        relativeLayout.setOnClickListener(view -> {
            Intent intent = new Intent(this, DgAsignarDelegadoActivity.class);
            launcher.launch(intent);
        });


        //Butom para actualizar
        binding.buttonEditarActDg.setOnClickListener(view -> {
            String nombreActividad = nombreAntiguo.getEditText().getText().toString();
            Actividades actividad = new Actividades();

            if( !nombreActividad.equals("") && user_delegado!=null){
                Intent intent = new Intent();
                actividad.setNombre(nombreActividad);
                actividad.setEstado("abierto");
                actividad.setId(actividadAEditar.getId());

                intent.putExtra("nombreActividad",actividad);
                intent.putExtra("delegado",user_delegado);
                setResult(RESULT_OK,intent);
                finish();
            }else {
                Toast.makeText(this,"Complete los campos",Toast.LENGTH_SHORT).show();
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
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent resultData = result.getData();
            if(resultData!=null){
                user_delegado = (Alumno) resultData.getSerializableExtra("delegado");
                delegadoAntiguo.setText(user_delegado.getNombre()+' '+user_delegado.getApellidos());
            }
        }
    });
}
package com.example.proyecto_iot.delegadoGeneral;

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
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.ActivityDgCrearActividadBinding;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaActividadesAdapter;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaDelegadosAdapter;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.example.proyecto_iot.delegadoGeneral.entity.ActividadesDao;
import com.example.proyecto_iot.delegadoGeneral.entity.Usuario;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CrearActividadActivity extends AppCompatActivity {
    ActivityDgCrearActividadBinding binding;
    EditText editTextNombreDelegado;
    List<Usuario> listaAct;
    private static  boolean delegadosCargados = false;
    Usuario user_delegado;
    RecyclerView recyclerView;
    FirebaseFirestore db;
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


        binding.buttonCrearActDg.setOnClickListener(view -> {
            String nombreActividad = binding.textFieldNombreActividad.getEditText().getText().toString();
            Actividades actividad = new Actividades();


            if( !nombreActividad.equals("") && user_delegado!=null){
                Intent intent = new Intent();
                actividad.setNombre(nombreActividad);
                actividad.setEstado("abierto");
                intent.putExtra("nombreActividad",actividad);
                intent.putExtra("delegado",user_delegado);
                setResult(RESULT_OK,intent);
                finish();
            }else {
                Toast.makeText(this,"Complete los campos",Toast.LENGTH_SHORT).show();
            }


        });

        editTextNombreDelegado = binding.editTextNombreDelegado;
        editTextNombreDelegado.setOnClickListener(view -> {
            showDialog();
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

    public void showDialog() {


        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.buttomsheetlayout_dg);

        db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .whereEqualTo("estado","activo")
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        listaAct = new ArrayList<>(); // Inicializa la lista

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Usuario usuario = document.toObject(Usuario.class);
                            listaAct.add(usuario);
                        }

                        ListaDelegadosAdapter adapter = new ListaDelegadosAdapter();
                        adapter.setContext(this);
                        adapter.setListaUsuarios(listaAct);
                        adapter.setOnItemClickListener(new ListaDelegadosAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Usuario usuario) {
                                user_delegado = usuario;
                                editTextNombreDelegado.setText(usuario.getNombre()+' '+usuario.getApellido()); // Actualiza el EditText con el nombre del usuario seleccionado
                                dialog.dismiss(); // Cierra el diálogo después de la selección

                            }
                        });

                        recyclerView = dialog.findViewById(R.id.recycleViewAsignarDelegado);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));

                        delegadosCargados = true; // Marca las delegados como cargadas


                    }
                    dialog.show();
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    dialog.getWindow().setGravity(Gravity.BOTTOM);

                });






    }

}

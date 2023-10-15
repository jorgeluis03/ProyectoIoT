package com.example.proyecto_iot.alumno.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Donacion;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaDonacionesAdapter;
import com.example.proyecto_iot.databinding.FragmentAlumnoDonacionesBinding;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AlumnoDonacionesFragment extends Fragment {

    FragmentAlumnoDonacionesBinding binding;
    ArrayList<Donacion> donationList = new ArrayList<>();
    ActivityResultLauncher<Intent> resultLauncher;
    Button buttonSubirImagen;
    ListaDonacionesAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoDonacionesBinding.inflate(inflater, container, false);

        // Configura la referencia a la base de datos Firebase
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference donacionesRef = databaseRef.child("donaciones");
        donacionesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Limpia la lista actual de donaciones
                donationList.clear();

                for (DataSnapshot donacionSnapshot : dataSnapshot.getChildren()) {
                    // Accede a los datos de cada donación
                    String fecha = donacionSnapshot.child("fecha").getValue(String.class);
                    String hora = donacionSnapshot.child("hora").getValue(String.class);
                    int monto = donacionSnapshot.child("monto").getValue(Integer.class);
                    String nombre = donacionSnapshot.child("nombre").getValue(String.class);

                    // Crea objetos Donacion y agrégalos a la lista
                    Donacion donacion = new Donacion(nombre, hora, "S/" + monto, fecha);
                    donationList.add(donacion);
                }

                // Notifica al adaptador que los datos han cambiado
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Maneja errores de lectura desde Firebase aquí
            }
        });
        // Configura el adaptador
        adapter = new ListaDonacionesAdapter(getContext(), donationList, donacion -> {
            // Aquí puedes manejar lo que sucede cuando se hace clic en una donación
        });

        // Configura el RecyclerView
        binding.rvDonaciones.setAdapter(adapter);
        binding.rvDonaciones.setLayoutManager(new LinearLayoutManager(getContext()));

        Bundle bundle = new Bundle();
        bundle.putString("header", "Donaciones");
        getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentDonacionesHeader, AlumnoHeader2Fragment.class, bundle)
                .commit();

        binding.buttonDonacionesYape.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
            View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_donacion_cuenta, (LinearLayout) view.findViewById(R.id.dialogCuentaContainer));
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });

        registerResult();
        binding.buttonDonar.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
            View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_donar, (ConstraintLayout) view.findViewById(R.id.bottomSheetContainer));

            // conf de botones de dialog donar
            bottomSheetView.findViewById(R.id.buttonDialogDonar).setOnClickListener(viewDialog -> {
                Toast.makeText(getContext(), "pipipi", Toast.LENGTH_SHORT).show();
            });

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();

            buttonSubirImagen = bottomSheetView.findViewById(R.id.buttonSubirImagen);
            buttonSubirImagen.setOnClickListener(viewDialog -> {
                pickImage();
            });
        });
        return binding.getRoot();
    }

    public void pickImage(){
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }
    private void registerResult(){
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    try {
                        Uri uri = result.getData().getData(); // data de imagen
                        buttonSubirImagen.setText(getImageName(uri, getContext()));
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), "Ocurrió un error", Toast.LENGTH_SHORT).show();
                        Log.d("msg-test", e.getMessage());
                    }
                }
        );
    }

    String getImageName(Uri uri, Context context){
        String res = null;
        if (uri.getScheme().equals("content")){
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()){
                    res = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
            finally {
                cursor.close();
            }
            if (res == null){
                res = uri.getPath();
                int cutt = res.lastIndexOf("/");
                if (cutt != -1){
                    res = res.substring(cutt+1);
                }
            }
        }
        return res;
    }
}

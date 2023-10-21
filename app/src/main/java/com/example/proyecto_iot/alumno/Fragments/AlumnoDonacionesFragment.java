package com.example.proyecto_iot.alumno.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

        // Accede al código de alumno desde el JSON en la memoria interna
        String codigoAlumno = obtenerCodigoAlumnoDesdeMemoria();
        // Configura la referencia a Cloud Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference donacionesRef = db.collection("donaciones");
        Log.d("FirebaseData", "Código de Alumno: " + codigoAlumno);
        donacionesRef.document(codigoAlumno).collection("id")
                .get()
                .addOnCompleteListener(task -> {
                    Log.d("FirebaseData", "Consulta exitosa"); // Agrega este mensaje de depuración
                    if (task.isSuccessful()) {
                        Log.d("FirebaseData", "Consulta exitosa en la colección 'donaciones x2'"); // Agrega este mensaje de depuración
                        for (QueryDocumentSnapshot idDocument : task.getResult()) {
                            String fecha = idDocument.getString("fecha");
                            String hora = idDocument.getString("hora");
                            int monto = idDocument.getLong("monto").intValue();
                            String nombre = idDocument.getString("nombre");
                            String rol = idDocument.getString("rol");
                            // Realiza operaciones con los campos obtenidos
                            Donacion donacion = new Donacion(nombre, hora, "S/" + monto, fecha, rol);
                            donationList.add(donacion);
                            // Agrega mensajes de depuración para verificar los datos
                            Log.d("FirebaseData", "Fecha: " + fecha);
                            Log.d("FirebaseData", "Hora: " + hora);
                            Log.d("FirebaseData", "Monto: " + monto);
                            Log.d("FirebaseData", "Nombre: " + nombre);
                            Log.d("FirebaseData", "Rol: " + rol);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("FirebaseData", "Error al obtener datos de Firestore: " + task.getException());
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
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                TextView textView = view.findViewById(R.id.textView19);
                textView.setText("Donaciones");

                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
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
    private String obtenerCodigoAlumnoDesdeMemoria() {
        // Abre el archivo JSON almacenado en la memoria interna
        try {
            FileInputStream fileInputStream = requireContext().openFileInput("userData");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            // Parsea el JSON y obtén el campo "codigo" del alumno
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            return jsonObject.getString("codigo");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            // Maneja errores aquí
            return ""; // O devuelve un valor predeterminado en caso de error
        }
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

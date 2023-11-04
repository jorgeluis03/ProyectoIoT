package com.example.proyecto_iot.alumno.Fragments;

import static java.util.Collections.*;

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
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.alumno.Entities.Donacion;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaDonacionesAdapter;
import com.example.proyecto_iot.databinding.FragmentAlumnoDonacionesBinding;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AlumnoDonacionesFragment extends Fragment {

    FragmentAlumnoDonacionesBinding binding;
    ArrayList<Donacion> donationList = new ArrayList<>();
    ActivityResultLauncher<Intent> resultLauncher;
    Button buttonSubirImagen;
    ListaDonacionesAdapter adapter;
    private String codigoAlumno;
    private FirebaseStorage storage; // storage
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private BottomSheetDialog bottomSheetDialog;
    private Uri uriFotoDonacion;
    private String urlNuevoDonacionCaptura;
    private float monto;
    private boolean fotoAgregada = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoDonacionesBinding.inflate(inflater, container, false);

        Log.d("msg-test", "recargado");

        // Accede al código de alumno desde el JSON en la memoria interna
        codigoAlumno = obtenerCodigoAlumnoDesdeMemoria();

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
                            Double monto = idDocument.getDouble("monto");
                            String monto_enviar = String.valueOf(monto);
                            String nombre = idDocument.getString("nombre");
                            String rol = idDocument.getString("rol");
                            String fotoQR = idDocument.getString("fotoQR");
                            // Realiza operaciones con los campos obtenidos
                            Donacion donacion = new Donacion(fecha, hora, rol, fotoQR, monto_enviar, nombre);
                            donationList.add(donacion);
                            // Agrega mensajes de depuración para verificar los datos
                            Log.d("FirebaseData", "Fecha: " + fecha);
                            Log.d("FirebaseData", "Hora: " + hora);
                            Log.d("FirebaseData", "Monto: " + monto);
                            Log.d("FirebaseData", "Nombre: " + nombre);
                            Log.d("FirebaseData", "Rol: " + rol);
                        }
// Aplica la lógica de ordenamiento aquí
                        donationList.sort(new Comparator<Donacion>() {
                            @Override
                            public int compare(Donacion donacion1, Donacion donacion2) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES"));                                try {
                                    Date date1 = dateFormat.parse(donacion1.getFecha());
                                    Date date2 = dateFormat.parse(donacion2.getFecha());
                                    int comparisonResult = date2.compareTo(date1); // Ordena de más reciente a más antigua
                                    return comparisonResult;
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    return 0;
                                }
                            }
                        });

                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("FirebaseData", "Error al obtener datos de Firestore: " + task.getException());
                    }
                });

        // Configura el adaptador
        adapter = new ListaDonacionesAdapter(getContext(), donationList, codigoAlumno, donacion -> {
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
            bottomSheetDialog = new BottomSheetDialog(getActivity());
            View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_donar, (ConstraintLayout) view.findViewById(R.id.bottomSheetContainer));

            // conf de botones de dialog donar
            Button buttonDonar = bottomSheetView.findViewById(R.id.buttonDialogDonar);
            buttonDonar.setOnClickListener(viewDialog -> {
                subirDonacion();
            });

            EditText inputMonto = bottomSheetView.findViewById(R.id.inputMonto);
            inputMonto.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (fotoAgregada) {
                        monto = Float.parseFloat(inputMonto.getText().toString());
                        buttonDonar.setEnabled(true);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
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
    /* tambien daba error xd
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

     */

    private void subirDonacion() {

        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) + " hrs";
        String nombre = binding.textCuentaDonacion.getText().toString();
        String rol = obtenerTipoAlumnoDesdeMemoria();

        Donacion donacionNueva = new Donacion();
        donacionNueva.setFecha(fecha);
        donacionNueva.setHora(hora);
        donacionNueva.setNombre(nombre);
        donacionNueva.setRol(rol);
        donacionNueva.setMonto(String.valueOf(monto));

        // guardar imagen en starge para obtener url
        UUID uuidDonacionCaptura = UUID.randomUUID();
        storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("images/donaciones/" + uuidDonacionCaptura.toString() + ".jpg");
        storageReference.putFile(uriFotoDonacion).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Log.d("msg-test", "donacion agregada en storage");
                    urlNuevoDonacionCaptura = task.getResult().toString();
                    Log.d("msg-test", "nueva url: " + urlNuevoDonacionCaptura);
                    donacionNueva.setFotoQR(urlNuevoDonacionCaptura);

                    subirDonacionFirestore(donacionNueva);
                } else {
                    Log.d("msg-test", "error");
                }
            }
        });

    }

    private void subirDonacionFirestore(Donacion donacionNueva) {
        // guardar donacion en firestore
        db.collection("donaciones").document(codigoAlumno).collection("id")
                .add(donacionNueva)
                .addOnSuccessListener(documentReference -> {
                    Log.d("msg-test", "donacion guardada en firestore-donacion guarada exitosamente");
                    //reiniciando fragmento para cargar nueva donacion
                    recargarFragment();
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }

    private void pickImage() {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }

    private void recargarFragment() {
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerViewHost);
        NavController navController = NavHostFragment.findNavController(navHostFragment);
        navController.navigate(R.id.alumnoDonacionesFragment);
        bottomSheetDialog.dismiss();
    }

    private void registerResult() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    try {
                        uriFotoDonacion = result.getData().getData(); // data de imagen
                        buttonSubirImagen.setText(getImageName(uriFotoDonacion));
                        fotoAgregada = true;
                    } catch (Exception e) {
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

    private String obtenerTipoAlumnoDesdeMemoria() {
        try (FileInputStream fileInputStream = getActivity().openFileInput("userData");
             FileReader fileReader = new FileReader(fileInputStream.getFD());
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String jsonData = bufferedReader.readLine();
            Gson gson = new Gson();
            Alumno alumno = gson.fromJson(jsonData, Alumno.class);
            return alumno.getTipo();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getImageName(Uri uri) {
        return DocumentFile.fromSingleUri(getContext(), uri).getName();
    }
}

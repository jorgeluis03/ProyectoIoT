package com.example.proyecto_iot.alumno.Fragments;

import static java.util.Collections.*;

import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.alumno.Entities.Donacion;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaDonacionesAdapter;
import com.example.proyecto_iot.alumno.Utils.DecimalDigitsInputFilter;
import com.example.proyecto_iot.databinding.FragmentAlumnoDonacionesBinding;

import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseFCMUtils;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
    private Float monto;
    private boolean fotoAgregada = false;
    private Button buttonRegistrarDonacion;
    private final double OBJETIVO_KIT_TELECO = 100.0;
    private String rolUsuario;

    private ProgressBar progressBarRegistrarDonacion;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoDonacionesBinding.inflate(inflater, container, false);
        rolUsuario = obtenerTipoAlumnoDesdeMemoria();

        // Accede al código de alumno desde el JSON en la memoria interna
        codigoAlumno = obtenerCodigoAlumnoDesdeMemoria();


        CollectionReference donacionesRef = db.collection("donaciones");
        donacionesRef.document(codigoAlumno).collection("id").orderBy("timestamp", Query.Direction.DESCENDING)
                        .addSnapshotListener(((value, error) -> {
                            if (error != null){
                                Log.d("msg-test", "Listen failed in donaciones fragment", error);
                                Toast.makeText(getContext(), "Ocurrió un error al recuperar las donaciones", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (value != null && !value.isEmpty()){
                                binding.textNoDonaciones.setVisibility(View.GONE);
                                donationList.clear();
                                double donacionesTotalesValidadas = 0.0;
                                for (QueryDocumentSnapshot doc: value){
                                    String fecha = doc.getString("fecha");
                                    String hora = doc.getString("hora");
                                    String monto = doc.getString("monto");
                                    String monto_enviar = monto;
                                    String nombre = doc.getString("nombre");
                                    String rol = doc.getString("rol");
                                    String fotoQR = doc.getString("fotoQR");
                                    String estado = doc.getString("estado");
                                    Log.d("msg-test", "timestamp: "+doc.get("timestamp"));

                                    if ("validado".equals(estado)) {
                                        String montoString = doc.getString("monto");
                                        try {
                                            double montoValidado = Double.parseDouble(montoString);
                                            donacionesTotalesValidadas += montoValidado;
                                        } catch (NumberFormatException e) {
                                            Log.e("Firestore", "Error al parsear el monto a double", e);
                                        }
                                    }
                                    if (rolUsuario.isEmpty()) {
                                        rolUsuario = doc.getString("rol");
                                    }
                                    // Realiza operaciones con los campos obtenidos
                                    Donacion donacion = new Donacion(fecha, hora, rol, fotoQR, monto_enviar, nombre, estado);
                                    donationList.add(donacion);
                                }

                                adapter.setDonacionesTotalesValidadas(donacionesTotalesValidadas);

                                /*
                                // Aplica la lógica de ordenamiento aquí
                                donationList.sort(new Comparator<Donacion>() {
                                    @Override
                                    public int compare(Donacion donacion1, Donacion donacion2) {
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm", new Locale("es", "ES"));
                                        try {
                                            String dateTime1 = donacion1.getFecha() + " " + donacion1.getHora();
                                            String dateTime2 = donacion2.getFecha() + " " + donacion2.getHora();
                                            Date date1 = dateFormat.parse(dateTime1);
                                            Date date2 = dateFormat.parse(dateTime2);
                                            return date2.compareTo(date1); // Ordena de más reciente a más antigua considerando fecha y hora
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                            return 0;
                                        }
                                    }
                                });
                                */

                                // Encontrar la última fecha de donación válida
                                Date lastValidDonationDate = null;
                                for (Donacion donacion : donationList) {
                                    if ("validado".equals(donacion.getEstado())) {
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm", new Locale("es", "ES"));
                                        try {
                                            lastValidDonationDate = dateFormat.parse(donacion.getFecha() + " " + donacion.getHora());
                                            break; // Salir del bucle después de encontrar la primera donación válida
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                if ("Egresado".equals(rolUsuario)) {
                                    verificarYCrearKitRecojoSiEsNecesario(codigoAlumno, donacionesTotalesValidadas, lastValidDonationDate);
                                }
                                adapter.notifyDataSetChanged();
                            }
                            else{
                                donationList.clear();
                                binding.textNoDonaciones.setVisibility(View.VISIBLE);
                            }
                        }));

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
            progressBarRegistrarDonacion = bottomSheetView.findViewById(R.id.progressBarRegistrarDonacion);
            buttonRegistrarDonacion = bottomSheetView.findViewById(R.id.buttonDialogDonar);
            buttonRegistrarDonacion.setOnClickListener(viewDialog -> {
                subirDonacion();
            });

            EditText inputMonto = bottomSheetView.findViewById(R.id.inputMonto);
            inputMonto.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10, 2)});
            inputMonto.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {
                        float montoIngresado = Float.parseFloat(inputMonto.getText().toString());
                        if ("Egresado".equals(rolUsuario) && montoIngresado >= 100) {
                            // Si el usuario es Egresado y el monto ingresado es mayor o igual a 100
                            buttonRegistrarDonacion.setEnabled(true);
                            monto = montoIngresado; // Guardar el monto
                        } else if (!"Egresado".equals(rolUsuario)) {
                            // Si el usuario no es Egresado
                            buttonRegistrarDonacion.setEnabled(true);
                            monto = montoIngresado; // Guardar el monto
                        } else {
                            // Si el usuario es Egresado pero el monto es menor a 100
                            buttonRegistrarDonacion.setEnabled(false);
                        }
                    } catch (NumberFormatException e) {
                        // Si el monto ingresado no es un número válido
                        buttonRegistrarDonacion.setEnabled(false);
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

    private void verificarYCrearKitRecojoSiEsNecesario(String codigoAlumno, double donacionesTotalesValidadas, Date lastDonationDate) {
        db.collection("KitRecojo").document(codigoAlumno).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (!document.exists() && donacionesTotalesValidadas > 100) {
                    crearDocumentoKitRecojo(codigoAlumno, lastDonationDate);
                }
            } else {
                Log.e("Firestore", "Error al verificar en KitRecojo", task.getException());
            }
        });
    }

    private void crearDocumentoKitRecojo(String codigoAlumno, Date lastDonationDate) {
        Calendar calendar = Calendar.getInstance();
        if (lastDonationDate != null) {
            calendar.setTime(lastDonationDate); // Usar la última fecha de donación
        }
        calendar.add(Calendar.WEEK_OF_YEAR, 1); // Añadir una semana
        calendar.set(Calendar.HOUR_OF_DAY, 9);  // Configurar la hora a las 9:00
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);


        // Crear el documento para KitRecojo
        Map<String, Object> kitRecojoData = new HashMap<>();
        kitRecojoData.put("Lugar", new GeoPoint(-12.07314, -77.0815708));
        kitRecojoData.put("estado", "no recogido");
        kitRecojoData.put("fechaHora", new Timestamp(calendar.getTime()));

        // Guardar el documento en Firestore
        db.collection("KitRecojo").document(codigoAlumno)
                .set(kitRecojoData)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Documento KitRecojo creado para " + codigoAlumno))
                .addOnFailureListener(e -> Log.e("Firestore", "Error al crear documento en KitRecojo", e));
    }

    private void subirDonacion() {

        buttonRegistrarDonacion.setEnabled(false);
        progressBarRegistrarDonacion.setVisibility(View.VISIBLE);

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
        donacionNueva.setEstado("por validar");
        donacionNueva.setTimestamp(Timestamp.now());

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

        HashMap<String, String> vacio = new HashMap<>();
        vacio.put("a", "b");

        db.collection("donaciones")
                .document(codigoAlumno)
                .set(vacio)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        // guardar donacion en firestore
                        db.collection("donaciones")
                                .document(codigoAlumno)
                                .collection("id")
                                .add(donacionNueva)
                                .addOnSuccessListener(documentReference -> {
                                    Log.d("msg-test", "donacion guardada en firestore-donacion guarada exitosamente");

                                    //lanzar notificacion para el delago general
                                    enviarNotificacion(donacionNueva);
                                    progressBarRegistrarDonacion.setVisibility(View.GONE);
                                    bottomSheetDialog.dismiss();
                                })
                                .addOnFailureListener(e -> {
                                    e.printStackTrace();
                                });

                    } else {
                        Log.d("msg-test", "error al crear doc donacion: " + task.getException().getMessage());
                    }
                });
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        resultLauncher.launch(intent);
    }

    private void registerResult() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    try {
                        uriFotoDonacion = result.getData().getData(); // data de imagen
                        buttonSubirImagen.setText(getImageName(uriFotoDonacion));
                        fotoAgregada = true;

                        if (monto != null) {
                            buttonRegistrarDonacion.setEnabled(true);
                        }

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

    public void enviarNotificacion(Donacion donacionNueva) {
        //current username, message, currentUserId, otherUserToken
        FirebaseUtilDg.getCollAlumnos().whereEqualTo("codigo", "20200643").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Alumno usuarioDg = task.getResult().toObjects(Alumno.class).get(0);
                try {
                    JSONObject jsonObject = new JSONObject();

                    JSONObject notificationObj = new JSONObject();
                    notificationObj.put("title", "Donación Aitel");
                    notificationObj.put("body", "Ha llegado una nueva donación para Aitel de S/" + donacionNueva.getMonto());

                    jsonObject.put("notification", notificationObj);
                    jsonObject.put("to", usuarioDg.getFcmToken());

                    //llamar a la api
                    FirebaseFCMUtils.callApi(jsonObject);


                } catch (Exception e) {

                }

            }
        });


    }




}

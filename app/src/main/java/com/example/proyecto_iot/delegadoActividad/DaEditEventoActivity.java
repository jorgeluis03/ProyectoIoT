package com.example.proyecto_iot.delegadoActividad;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyecto_iot.AppConstants;
import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.AlumnoEventoActivity;
import com.example.proyecto_iot.alumno.Delegado_select_map_activity;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.alumno.Entities.Chat;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.alumno.Entities.Foto;
import com.example.proyecto_iot.alumno.Utils.FirebaseUtilAl;
import com.example.proyecto_iot.databinding.ActivityDaEditEventoBinding;
import com.example.proyecto_iot.delegadoActividad.Adapters.CarouselAdapter;
import com.example.proyecto_iot.delegadoActividad.Adapters.ListaEventosActividadesAdapter;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.example.proyecto_iot.inicioApp.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DaEditEventoActivity extends AppCompatActivity {
    ActivityDaEditEventoBinding binding;
    Evento eventoGuardar = new Evento();
    boolean isExistEvent;
    boolean changedImage = false;
    private Uri imageUri = null;
    private Uri imagesUri = null;
    Map<String, Object> datoRecibido = new HashMap<>();
    private Evento evento;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage;
    private ArrayList<Actividades> actividadList = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;
    ArrayList<String> listaFinal = new ArrayList<>();
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yy");
    ArrayList<Actividades> actividadesList = new ArrayList<>();
        Actividades currentActividad;
    private static final int ACTIVIDAD_MAPS_REQUEST_CODE = 2;
    private String userUid = FirebaseAuth.getInstance().getUid();
    private CarouselAdapter adapter = new CarouselAdapter();
    ArrayList<CarouselModel> listaFotos = new ArrayList<>();
    CarouselModel carrusel = new CarouselModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDaEditEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonEventoABack.setOnClickListener(view -> mostrarConfirmacionExit());

        Intent intent = getIntent();
        evento = (Evento) intent.getSerializableExtra("evento");
        actividadList = obtenerActividadesDesdeMemoria();
        Actividades actividadName = (Actividades) intent.getSerializableExtra("actividadName");
        if (evento == null){
            isExistEvent = false;
            AutoCompleteTextView autoCompleteTextView = binding.textSelectActividad;
            for (Actividades actividad: actividadList){
                buscarActividad(actividad.getId());
            }
            Log.d("msg-test", String.valueOf(listaFinal.size()));
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, listaFinal);
            autoCompleteTextView.setAdapter(adapter);

            if (actividadName != null){
                binding.textSelectActividad.setText(actividadName.getNombre());
                binding.textSelectActividad.setEnabled(false);
                binding.textSubtitleEventEdition.setText("Actividad: "+actividadName.getNombre());
            }

            binding.buttonFinishEvent.setVisibility(View.GONE);
            binding.textHeaderEventEdition.setText("Crear evento");
            binding.buttonSaveChangeEvent.setText("Crear evento");
        }else {
            isExistEvent = true;
            binding.textTitleEvent.setText(evento.getTitulo());
            binding.textTitleEvent.setEnabled(false);
            binding.selectActividad.setVisibility(View.GONE);
            binding.textDescripEvent.setText(evento.getDescripcion());
            binding.textSubtitleEventEdition.setText("Actividad: "+evento.getActividad());
            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(DaEditEventoActivity.this)
                    .load(evento.getFotoUrl())
                    .apply(requestOptions)
                    .into(binding.imageView10);
            binding.textHourEvent.setText(evento.getHora());
            binding.textDateEvent.setText(evento.getFecha());
            binding.textPlaceEvent.setText(evento.getLugar());
        }

        long today = MaterialDatePicker.todayInUtcMilliseconds();
        CalendarConstraints.Builder constraintsBuilder =
                new CalendarConstraints.Builder()
                        .setFirstDayOfWeek(Calendar.MONDAY)
                        .setStart(today)
                        .setValidator(DateValidatorPointForward.now());

        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTitleText("Selecciona la hora del evento")
                .build();

        //TODO DA*: solucionar error del inputText from DatePicker
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecciona una fecha")
                .setTextInputFormat(formato)
                .setCalendarConstraints(constraintsBuilder.build())
                .build();

        binding.textDateEvent.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mostrarDatePicker(datePicker);
            }
        });
        binding.textDateEvent.setOnClickListener(view -> {
            mostrarDatePicker(datePicker);
        });
        binding.inputDateEvent.setEndIconOnClickListener(view -> {
            mostrarDatePicker(datePicker);
        });

        binding.textHourEvent.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus){
                mostrarTimePicker(timePicker);
            }
        });
        binding.textHourEvent.setOnClickListener(view -> {
            mostrarTimePicker(timePicker);
        });
        binding.inputHourEvent.setEndIconOnClickListener(view -> {
            mostrarTimePicker(timePicker);
        });
        binding.textPlaceEvent.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus){
                Intent intent1 = new Intent(DaEditEventoActivity.this, Delegado_select_map_activity.class);
                Log.d("msg-test", "Iniciando MapaDelegadoActividad"); // Agregar este log
                startActivityForResult(intent1, ACTIVIDAD_MAPS_REQUEST_CODE);
            }
        });
        binding.textPlaceEvent.setOnClickListener(view -> {
            Intent intent1 = new Intent(DaEditEventoActivity.this, Delegado_select_map_activity.class);
            Log.d("msg-test", "Iniciando MapaDelegadoActividad"); // Agregar este log
            startActivityForResult(intent1, ACTIVIDAD_MAPS_REQUEST_CODE);
        });
        binding.inputPlaceEvent.setEndIconOnClickListener(view -> {
            Intent intent1 = new Intent(DaEditEventoActivity.this, Delegado_select_map_activity.class);
            Log.d("msg-test", "Iniciando MapaDelegadoActividad"); // Agregar este log
            startActivityForResult(intent1, ACTIVIDAD_MAPS_REQUEST_CODE);
        });
        binding.textTitleEvent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().equals("")){
                    validarCompleteForm(evento);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().equals("")){
                    validarCompleteForm(evento);
                }
            }
        });
        binding.textDescripEvent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().equals("")){
                    if (!isExistEvent){
                        validarCompleteForm(evento);
                    }else {
                        if (!charSequence.toString().equals(evento.getDescripcion())){
                            validarChangeForm(evento);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().equals("")){
                    if (!isExistEvent){
                        validarCompleteForm(evento);
                    }else {
                        if (!editable.toString().equals(evento.getTitulo())){
                            validarChangeForm(evento);
                        }
                    }
                }
            }
        });
        binding.subirFoto.setOnClickListener(view -> {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            openImageLauncher.launch(galleryIntent);
        });

        binding.buttonSaveChangeEvent.setOnClickListener(view -> {
            if (binding.buttonSaveChangeEvent.isEnabled()){
                binding.buttonSaveChangeEvent.setEnabled(false);
                if (isExistEvent){
                    Map<String, Object> eventoUpdate = new HashMap<>();
                    if (datoRecibido.get("nombre")!=null){
                        guardarLugar(datoRecibido);
                        eventoUpdate.put("lugar", binding.textPlaceEvent.getText().toString());
                    }
                    if (!binding.textDescripEvent.getText().toString().equals(evento.getDescripcion())){
                        eventoUpdate.put("descripcion", binding.textDescripEvent.getText().toString());
                    }
                    if (!binding.textDateEvent.getText().toString().equals(evento.getFecha())){
                        eventoUpdate.put("fecha", binding.textDateEvent.getText().toString());
                    }
                    if (!binding.textHourEvent.getText().toString().equals(evento.getHora())){
                        eventoUpdate.put("hora", binding.textHourEvent.getText().toString());
                    }
                    subirUpdateEventoFirestore(eventoUpdate);
                    if (imageUri!=null){
                        subirFoto(imageUri, evento.getFechaHoraCreacion().toString());
                    }
                    finish();
                }else {
                    for (Actividades a: actividadesList){
                        if (a.getNombre().equals(binding.textSelectActividad.getText().toString())){
                            currentActividad = a;
                            break;
                        }
                    }

                    String chatID = "evento"+Date.from(Instant.now());
                    crearChat(chatID);

                    guardarLugar(datoRecibido);
                    eventoGuardar.setActividad(currentActividad.getNombre());
                    eventoGuardar.setActividadId(currentActividad.getId());
                    eventoGuardar.setDelegado(userUid);
                    eventoGuardar.setChatID(chatID);
                    eventoGuardar.setDescripcion(binding.textDescripEvent.getText().toString());
                    eventoGuardar.setEstado("activo");
                    eventoGuardar.setFecha(binding.textDateEvent.getText().toString());
                    eventoGuardar.setFechaHoraCreacion(Date.from(Instant.now()));
                    eventoGuardar.setHora(binding.textHourEvent.getText().toString());
                    eventoGuardar.setTitulo(binding.textTitleEvent.getText().toString());
                    eventoGuardar.setLugar(binding.textPlaceEvent.getText().toString());
                    eventoGuardar.setFotoUrl("");

                    subirNuevoEventoFirestore();
                    subirFoto(imageUri, eventoGuardar.getFechaHoraCreacion().toString());
                }
            }else {
                String mensaje;
                if (isExistEvent){
                    mensaje = "Debe modificar la informaciónde forma válida para poder actualizar el evento";
                }
                else {
                    mensaje = "Debe completar los campos para crear el evento";
                }
                Snackbar.make(binding.getRoot(),mensaje, Snackbar.LENGTH_SHORT).show();
            }
        });

        binding.buttonFinishEvent.setOnClickListener(view -> {
            bottomSheetDialog = new BottomSheetDialog(DaEditEventoActivity.this);
            View bottomSheetView = LayoutInflater.from(DaEditEventoActivity.this).inflate(R.layout.dialog_da_finalizar_evento, (ConstraintLayout) findViewById(R.id.bottomSheetFinishEvent));
            Button finishBtn = bottomSheetView.findViewById(R.id.buttonDialogFinish);
            ProgressBar progressBar = bottomSheetView.findViewById(R.id.progressBar4);
            Button upload = bottomSheetView.findViewById(R.id.buttonSubirImagenEvento);
            ImageButton uploadImage = bottomSheetView.findViewById(R.id.imageButton);
            finishBtn.setOnClickListener(view1 -> {
                finishBtn.setText("");
                finishBtn.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                db.collection("actividades").document(evento.getActividadId()).collection("eventos")
                        .document("evento"+evento.getFechaHoraCreacion())
                        .update("estado","inactivo")
                        .addOnSuccessListener(unused -> {
                            db.collection("eventos").document("evento"+evento.getFechaHoraCreacion().toString())
                                    .update("estado", "inactivo")
                                    .addOnSuccessListener(unused1 -> {
                                        Snackbar.make(binding.getRoot(),"Se finalizó el evento exitosamente", Snackbar.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Snackbar.make(binding.getRoot(),"Ocurrió un error.", Snackbar.LENGTH_SHORT).show();
                                    });
                        })
                        .addOnFailureListener(e -> {
                            Snackbar.make(binding.getRoot(),"Ocurrió un error.", Snackbar.LENGTH_SHORT).show();
                        });
                finish();
            });
            upload.setOnClickListener(view1 -> {
                bottomSheetDialog.dismiss();
                Intent finishIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                finishIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                finishIntent.addCategory(Intent.CATEGORY_OPENABLE);
                finishIntent.setType("image/*");
                finishIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                openManyImageLauncher.launch(finishIntent);
            });
            uploadImage.setOnClickListener(view1 -> {
                bottomSheetDialog.dismiss();
                Intent finishIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                finishIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                finishIntent.addCategory(Intent.CATEGORY_OPENABLE);
                finishIntent.setType("image/*");
                finishIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                openManyImageLauncher.launch(finishIntent);
            });
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });
    }
    private ActivityResultLauncher<Intent> openImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    imageUri = result.getData().getData();
                    abrirDialogSubirFoto();
                }
            }
    );
    private ActivityResultLauncher<Intent> openManyImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Log.d("msg-test","ok result");
                    Log.d("msg-test","result.getData: "+ (result.getData() != null) + (result.getData().getClipData() != null));
                    if (result.getData() != null) {
                        ClipData clipData = result.getData().getClipData();
                        Log.d("msg-test","hay info");
                        listaFotos = new ArrayList<>();

                        if (clipData != null) {
                            Log.d("msg-test","es clipdata");
                            for (int i = 0; i < Math.min(clipData.getItemCount(),5); i++) {
                                carrusel = new CarouselModel();
                                imageUri = clipData.getItemAt(i).getUri();
                                carrusel.setImageUri(imageUri);
                                listaFotos.add(carrusel);
                                Log.d("msg-test", "caso 1:"+listaFotos.size());
                                Log.d("msg-test", "caso 1:"+(carrusel.getImageUri()!=null));
                            }
                        } else {
                            // Solo se seleccionó una imagen
                            Log.d("msg-test", "Una foto");
                            carrusel = new CarouselModel();
                            imageUri = result.getData().getData();
                            carrusel.setImageUri(imageUri);
                            listaFotos.add(carrusel);
                        }
                    }
                    abrirDialogFinishFinish();
                }
            }
    );

    private void guardarLugar(Map<String, Object> datoRecibido) {
        CollectionReference lugaresRef = FirebaseFirestore.getInstance().collection("lugares");
        DocumentReference lugarDocumento = lugaresRef.document(datoRecibido.get("nombre").toString());
        Map<String, Object> datosLugar = new HashMap<>();
        GeoPoint geoPoint = new GeoPoint(Double.parseDouble(datoRecibido.get("latitud").toString()), Double.parseDouble(datoRecibido.get("longitud").toString()));
        datosLugar.put("coordenadas", geoPoint);

        datosLugar.put("nombre", datoRecibido.get("nombre").toString());

        lugarDocumento.set(datosLugar)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }


    private void validarChangeForm(Evento evento) {
        boolean changed = false;
        if (binding.textTitleEvent.getText().toString().equals(evento.getTitulo())){
            changed = false;
            if (binding.textDescripEvent.getText().toString().equals(evento.getDescripcion())){
                changed = false;
                if (binding.textDateEvent.getText().toString().trim().equals(evento.getFecha())){
                    changed = false;
                    if (binding.textHourEvent.getText().toString().equals(evento.getHora())){
                        changed = false;
                        if (binding.textPlaceEvent.getText().toString().equals(evento.getLugar())){
                            changed = false;
                            if (!changedImage){
                                changed = false;
                            }
                            else {
                                changed = true;
                            }
                        }
                        else {
                            changed = true;
                        }
                    }else {
                        changed = true;
                    }
                }else {
                    changed = true;
                }
            }else {
                changed = true;
            }
        }else {
            changed = true;

        }

        if (changed){
            if (!binding.buttonSaveChangeEvent.isEnabled()){
                binding.buttonSaveChangeEvent.setEnabled(true);
                binding.buttonSaveChangeEvent.setTextColor(ContextCompat.getColor(DaEditEventoActivity.this,R.color.white));
            }
        }else {
            if (binding.buttonSaveChangeEvent.isEnabled()){
                binding.buttonSaveChangeEvent.setEnabled(false);
                binding.buttonSaveChangeEvent.setTextColor(ContextCompat.getColor(DaEditEventoActivity.this,R.color.letra_clara));
            }
        }
    }

    private void validarCompleteForm(Evento evento) {
        boolean complete = false;
        if (!binding.textTitleEvent.getText().toString().trim().equals("")){
            complete = true;
            if (!binding.textDescripEvent.getText().toString().trim().equals("")){
                complete = true;
                if (!binding.textDateEvent.getText().toString().trim().equals("")){
                    complete = true;
                    if (!binding.textHourEvent.getText().toString().trim().equals("")){
                        complete = true;
                        if (!binding.textPlaceEvent.getText().toString().trim().equals("")){
                            complete = true;
                            if (!binding.textSelectActividad.getText().toString().equals("")){
                                complete = true;
                                if (changedImage){
                                    complete = true;
                                }
                                else {
                                    complete = false;
                                }
                            }
                            else {
                                complete = false;
                            }
                        }
                        else {
                            complete = false;
                        }
                    }else {
                        complete = false;
                    }
                }else {
                    complete = false;
                }
            }else {
                complete = false;
            }
        }else {
            complete = false;
        }

        if (complete){
            if (!binding.buttonSaveChangeEvent.isEnabled()){
                binding.buttonSaveChangeEvent.setEnabled(true);
                binding.buttonSaveChangeEvent.setTextColor(ContextCompat.getColor(DaEditEventoActivity.this,R.color.white));
            }
        }else {
            if (binding.buttonSaveChangeEvent.isEnabled()){
                binding.buttonSaveChangeEvent.setEnabled(false);
                binding.buttonSaveChangeEvent.setTextColor(ContextCompat.getColor(DaEditEventoActivity.this,R.color.letra_clara));
            }
        }
    }

    private void mostrarConfirmacionExit() {
        if (isExistEvent){
            if (binding.buttonSaveChangeEvent.isEnabled()){
                MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(DaEditEventoActivity.this);
                alertDialog.setTitle("Confirmación");
                alertDialog.setMessage("¿Está seguro de que desea descartar los cambios del evento? Los cambios hechos no serán guardados.");
                alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                alertDialog.setNegativeButton("Continuar editando", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.show();
            }else {finish();}
        }
        else {
            MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(DaEditEventoActivity.this);
            alertDialog.setTitle("Confirmación");
            alertDialog.setMessage("¿Está seguro de que desea descartar los cambios del evento? Los cambios hechos no serán guardados.");
            alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            alertDialog.setNegativeButton("Continuar editando", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertDialog.show();
        }
    }

    private void mostrarTimePicker(MaterialTimePicker timePicker) {
        timePicker.show(getSupportFragmentManager(), "tag");
        timePicker.addOnPositiveButtonClickListener(view -> {
            String minutes;
            String hour;
            if (timePicker.getMinute()<10){
                minutes = "0"+timePicker.getMinute();
            }else {
                minutes = String.valueOf(timePicker.getMinute());
            }
            if (timePicker.getHour()<10){
                hour = "0"+timePicker.getHour();
            }else {
                hour = String.valueOf(timePicker.getHour());
            }
            binding.textHourEvent.setText(hour+":"+minutes+" hrs");
            if (isExistEvent){
                validarChangeForm(evento);
            }else {
                validarCompleteForm(evento);
            }
        });
    }

    public void mostrarDatePicker(MaterialDatePicker datePicker){
        datePicker.show(getSupportFragmentManager(), "tag");
        datePicker.addOnPositiveButtonClickListener(selection -> {
            selection = Instant.ofEpochMilli((Long) selection)
                    .plus(6, ChronoUnit.HOURS)
                    .toEpochMilli();
            Date date = new Date((Long) selection);
            binding.textDateEvent.setText(formato.format(date));
            if (isExistEvent){
                validarChangeForm(evento);
            }else {
                validarCompleteForm(evento);
            }
        });
    }
    private void abrirDialogSubirFoto() {
        bottomSheetDialog = new BottomSheetDialog(DaEditEventoActivity.this);
        View bottomSheetView = LayoutInflater.from(DaEditEventoActivity.this).inflate(R.layout.dialog_alumno_subir_foto, (ConstraintLayout) findViewById(R.id.bottomSheetSubirFoto));
        EditText inputDescripcion = bottomSheetView.findViewById(R.id.inputDescripcion);
        inputDescripcion.setVisibility(View.GONE);
        ImageView imagenFoto = bottomSheetView.findViewById(R.id.imageFoto);
        imagenFoto.setImageURI(imageUri);
        Button botonSubirFoto = bottomSheetView.findViewById(R.id.buttonDialogSubirFoto);
        botonSubirFoto.setOnClickListener(view -> {
            changedImage = true;
            if (isExistEvent){
                validarChangeForm(evento);
            }else {
                validarCompleteForm(evento);
            }
            binding.imageView10.setImageURI(imageUri);
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
    private void abrirDialogFinishFinish() {
        bottomSheetDialog = new BottomSheetDialog(DaEditEventoActivity.this);
        View bottomSheetView = LayoutInflater.from(DaEditEventoActivity.this).inflate(R.layout.dialog_da_finalizar_evento_upload_photos, findViewById(R.id.bottomSheetFinishF));
        Button updatePhotos = bottomSheetView.findViewById(R.id.changePhotosF);
        Button finishAndUpload = bottomSheetView.findViewById(R.id.buttonDialogFinishF);
        ProgressBar progressBar = bottomSheetView.findViewById(R.id.progressBar3);
        updatePhotos.setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
            Intent finishIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            finishIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            finishIntent.addCategory(Intent.CATEGORY_OPENABLE);
            finishIntent.setType("image/*");
            finishIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            openManyImageLauncher.launch(finishIntent);
        });
        finishAndUpload.setOnClickListener(view -> {
            finishAndUpload.setText("");
            finishAndUpload.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            for (int i = 0; i<Math.min(listaFotos.size(), 5);i++){
                subirPicStorage(listaFotos.get(i).getImageUri(), evento.getFechaHoraCreacion().toString());
            }
            db.collection("actividades").document(evento.getActividadId()).collection("eventos")
                    .document("evento"+evento.getFechaHoraCreacion())
                    .update("estado","inactivo")
                    .addOnSuccessListener(unused -> {
                        db.collection("eventos").document("evento"+evento.getFechaHoraCreacion().toString())
                                .update("estado", "inactivo")
                                .addOnSuccessListener(unused1 -> {
                                    Snackbar.make(binding.getRoot(),"Se finalizó el evento exitosamente", Snackbar.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Snackbar.make(binding.getRoot(),"Ocurrió un error.", Snackbar.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Snackbar.make(binding.getRoot(),"Ocurrió un error.", Snackbar.LENGTH_SHORT).show();
                    });
            finish();
        });
        adapter.setContext(DaEditEventoActivity.this);
        Log.d("msg-test","Adapter set: "+listaFotos.size());
        adapter.setList(listaFotos);
        RecyclerView rvCarousel = bottomSheetView.findViewById(R.id.carousel_recycler_view);
        rvCarousel.setAdapter(adapter);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void subirPicStorage(Uri imageUri, String id) {
        Foto fotoNueva = new Foto();
        fotoNueva.setFechaHoraSubida(com.google.firebase.Timestamp.now());
        String userUid = FirebaseAuth.getInstance().getUid();
        fotoNueva.setAlumnoID(userUid);
        fotoNueva.setDescripcion("Gracias por participar en "+ evento.getTitulo());
        storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference().child("evento" + id + "/" + imageUri.getLastPathSegment() + ".jpg");
        reference.putFile(imageUri).continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                })
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("msg-test", "foto de evento agregada en storage");
                        fotoNueva.setFotoUrl(task.getResult().toString());
                        subirPicFirestore(fotoNueva, id);
                    } else {
                        Log.d("msg-test", "error");
                    }
                });
    }
    private void subirPicFirestore(Foto fotoNueva, String id) {
        db.collection("eventos")
                .document("evento" + id)
                .collection("fotos")
                .add(fotoNueva)
                .addOnSuccessListener(documentReference -> {
                    Log.d("msg-test", "foto guardada en firestore exitosamente");
                    finish();
                    startActivity(getIntent());
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }

    private void subirFoto(Uri imageUri, String id) {
        storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference().child("eventos/evento" + id + ".jpg");
        reference.putFile(imageUri).continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                })
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("msg-test", "foto perfil de evento agregada en storage");
                        subirFotoFirestore(task.getResult().toString(), id);
                    } else {
                        Log.d("msg-test", "error");
                    }
                });

    }
    private void subirFotoFirestore(String url, String id) {
        eventoGuardar.setFotoUrl(url);
        db.collection("eventos")
                .document("evento"+id)
                .update("fotoUrl", url)
                .addOnSuccessListener(documentReference -> {
                    Log.d("msg-test", "foto guardada en firestore exitosamente");
                    Snackbar.make(DaEditEventoActivity.this.getCurrentFocus(), "Se ha creado el evento exitosamente", Snackbar.LENGTH_SHORT).show();
                    Map<String, String> estado = new HashMap<>();
                    estado.put("estado",eventoGuardar.getEstado());
                    db.collection("actividades")
                            .document(currentActividad.getId()).collection("eventos")
                            .document("evento"+eventoGuardar.getFechaHoraCreacion().toString())
                            .set(estado)
                            .addOnSuccessListener(unused ->{
                                Snackbar.make(DaEditEventoActivity.this.getCurrentFocus(), "Se ha creado el evento exitosamente", Snackbar.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                //TODO DA*: borrar evento creado
                                Log.d("msg-test", "No se pudo actualizar en actividades");
                            });
                    finish();
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }
    private void subirNuevoEventoFirestore() {
        db.collection("eventos")
                .document("evento"+eventoGuardar.getFechaHoraCreacion().toString())
                .set(eventoGuardar)
                .addOnSuccessListener(documentReference -> {
                    Log.d("msg-test", "foto guardada en firestore exitosamente");
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }
    private void subirUpdateEventoFirestore(Map<String, Object> eventoUpdate) {
        db.collection("eventos")
                .document("evento"+evento.getFechaHoraCreacion().toString())
                .update(eventoUpdate)
                .addOnSuccessListener(documentReference -> {
                    Log.d("msg-test", "foto guardada en firestore exitosamente");
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }
    private ArrayList<Actividades> obtenerActividadesDesdeMemoria() {
        try (FileInputStream fileInputStream = DaEditEventoActivity.this.openFileInput("userData");
             FileReader fileReader = new FileReader(fileInputStream.getFD());
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String jsonData = bufferedReader.readLine();
            Gson gson = new Gson();
            Alumno alumno = gson.fromJson(jsonData, Alumno.class);
            return alumno.getActividadesId();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void buscarActividad(String id) {
        db.collection("actividades")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            Actividades a = task.getResult().toObject(Actividades.class);
                            Log.d("msg-test", "actividad encontrada: "+a.getNombre());
                            if (a.getEstado().equals("abierto")){
                                listaFinal.add(a.getNombre());
                                actividadesList.add(a);
                            }
                        }
                        else{
                            Log.d("msg-test", "DaGestionFragment error buscando act: "+id);
                        }
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVIDAD_MAPS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                datoRecibido = (Map<String, Object>) data.getSerializableExtra("lugar");
                Log.d("msg-test", "entra o no"+datoRecibido.get("nombre").toString());
                binding.textPlaceEvent.setText(datoRecibido.get("nombre").toString());
                if (isExistEvent){
                    validarChangeForm(evento);
                }else {
                    validarCompleteForm(evento);
                }
            } else {
                Snackbar.make(binding.getRoot(), "Ocurrió un error al cargar el lugar elegido, intente más tarde.", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void crearChat(String chatID){
        // creando chat
        Chat chat = new Chat(chatID, Arrays.asList(userUid), Timestamp.now(), ""); // agregando a delegado de activ como usuario por defecto de chat
        FirebaseUtilAl.getChatRoomReference(chatID).set(chat);
    }
}
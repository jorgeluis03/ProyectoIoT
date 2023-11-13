package com.example.proyecto_iot.delegadoActividad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.AlumnoEventoActivity;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.databinding.ActivityDaEditEventoBinding;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DaEditEventoActivity extends AppCompatActivity {
    ActivityDaEditEventoBinding binding;
    boolean isExistEvent;
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDaEditEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonEventoABack.setOnClickListener(view -> mostrarConfirmacionExit());

        Intent intent = getIntent();
        Evento evento = (Evento) intent.getSerializableExtra("evento");
        if (evento == null){
            isExistEvent = false;
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

        //TODO DA: solucionar error del inputText from DatePicker
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
            Intent galleryIntent = new Intent(MediaStore.ACTION_PICK_IMAGES);
            //openImageLauncher.launch(galleryIntent);
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
                                if (!binding.imageView10.getDrawable().equals(ContextCompat.getDrawable(DaEditEventoActivity.this,R.drawable.icon_upload2))){
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
                binding.buttonSaveChangeEvent.setTextColor(ContextCompat.getColor(DaEditEventoActivity.this,R.color.letra_clara));
            }
        }
    }

    private void mostrarConfirmacionExit() {
    }

    private void mostrarTimePicker(MaterialTimePicker timePicker) {
        timePicker.show(getSupportFragmentManager(), "tag");
        timePicker.addOnPositiveButtonClickListener(view -> {
            if (!binding.buttonSaveChangeEvent.isEnabled()){
                binding.buttonSaveChangeEvent.setEnabled(true);
                binding.buttonSaveChangeEvent.setTextColor(ContextCompat.getColor(DaEditEventoActivity.this,R.color.letra_clara));
            }
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
        });
    }

    public void mostrarDatePicker(MaterialDatePicker datePicker){
        datePicker.show(getSupportFragmentManager(), "tag");
        datePicker.addOnPositiveButtonClickListener(selection -> {
            if (!binding.buttonSaveChangeEvent.isEnabled()){
                binding.buttonSaveChangeEvent.setEnabled(true);
                binding.buttonSaveChangeEvent.setTextColor(ContextCompat.getColor(DaEditEventoActivity.this,R.color.letra_clara));
            }
            selection = Instant.ofEpochMilli((Long) selection)
                    .plus(6, ChronoUnit.HOURS)
                    .toEpochMilli();
            Date date = new Date((Long) selection);
            binding.textDateEvent.setText(formato.format(date));
        });
    }
}
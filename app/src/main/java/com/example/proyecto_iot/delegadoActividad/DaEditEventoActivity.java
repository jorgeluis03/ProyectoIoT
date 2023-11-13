package com.example.proyecto_iot.delegadoActividad;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.ActivityDaEditEventoBinding;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DaEditEventoActivity extends AppCompatActivity {
    ActivityDaEditEventoBinding binding;
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDaEditEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        long today = MaterialDatePicker.todayInUtcMilliseconds();
        CalendarConstraints.Builder constraintsBuilder =
                new CalendarConstraints.Builder()
                        .setFirstDayOfWeek(Calendar.MONDAY)
                        .setStart(today)
                        .setValidator(DateValidatorPointForward.now());

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
    }
    public void mostrarDatePicker(MaterialDatePicker datePicker){
        datePicker.show(getSupportFragmentManager(), "tag");
        datePicker.addOnPositiveButtonClickListener(selection -> {
            selection = Instant.ofEpochMilli((Long) selection)
                    .plus(6, ChronoUnit.HOURS)
                    .toEpochMilli();
            Date date = new Date((Long) selection);
            binding.textDateEvent.setText(formato.format(date));
        });
    }
}
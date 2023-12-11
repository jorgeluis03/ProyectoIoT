package com.example.proyecto_iot.alumno.Utils;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecimalDigitsInputFilter implements InputFilter { // PARA NO PERMITIR MÁS DE DOS DECIMALES EN DONACIONES

    Pattern mPattern;
    public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
        mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
    }

    @Override
    public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
        Matcher matcher = mPattern.matcher(spanned);
        if(!matcher.matches())
            return "";
        return null;
    }
}

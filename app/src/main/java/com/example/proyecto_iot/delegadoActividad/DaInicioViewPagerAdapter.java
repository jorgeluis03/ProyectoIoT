package com.example.proyecto_iot.delegadoActividad;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.proyecto_iot.alumno.Fragments.AlumnoEventosApoyandoFragment;
import com.example.proyecto_iot.alumno.Fragments.AlumnoEventosTodosFragment;
import com.example.proyecto_iot.delegadoActividad.Fragments.DaEventosMisActividadesFragment;

public class DaInicioViewPagerAdapter extends FragmentStateAdapter {
    public DaInicioViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new AlumnoEventosTodosFragment();
            case 1:
                return new DaEventosMisActividadesFragment();
            case 2:
                return new AlumnoEventosApoyandoFragment();
            default:
                return new AlumnoEventosTodosFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

package com.example.proyecto_iot.delegadoGeneral.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.FragmentDgAlumnosPendBinding;
import com.example.proyecto_iot.databinding.FragmentDgAlumnosRegistrBinding;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaUsuariosAdapter;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaUsuariosPendiAdapter;
import com.example.proyecto_iot.delegadoGeneral.entity.Usuario;

import java.util.ArrayList;
import java.util.List;

public class Dg_alumnos_pendFragment extends Fragment {
    FragmentDgAlumnosPendBinding binding;
    private List<Usuario> listaUserPendi = llenarListUsuariosPendi();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentDgAlumnosPendBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }
    @Override
    public void onStart() {
        super.onStart();
        cargarListaUserPendi();
    }

    private void cargarListaUserPendi() {
        ListaUsuariosPendiAdapter adapter = new ListaUsuariosPendiAdapter();
        adapter.setContext(getContext());
        adapter.setListaUsuarios(listaUserPendi);

        binding.recycleViewUserPendi.setAdapter(adapter);
        binding.recycleViewUserPendi.setLayoutManager(new LinearLayoutManager(getContext()));

    }
    public List<Usuario> llenarListUsuariosPendi(){
        List<Usuario> lista = new ArrayList<>();
        lista.add(new Usuario("Jorge Luis","Dominguez","a20200643@pucp.edu.pe"));
        lista.add(new Usuario("Noe","Gutierrez","a20204578@pucp.edu.pe"));
        lista.add(new Usuario("Valentino","De las casas","a20208963@pucp.edu.pe"));
        lista.add(new Usuario("Alexandra","Maldini","a20187458@pucp.edu.pe"));
        lista.add(new Usuario("Conor","McGregor Poirier","a20169852@pucp.edu.pe"));
        lista.add(new Usuario("Charles","Oliveira","a20193256@pucp.edu.pe"));
        lista.add(new Usuario("Dustin","Chandler","a20200521@pucp.edu.pe"));
        lista.add(new Usuario("Nate","Diaz","a20168547@pucp.edu.pe"));
        lista.add(new Usuario("Khabib","Makhashev","a20159632@pucp.edu.pe"));

        return lista;
    }
}
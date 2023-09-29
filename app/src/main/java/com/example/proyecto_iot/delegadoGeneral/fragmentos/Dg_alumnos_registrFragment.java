package com.example.proyecto_iot.delegadoGeneral.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.FragmentDgAlumnosRegistrBinding;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaUsuariosAdapter;
import com.example.proyecto_iot.delegadoGeneral.entity.Usuario;

import java.util.ArrayList;
import java.util.List;

public class Dg_alumnos_registrFragment extends Fragment {
    FragmentDgAlumnosRegistrBinding binding;
    private List<Usuario> listaUser = llenarListUsuarios();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentDgAlumnosRegistrBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        cargarListaUser();
    }
    public void cargarListaUser(){
        ListaUsuariosAdapter adapter = new ListaUsuariosAdapter();
        adapter.setContext(getContext());
        adapter.setListaUsuarios(listaUser);

        binding.recycleViewUserRegi.setAdapter(adapter);
        binding.recycleViewUserRegi.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public List<Usuario> llenarListUsuarios(){
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
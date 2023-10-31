package com.example.proyecto_iot.alumno.RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Foto;

import java.util.List;

public class ListaFotosEventoAdapter extends RecyclerView.Adapter<ListaFotosEventoAdapter.FotoViewHolder>{
    private List<Foto> fotoList;
    private Context context;

    @NonNull
    @Override
    public FotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_alumno_evento_foto, parent, false);
        return new FotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FotoViewHolder holder, int position) {
        Foto foto = fotoList.get(position);
        holder.foto = foto;

        TextView textDescripcion = holder.itemView.findViewById(R.id.textDescipcionFoto);
        textDescripcion.setText(foto.getDescripcion());
        ImageView imagenFoto = holder.itemView.findViewById(R.id.imageFoto);
        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL); // Almacenamiento en cache
        Glide.with(getContext())
                .load(foto.getFotoUrl())
                .apply(requestOptions)
                .into(imagenFoto);
    }

    @Override
    public int getItemCount() {
        return fotoList.size();
    }

    public class FotoViewHolder extends RecyclerView.ViewHolder{
        Foto foto;
        public FotoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public List<Foto> getFotoList() {
        return fotoList;
    }

    public void setFotoList(List<Foto> fotoList) {
        this.fotoList = fotoList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

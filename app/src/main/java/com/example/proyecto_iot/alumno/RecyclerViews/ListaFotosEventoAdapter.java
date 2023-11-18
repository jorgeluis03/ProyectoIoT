package com.example.proyecto_iot.alumno.RecyclerViews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.AlumnoPerfilEditarActivity;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.alumno.Entities.Foto;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.List;

public class ListaFotosEventoAdapter extends RecyclerView.Adapter<ListaFotosEventoAdapter.FotoViewHolder> {
    private List<Foto> fotoList;
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        ImageView imagenFoto = holder.itemView.findViewById(R.id.imageFoto);
        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL); // Almacenamiento en cache
        Glide.with(getContext())
                .load(foto.getFotoUrl())
                .apply(requestOptions)
                .into(imagenFoto);

        TextView textNombre = holder.itemView.findViewById(R.id.textAlumnoNombre);

        db.collection("alumnos")
                .document(foto.getAlumnoID())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Alumno alumno = task.getResult().toObject(Alumno.class);
                        textNombre.setText(alumno.getNombre() + " " + alumno.getApellidos() + ":");
                    } else {
                        Log.d("msg-test", "error buscando alumno de foto: " + task.getException().getMessage());
                    }
                });

        TextView textDescripcion = holder.itemView.findViewById(R.id.textDescipcionFoto);
        textDescripcion.setText(foto.getDescripcion());

        TextView textFechaPublicacion = holder.itemView.findViewById(R.id.textFechaPublicación);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM, dd");
        String fechaFormateada = simpleDateFormat.format(foto.getFechaHoraSubida().toDate());
        textFechaPublicacion.setText(fechaFormateada);
    }

    @Override
    public int getItemCount() {
        return fotoList.size();
    }

    public class FotoViewHolder extends RecyclerView.ViewHolder {
        Foto foto;

        public FotoViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.findViewById(R.id.buttonShare).setOnClickListener(view -> {

                Glide.with(getContext())
                        .asBitmap()
                        .load(foto.getFotoUrl())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                String path = MediaStore.Images.Media.insertImage( // bitmap en archivo temporal
                                        getContext().getContentResolver(),
                                        resource,
                                        "noFotoImagen",
                                        null
                                );
                                Uri fotoUri = Uri.parse(path);
                                Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, fotoUri);
                                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                shareIntent.setType("image/*");
                                getContext().startActivity(Intent.createChooser(shareIntent, getContext().getResources().getText(R.string.send)));
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            });
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

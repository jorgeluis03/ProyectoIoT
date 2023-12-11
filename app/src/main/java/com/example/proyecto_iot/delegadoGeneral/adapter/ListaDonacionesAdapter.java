package com.example.proyecto_iot.delegadoGeneral.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.delegadoGeneral.dto.DonacionDto;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseFCMUtils;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONObject;

import java.util.List;

public class ListaDonacionesAdapter extends RecyclerView.Adapter<ListaDonacionesAdapter.DonacionViewHolder> {
    private List<DonacionDto> lista;
    private Context context;

    @NonNull
    @Override
    public DonacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_dg_detalle_donacion,parent,false);
        return new DonacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonacionViewHolder holder, int position) {
        DonacionDto donacion = lista.get(position);
        holder.donacionDto = donacion;
        holder.nombreDonador.setText(donacion.getNombreDonante());
        holder.horaDonacion.setText(donacion.getHorahoraFecha());
        holder.montoDonacion.setText("S/"+donacion.getMonto());
    }
    @Override
    public int getItemCount() {
        return lista.size();
    }


    class DonacionViewHolder extends RecyclerView.ViewHolder{
        DonacionDto donacionDto;
        TextView nombreDonador,horaDonacion,montoDonacion,urlDonacion;
        Button btnAceptar, btnRechazar;

        public DonacionViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreDonador = itemView.findViewById(R.id.nombreDontante);
            horaDonacion = itemView.findViewById(R.id.horaDonacion);
            montoDonacion = itemView.findViewById(R.id.montoDonacion);
            btnAceptar = itemView.findViewById(R.id.btnAceptarDonacion);
            btnRechazar = itemView.findViewById(R.id.btnRechazarDonacion);
            //btnVerDonacion = itemView.findViewById(R.id.btnVerDonacion);


            btnAceptar.setOnClickListener(view -> {
                new MaterialAlertDialogBuilder(context)
                        .setTitle("Validar")
                        .setMessage("¿Estás seguro que deseas validar esta donación?")
                        .setPositiveButton("Aceptar", (dialogInterface, i) -> {
                            FirebaseUtilDg.getColeccionIdDonantes(donacionDto.getCodigoDonante())
                                .document(donacionDto.getIdDocumento())
                                        .update("estado","validado")
                                                .addOnSuccessListener(unused -> {
                                                    int position =getAbsoluteAdapterPosition();
                                                    lista.remove(position);

                                                    notifyDataSetChanged();

                                                    //enviar notificacion al donante de que se aprobó
                                                    enviarNotificacion(donacionDto.getCodigoDonante());
                                                });
                            Log.d("msg-don","codigo donante "+donacionDto.getCodigoDonante());
                            Log.d("msg-don","id document "+donacionDto.getIdDocumento());
                        })
                        .setNeutralButton("Cancelar", (dialogInterface, i) -> {
                            //
                        })
                        .show();
            });

            btnRechazar.setOnClickListener(view -> {
                new MaterialAlertDialogBuilder(context)
                        .setTitle("Denegar")
                        .setMessage("¿Estás seguro que deseas denegar esta donación?")
                        .setPositiveButton("Aceptar", (dialogInterface, i) -> {
                            FirebaseUtilDg.getColeccionIdDonantes(donacionDto.getCodigoDonante())
                                    .document(donacionDto.getIdDocumento())
                                    .update("estado","denegado")
                                    .addOnSuccessListener(unused -> {
                                        lista.remove(donacionDto);
                                        notifyDataSetChanged();
                                    });
                            Log.d("msg-don","codigo donante "+donacionDto.getCodigoDonante());
                            Log.d("msg-don","id document "+donacionDto.getIdDocumento());
                        })
                        .setNeutralButton("Cancelar", (dialogInterface, i) -> {
                            //
                        })
                        .show();
            });


            /*
            btnVerDonacion.setOnClickListener(view -> {
                Log.d("Debug", "Botón Ver Donación presionado");
                Log.d("Debug", "URL de la imagen: " + donacionDto.getFotoDonante());

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_image_viewer);
                ImageView imageView = dialog.findViewById(R.id.imageViewDialog);

                Glide.with(context)
                        .load(donacionDto.getFotoDonante())
                        .override(Target.SIZE_ORIGINAL) // Cargar imagen en su tamaño original
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.e("GlideError", "Error al cargar la imagen", e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                Log.d("Glide", "Imagen cargada con éxito");
                                return false;
                            }
                        })
                        .into(imageView);

                dialog.show();
            });
            */


        }

    }


    //metodo enviar notificacion
    public void enviarNotificacion(String codigoDonador) {
        //current username, message, currentUserId, otherUserToken
        FirebaseUtilDg.getCollAlumnos().whereEqualTo("codigo", codigoDonador).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Alumno usuarioDg = task.getResult().toObjects(Alumno.class).get(0);
                try {
                    JSONObject jsonObject = new JSONObject();

                    JSONObject notificationObj = new JSONObject();
                    notificationObj.put("title", "Donación aprobada");
                    notificationObj.put("body", "Gracias por su donación hacia Aitel");

                    jsonObject.put("notification", notificationObj);
                    jsonObject.put("to", usuarioDg.getFcmToken());

                    //llamar a la api
                    FirebaseFCMUtils.callApi(jsonObject);


                } catch (Exception e) {

                }

            }
        });


    }


    //encapsulamiento

    public List<DonacionDto> getLista() {
        return lista;
    }

    public void setLista(List<DonacionDto> lista) {
        this.lista = lista;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

package com.example.proyecto_iot.delegadoGeneral.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.proyecto_iot.delegadoGeneral.utils.AndroidUtilDg;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public class ListaUsuariosRegisAdpter extends FirestoreRecyclerAdapter<Alumno,ListaUsuariosRegisAdpter.DelegadoViewHolder> {
    Context context;


    String motivo;

    public ListaUsuariosRegisAdpter(@NonNull FirestoreRecyclerOptions<Alumno> options,Context context) {
        super(options);
        this.context = context;
    }
    @Override
    protected void onBindViewHolder(@NonNull DelegadoViewHolder holder, int position, @NonNull Alumno model) {
        holder.tvNombreUser.setText(model.getNombre()+' '+model.getApellidos());
        holder.tvCorreoUser.setText(model.getCorreo());

        holder.buttonBanear.setOnClickListener(view -> {
            showDialog(model);

        });
    }

    @NonNull
    @Override
    public DelegadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_dg_alumnos_regist,parent,false);
        return new DelegadoViewHolder(view);
    }


    public class DelegadoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreUser;
        TextView tvCorreoUser;
        Button buttonBanear;
        public DelegadoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreUser=itemView.findViewById(R.id.textViewNombreUserRegi_dg);
            tvCorreoUser=itemView.findViewById(R.id.textViewCorreoUserRegi_dg);
            buttonBanear = itemView.findViewById(R.id.buttonBanearUser);


        }
    }

    public void showDialog(Alumno model) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dg_dialog_layout, null);
        bottomSheetDialog.setContentView(dialogView);

        // Obtener referencias a los elementos del layout
        TextView textViewMiembros = dialogView.findViewById(R.id.textViewMiembros);
        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);
        RadioButton radioButtonOption1 = dialogView.findViewById(R.id.radioButtonOption1);
        RadioButton radioButtonOption2 = dialogView.findViewById(R.id.radioButtonOption2);
        // Agregar un OnCheckedChangeListener al RadioGroup
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if(checkedId==R.id.radioButtonOption1){
                motivo= radioButtonOption1.getText().toString();
                bottomSheetDialog.dismiss();

            }else if (checkedId == R.id.radioButtonOption2) {
                motivo= radioButtonOption2.getText().toString();

            }
            bottomSheetDialog.dismiss();
            new MaterialAlertDialogBuilder(context)
                    .setTitle("Banear")
                    .setMessage("¿Estás seguro que deseas banear a este usuario por "+motivo+"?")
                    .setNeutralButton("Cancelar", (dialog, which) -> {

                    })
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        // Responder a la pulsación del botón positivo
                        FirebaseUtilDg.getCollAlumnos().document(model.getId())
                                .update("estado","baneado")
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(context,"Usuario baneado",Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show());

                        AndroidUtilDg.enviarCorreo(model,"baneado");
                    })
                    .show();
        });


        // Mostrar el dialog
        bottomSheetDialog.show();
    }



}

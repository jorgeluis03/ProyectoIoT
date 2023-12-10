package com.example.proyecto_iot.alumno.RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.alumno.Entities.ChatMessage;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;

public class ListaMensajesAdapter extends FirestoreRecyclerAdapter<ChatMessage, ListaMensajesAdapter.ViewHolder> {
    private Context context;
    public ListaMensajesAdapter(@NonNull FirestoreRecyclerOptions<ChatMessage> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ChatMessage model) {

        // formateando hora
        String pattern = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String horaMensaje = simpleDateFormat.format(model.getTimestamp().toDate());

        if (model.getSenderID().equals(FirebaseUtilDg.getusuarioActualId())){
            holder.leftChatLayout.setVisibility(View.GONE);
            holder.rightChatLayout.setVisibility(View.VISIBLE);

            holder.rightChatText.setText(model.getChatMessage());
            holder.rightChatTime.setText(horaMensaje);
        }
        else{
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatLayout.setVisibility(View.VISIBLE);

            holder.leftChatText.setText(model.getChatMessage());
            holder.leftChatTime.setText(horaMensaje);

            FirebaseUtilDg.getCollAlumnos().document(model.getSenderID()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Alumno alumno = task.getResult().toObject(Alumno.class);
                    String nombreMensaje = alumno.getFullName();
                    holder.leftChatUser.setText(nombreMensaje);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_chat_mensaje, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftChatLayout, rightChatLayout;
        TextView leftChatText, leftChatUser, leftChatTime, rightChatText, rightChatTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            leftChatLayout = itemView.findViewById(R.id.leftChatLayout);
            leftChatText = itemView.findViewById(R.id.leftChatText);
            leftChatUser = itemView.findViewById(R.id.leftChatUser);
            leftChatTime = itemView.findViewById(R.id.leftChatTime);

            rightChatLayout = itemView.findViewById(R.id.rightChatLayout);
            rightChatText = itemView.findViewById(R.id.rightChatText);
            rightChatTime = itemView.findViewById(R.id.rightChatTime);
        }
    }
}

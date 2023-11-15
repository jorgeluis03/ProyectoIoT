package com.example.proyecto_iot.alumno;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cometchat.chat.constants.CometChatConstants;
import com.cometchat.chat.core.AppSettings;
import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.chat.models.Group;
import com.cometchat.chatuikit.messagecomposer.MessageComposerStyle;
import com.cometchat.chatuikit.messagelist.CometChatMessageList;
import com.cometchat.chatuikit.messagelist.MessageListStyle;
import com.example.proyecto_iot.AppConstants;
import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.alumno.Fragments.AlumnoHeader3Fragment;
import com.example.proyecto_iot.databinding.ActivityAlumnoChatBinding;

public class AlumnoChatActivity extends AppCompatActivity {
    private ActivityAlumnoChatBinding binding;
    private Evento evento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlumnoChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        evento = (Evento) getIntent().getSerializableExtra("evento");
        Bundle bundle = new Bundle();
        bundle.putString("header", evento.getTitulo());
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentHeader, AlumnoHeader3Fragment.class, bundle)
                .commit();
        cargarInterfaz();
        inicializarCometChatGrupo();
    }

    private void inicializarCometChatGrupo() {
        String region = AppConstants.REGION;
        String appID = AppConstants.APP_ID;
        Group group = new Group(evento.getChatID(), null, CometChatConstants.GROUP_TYPE_PRIVATE, null);

        AppSettings appSettings = new AppSettings.AppSettingsBuilder()
                .subscribePresenceForAllUsers()
                .setRegion(region)
                .autoEstablishSocketConnection(true)
                .build();

        CometChat.init(AlumnoChatActivity.this, appID, appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d("msg-test", "Initialization completed successfully");
                MessageListStyle listStyle = new MessageListStyle();
                listStyle.setBorderWidth(0);
                listStyle.setBackground(Color.parseColor("#303b57"));
                listStyle.setTimeStampTextColor(Color.parseColor("#b9b9b9"));
                listStyle.setLoadingIconTint(Color.parseColor("#ffffff"));
                binding.groupMessages.setGroup(group);
                binding.groupMessages.setStyle(listStyle);

                MessageComposerStyle composerStyle = new MessageComposerStyle();
                composerStyle.setBorderWidth(0);
                composerStyle.setInputBackgroundColor(Color.parseColor("#ffffff"));
                composerStyle.setTextColor(Color.parseColor("#292929"));
                binding.groupComposer.setGroup(group);
                binding.groupComposer.setStyle(composerStyle);
            }

            @Override
            public void onError(CometChatException e) {
                Log.d("msg-test", "Initialization failed with exception: " + e.getMessage());
            }
        });
    }

    private void cargarInterfaz(){
        if (evento.getEstado().equals("")){
            binding.groupComposer.setVisibility(View.GONE);
        }
    }
}
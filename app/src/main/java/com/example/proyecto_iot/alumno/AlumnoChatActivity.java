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
import com.cometchat.chatuikit.messageinformation.MessageInformationConfiguration;
import com.cometchat.chatuikit.messageinformation.MessageInformationStyle;
import com.cometchat.chatuikit.messagelist.CometChatMessageList;
import com.cometchat.chatuikit.messagelist.MessageListConfiguration;
import com.cometchat.chatuikit.messagelist.MessageListStyle;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.chatuikit.shared.views.CometChatMessageBubble.MessageBubbleStyle;
import com.cometchat.chatuikit.shared.views.CometChatTextBubble.CometChatTextBubble;
import com.cometchat.chatuikit.shared.views.cometchatActionSheet.ActionSheetStyle;
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
        cargarEstilosChat();
    }

    private void cargarEstilosChat() {
        String region = AppConstants.REGION;
        String appID = AppConstants.APP_ID;
        Group group = new Group(evento.getChatID(), null, CometChatConstants.GROUP_TYPE_PUBLIC, null);

        AppSettings appSettings = new AppSettings.AppSettingsBuilder()
                .subscribePresenceForAllUsers()
                .setRegion(region)
                .autoEstablishSocketConnection(true)
                .build();

        CometChat.init(AlumnoChatActivity.this, appID, appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d("msg-test", "Initialization completed successfully");

                // lista de mensajes
                CometChatMessageList messageList = binding.groupMessages;

                // estilo de lista de mensajes
                MessageListStyle listStyle = new MessageListStyle()
                        .setBorderWidth(0)
                        .setBackground(Color.parseColor("#222A3F"))
                        .setNameTextColor(Color.parseColor("#B9B9B9"))
                        .setTimeStampTextColor(Color.parseColor("#B9B9B9"))
                        .setLoadingIconTint(Color.parseColor("#ffffff"));

                messageList.setGroup(group); // grupo
                messageList.setStyle(listStyle); // estilo de lista de msj
                messageList.setLoadingIconTintColor(Color.GRAY); // icono de carga
                messageList.emptyStateText("Aún no hay mensajes en el chat 🧐");

                // input de mensaje
                MessageComposerStyle composerStyle = new MessageComposerStyle()
                        .setBorderWidth(0)
                        .setInputBackgroundColor(Color.parseColor("#FFFFFF"));

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
        if (evento.getEstado().equals("inactivo")){
            //binding.groupComposer.setVisibility(View.GONE);
        }
    }
}
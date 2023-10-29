package com.example.proyecto_iot.alumno;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.cometchat.chat.constants.CometChatConstants;
import com.cometchat.chat.core.AppSettings;
import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.chat.models.Group;
import com.example.proyecto_iot.AppConstants;
import com.example.proyecto_iot.databinding.ActivityAlumnoChatBinding;

public class AlumnoChatActivity extends AppCompatActivity {
    private ActivityAlumnoChatBinding binding;
    private String chatID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlumnoChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatID = getIntent().getStringExtra("chatID");

        inicializarCometChatGrupo();
    }

    private void inicializarCometChatGrupo(){
        String region = AppConstants.REGION;
        String appID = AppConstants.APP_ID;
        String authKey = AppConstants.AUTH_KEY;

        AppSettings appSettings = new AppSettings.AppSettingsBuilder()
                .subscribePresenceForAllUsers()
                .setRegion(region)
                .autoEstablishSocketConnection(true)
                .build();

        CometChat.init(AlumnoChatActivity.this, appID, appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d("msg-test", "Initialization completed successfully");
                binding.groupChat.setGroup(new Group(chatID, "Evento SDI", CometChatConstants.GROUP_TYPE_PRIVATE, ""));
            }

            @Override
            public void onError(CometChatException e) {
                Log.d("msg-test", "Initialization failed with exception: " + e.getMessage());
            }
        });
    }
}
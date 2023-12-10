package com.example.proyecto_iot.alumno.Entities;

import com.google.firebase.Timestamp;

import java.util.List;

public class Chat {
    private String chatID; // mismo que id de evento ("evento"+fechaHoraCreacion)
    private List<String> userIDs; // usuarios que pertenecen al chat
    private Timestamp lastMessageTimestamp;
    private String lastMessageSenderID;

    public Chat() {
    }

    public Chat(String chatId, List<String> usersIds, Timestamp lastMessageTimestamp, String lastMessageSenderId) {
        this.chatID = chatId;
        this.userIDs = usersIds;
        this.lastMessageTimestamp = lastMessageTimestamp;
        this.lastMessageSenderID = lastMessageSenderId;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public List<String> getUserIDs() {
        return userIDs;
    }

    public void setUserIDs(List<String> userIDs) {
        this.userIDs = userIDs;
    }

    public Timestamp getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(Timestamp lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getLastMessageSenderID() {
        return lastMessageSenderID;
    }

    public void setLastMessageSenderID(String lastMessageSenderID) {
        this.lastMessageSenderID = lastMessageSenderID;
    }
}

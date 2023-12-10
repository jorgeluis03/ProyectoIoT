package com.example.proyecto_iot.alumno.Entities;

import com.google.firebase.Timestamp;

public class ChatMessage {
    private String chatMessage;
    private String senderID;
    private Timestamp timestamp;

    public ChatMessage() {
    }

    public ChatMessage(String chatMessage, String senderID, Timestamp timestamp) {
        this.chatMessage = chatMessage;
        this.senderID = senderID;
        this.timestamp = timestamp;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

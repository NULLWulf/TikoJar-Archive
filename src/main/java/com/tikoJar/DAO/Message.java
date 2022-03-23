package com.tikoJar.DAO;

import java.time.LocalDate;

public class Message {

    private String userID;
    private LocalDate datePosted;
    private String messageContent;

    public Message(){}

    public Message(String userID, LocalDate datePosted, String messageContent) {
        this.userID = userID;
        this.datePosted = datePosted;
        this.messageContent = messageContent;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public LocalDate getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(LocalDate datePosted) {
        this.datePosted = datePosted;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}

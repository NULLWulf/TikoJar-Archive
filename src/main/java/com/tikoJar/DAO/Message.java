package com.tikoJar.DAO;

import org.apache.commons.lang3.RandomStringUtils;

import java.lang.invoke.StringConcatFactory;
import java.time.LocalDate;
import java.util.concurrent.BlockingDeque;

public class Message {

    private String userID;
    private String datePosted;
    private String messageContent;
    private String messageId;

    public Message(){}

    public Message(String userID, String datePosted, String messageContent) {
        this.userID = userID;
        this.datePosted = LocalDate.now().toString();
        this.messageContent = messageContent;
        generateMessageId();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageId() { return messageId;}

    public void setMessageId(String messageId) { this.messageId = messageId;}

    public void generateMessageId(){
        this.messageId = RandomStringUtils.randomAlphanumeric(10);
    }
}

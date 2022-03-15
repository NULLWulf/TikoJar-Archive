package com.tikoJar.DAO;

import java.util.ArrayList;

public class Jar {

    private String serverID;
    private OpeningCondition openingCondition;
    private ArrayList<Message> messages;

    public Jar(){}

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public OpeningCondition getOpeningCondition() {
        return openingCondition;
    }

    public void setOpeningCondition(OpeningCondition openingCondition) {
        this.openingCondition = openingCondition;
    }
}

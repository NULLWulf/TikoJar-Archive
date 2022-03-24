package com.tikoJar.DAO;

import java.util.ArrayList;

public class Jar extends MessageJar {

    private String serverID;
    private OpeningCondition openingCondition;

    public Jar(){}

    public Jar(String serverID, OpeningCondition openingCondition, ArrayList<Message> messages) {
        this.serverID = serverID;
        this.openingCondition = openingCondition;
        super.setMessages(messages);
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

/*
Authors:
Matthew Brown
Nathan Wolf
 */

package com.tikoJar.DAO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Jar {

    String serverID;
    private OpeningCondition openingCondition;
    private ArrayList<Message> messages;

    public Jar(){}  // Empty constructor needed for Jackson

    // Primary Constructor, createJar should use this, with OpeningCondition nested inside of Constructor
    // messages set to null to ensure array initialized in database
    public Jar(String serverID, OpeningCondition openingCondition) {
        this.serverID = serverID;
        this.openingCondition = openingCondition; // nest opening condiiton inside Jar constructor
        this.messages = new ArrayList<>();
        // such as website access etc.
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

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}

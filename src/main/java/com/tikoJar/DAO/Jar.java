package com.tikoJar.DAO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import java.util.ArrayList;
import org.bson.types.ObjectId;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Jar {

    String serverID;
    private OpeningCondition openingCondition;
    private String hashCode;
    private ArrayList<Message> messages;

    public Jar(){}  // Empty constructor needed for Jackson

    // Primary Constructor, createJar should use this, with OpeningCondition nested inside of Constructor
    // messages set to null to ensure array initialized in database
    public Jar(String serverID, OpeningCondition openingCondition) {
        this.serverID = serverID;
        this.openingCondition = openingCondition; // nest opening condiiton inside Jar constructor
        this.messages = new ArrayList<>();
        this.hashCode =  RandomStringUtils.randomAlphanumeric(20);  // hashCode, for possible additional admin related features
        // such as website access etc.
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String  serverID) {
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

    public void setMessages(ArrayList<Message> person) {
        this.messages = person;
    }

    public String getHashCode() { return hashCode;}

    public void setHashCode(String hashCode) { this.hashCode = hashCode;}
}

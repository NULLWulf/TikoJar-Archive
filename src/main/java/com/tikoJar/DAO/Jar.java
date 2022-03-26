package com.tikoJar.DAO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Jar {

    private Long serverID;
    private String serverName;
    private OpeningCondition openingCondition;
    private String hashCode;
    private ArrayList<Message> messages;

    public Jar(){}  // Empty constructor needed for Jackson

    // Primary Constructor, createJar should use this, with OpeningCondition nested inside of Constructor
    // messages set to null to ensure array initialized in database
    public Jar(Long serverID, String serverName, OpeningCondition openingCondition) {
        this.serverName = serverName;
        this.serverID = serverID;
        this.openingCondition = openingCondition; // nest opening condiiton inside Jar constructor
        this.messages = null;
        this.hashCode =  RandomStringUtils.randomAlphanumeric(20);  // hashCode, for possible additional admin related features
        // such as website access etc.
    }

    public String getServerName() {return serverName;}

    public void setServerName(String serverName) {this.serverName = serverName;}

    public Long getServerID() {
        return serverID;
    }

    public void setServerID(Long serverID) {
        this.serverID = serverID;
    }

    public OpeningCondition getOpeningCondition() {
        return openingCondition;
    }

    public void setOpeningCondition(OpeningCondition openingCondition) {
        this.openingCondition = openingCondition;
    }

    public ArrayList<Message> getMessage() {
        return messages;
    }

    public void setMessage(ArrayList<Message> person) {
        this.messages = person;
    }

    public String getHashCode() { return hashCode;}

    public void setHashCode(String hashCode) { this.hashCode = hashCode;}
}

package com.tikoJar.DAO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Jar {

    private Long serverID;
    private String serverName;
    private OpeningCondition openingCondition;

    @JsonProperty("Messages")
    private List<Message> message;

    public Jar(){}

    public Jar(Long serverID, String serverName, OpeningCondition openingCondition, List<Message> message) {
        this.serverName = serverName;
        this.serverID = serverID;
        this.openingCondition = openingCondition;
        this.message = message;
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

    public List<Message> getMessage() {
        return message;
    }

    public void setMessage(List<Message> person) {
        this.message = person;
    }
}

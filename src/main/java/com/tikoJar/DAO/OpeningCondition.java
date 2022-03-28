/*
Authors:
Matthew Brown
Nathan Wolf
 */

package com.tikoJar.DAO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpeningCondition {

    private boolean hasMessageLimit;
    private int messageLimit;
    private String creationDate;
    private String openingDate;
    private String serverChannelID;

    public OpeningCondition(){}  // Empty constructor for Jackson

    // Primary Constructor OpeningCondition, this should be nested inside the Jar constructor during createJars
    public OpeningCondition(boolean hasMessageLimit, int messageLimit, int timeLimitInDays, String serverChannelID) {
        this.hasMessageLimit = hasMessageLimit;
        this.messageLimit = messageLimit;
        this.creationDate = LocalDate.now().toString();  // get Time at Jar Creation and Converts to String
        this.openingDate = LocalDate.now().plusDays(timeLimitInDays).toString();  //adds days to current time for opening condition
        this.serverChannelID = serverChannelID;
    }

    public boolean isHasMessageLimit() {
        return hasMessageLimit;
    }

    public void setHasMessageLimit(boolean hasMessageLimit) {
        this.hasMessageLimit = hasMessageLimit;
    }

    public int getMessageLimit() {
        return messageLimit;
    }

    public void setMessageLimit(int messageLimit) {
        this.messageLimit = messageLimit;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(String openingDate) {
        this.openingDate = openingDate;
    }

    public String getServerChannelID() {
        return serverChannelID;
    }

    public void setServerChannelID(String serverChannelID) {
        this.serverChannelID = serverChannelID;
    }
}

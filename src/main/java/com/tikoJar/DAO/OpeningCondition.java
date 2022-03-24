package com.tikoJar.DAO;

import java.time.LocalDate;

public class OpeningCondition {

    private boolean hasMessageLimit;
    private int messageLimit;
    private String creationDate;
    private String openingDate;
    private String serverChannelID;

    public OpeningCondition(){}

    public OpeningCondition(boolean hasMessageLimit, int messageLimit, int timeLimitInDays, String serverChannelID) {
        this.hasMessageLimit = hasMessageLimit;
        this.messageLimit = messageLimit;
        this.creationDate = LocalDate.now().toString();;
        this.openingDate = LocalDate.now().plusDays(timeLimitInDays).toString();
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

package com.tikoJar.DAO;

import java.time.LocalDate;

public class OpeningCondition {

    private boolean hasMessageLimit;
    private int messageLimit;
    private LocalDate creationDate;
    private LocalDate openingDate;
    private String serverChannelID;

    public OpeningCondition(){

    }

    public OpeningCondition(boolean hasMessageLimit, int messageLimit, LocalDate creationDate, LocalDate openingDate, String serverChannelID) {
        this.hasMessageLimit = hasMessageLimit;
        this.messageLimit = messageLimit;
        this.creationDate = creationDate;
        this.openingDate = openingDate;
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

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public String getServerChannelID() {
        return serverChannelID;
    }

    public void setServerChannelID(String serverChannelID) {
        this.serverChannelID = serverChannelID;
    }
}

package com.tikoJar.DTO;
/*
Author: Nathan Wolf
Enumerated Endpoints, these are plugged into our specific MongoDB Api to indicate what kind of action
is being taken within MongoDB
 */
public enum ENDPT {

    FIND("findOne"),
    INSERT("insertOne"),
    UPDATE("updateOne"),
    DELETE("deleteOne"),
    AGG("aggregate");

    private final String endPoint;

    ENDPT(String endPoint) {
        this.endPoint = endPoint;
    }

    public String get() {
        return endPoint;
    }  // Gets enumerated endpoint
}

package com.tikoJar.DTO;
/*
Author: Nathan Wolf
Enumerated Endpoints, these are plugged into our specific MongoDB Api to indicate what kind of action
is being taken within MongoDB.  Do not remove from dedicated class
 */
public enum ENDPT {

    FIND("findOne"),
    INSERT("insertOne"),
    UPDATE("updateOne"),
    DELETE("deleteOne"),
    AGG("aggregate"),
    FINDALL("find");

    private final String endPoint;

    ENDPT(String endPoint) {
        this.endPoint = endPoint;
    }

    public String get() {
        return endPoint;
    }  // Gets enumerated endpoint

}

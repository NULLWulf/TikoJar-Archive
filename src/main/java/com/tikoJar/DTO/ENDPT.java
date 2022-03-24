package com.tikoJar.DTO;

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
    }
}

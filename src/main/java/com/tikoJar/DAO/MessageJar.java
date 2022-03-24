package com.tikoJar.DAO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class MessageJar {

    public MessageJar(){}

    @JsonProperty("messages")
    private List<Message> message;

    public List<Message> getMessage() {
        return message;
    }

    public void setMessage(List<Message> person) {
        this.message = person;
    }

}

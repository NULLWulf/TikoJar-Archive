package com.tikoJar.DTO;

import org.javacord.api.event.Event;
import org.javacord.api.event.message.MessageCreateEvent;

public class QueryHandler {
    private MessageCreateEvent event;

    public QueryHandler(MessageCreateEvent event){
        this.event = event;
    }

    public void addMessage(){

    }

    public void createJar(){

    }

    public void viewMessages(){

    }

    public void deleteMessage(){

    }

    public void deleteJar(){

    }

    public void getHelp(){
        new Thread(() -> {
            ResponseBuilder responseBuilder = new ResponseBuilder(null, event);
            responseBuilder.getHelpResponse();
        }).start();
    }

    public void checkMessageLimit(){

    }

    public void checkTimeLimits(){

    }
}

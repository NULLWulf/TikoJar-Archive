package com.tikoJar.DTO;

import jakarta.json.JsonObject;
import org.javacord.api.event.message.MessageCreateEvent;

public class QueryHandler {
    private MessageCreateEvent event;
    ResponseBuilder responseBuilder;
    JsonObject jsonObject;

    public QueryHandler(MessageCreateEvent event){
        this.event = event;
    }

    public void addMessage(){

        boolean messageAdded = true;

        // TODO: if server has jar, submit message, then store the message in it.

        // TODO: update messageAdded boolean

        responseBuilder.addMessageResponse(messageAdded);

    }

    public void createJar(boolean validCall, boolean isAdmin){

        boolean hasJar = true;

        if(validCall && isAdmin){

            // TODO: check if server has jar.
            // TODO: update hasJar boolean
            // TODO: if server does not have jar, store new jar in database.

        }

        this.responseBuilder = new ResponseBuilder(null, event);

        responseBuilder.createJarResponse(validCall, isAdmin, hasJar);

    }

    public void viewMessages(boolean isAdmin){

        if(isAdmin){

            // TODO: query all messages for jar

        } else {

            // TODO: query only the user's messages for jar

        }

        // TODO: instantiate responseBuilder with response object
        responseBuilder.viewMessagesResponse();
    }

    public void deleteMessage(boolean validSyntax){

        boolean messageDeleted = true;

        if(validSyntax){

            // TODO: delete message if it exists
            // TODO: update messageDeleted boolean

        }

        responseBuilder.deleteMessageResponse(validSyntax, messageDeleted);

    }

    public void deleteJar(){

    }

    public void getHelp(){

        this.responseBuilder = new ResponseBuilder(null, event);

        responseBuilder.getHelpResponse();

    }

    public void hello(){

        this.responseBuilder = new ResponseBuilder(null, event);

        responseBuilder.helloResponse();

    }

    public void invalidCommand(){

        this.responseBuilder = new ResponseBuilder(null, event);

        responseBuilder.invalidCommandResponse();

    }

    public void checkMessageLimit(){

    }

    public void checkTimeLimits(){

    }

}

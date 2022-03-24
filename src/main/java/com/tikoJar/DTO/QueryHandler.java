package com.tikoJar.DTO;

import jakarta.json.JsonObject;
import org.javacord.api.event.message.MessageCreateEvent;

import java.time.LocalDate;

public class QueryHandler {

    private MessageCreateEvent event;
    private String serverId;
    ResponseBuilder responseBuilder;

    public QueryHandler(MessageCreateEvent event){
        this.event = event;
        this.serverId = event.getServer().toString();
    }

    public void addMessage(String message){

        boolean messageAdded = false;

        // TODO: if server has jar, store the message in it.
        // TODO: else, messageAdded = false

        this.responseBuilder = new ResponseBuilder(null, event);

        responseBuilder.addMessageResponse(messageAdded);

        if(messageAdded){

            if(checkMessageLimit()){

                // TODO: retrieve jar from database

                // TODO: replace responseBuilder with new ResponseBuilder, instantiated with response object

                responseBuilder.messageLimitEvent();

                // TODO: delete server's jar

            }
        }

    }

    public void createJar(boolean validSyntax, boolean isAdmin, int messageLimit, int timeLimitInDays){

        boolean hasJar = false;

        if(validSyntax && isAdmin){

            // TODO: check if server has jar. If it does, set hasJar to true.

            if (!hasJar){

                if (messageLimit != 0){

                    // TODO: Store jar with message limit in database

                    // SUGGESTED METHOD:
                    // LocalDate creationDate = LocalDate.now();

                } else {

                    // TODO: Store jar with time limit in database

                    // SUGGESTED METHODS:
                    // LocalDate creationDate = LocalDate.now();
                    // LocalDate openingDate = creationDate.plusDays(timeLimitInDays);

                }

            }

        }

        this.responseBuilder = new ResponseBuilder(null, event);

        responseBuilder.createJarResponse(validSyntax, isAdmin, hasJar);

    }

    public void viewMessages(boolean isAdmin){

        boolean hasJar = true;

        // TODO: verify that server has a jar
        // TODO: else, hasJar = false

        if(hasJar){

            if(isAdmin){

                // TODO: query all messages for jar

            } else {

                // TODO: query only the user's messages for jar

            }

            // TODO: instantiate responseBuilder with response object

        } else {

            responseBuilder = new ResponseBuilder(null, event);

        }

        responseBuilder.viewMessagesResponse();
    }

    public void deleteMessage(boolean includedMessageID, String messageID){

        boolean messageDeleted = true;

        if(includedMessageID){

            // TODO: delete message if it exists
            // TODO: else, messageDeleted = false;

        }

        this.responseBuilder = new ResponseBuilder(null, event);

        responseBuilder.deleteMessageResponse(includedMessageID, messageDeleted);

    }

    public void deleteJar(boolean isAdmin){

        boolean jarDeleted = true;

        if(isAdmin){

            // TODO: delete jar if it exists
            // TODO: update jarDeleted boolean

        }

        this.responseBuilder = new ResponseBuilder(null, event);

        responseBuilder.deleteJarResponse(isAdmin, jarDeleted);

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

    public boolean checkMessageLimit(){

        boolean messageLimitReached = false;

        // TODO: check if message limit has been reached
        // TODO: if message limit is reached, set messageLimitReached = true;

        return messageLimitReached;

    }

    public void checkTimeLimits(){

        // TODO: query all jars that have time limits && the limit's date is <= today's date

        // TODO: instantiate responseBuilder with response object

        this.responseBuilder.timeLimitEvent();

    }

}

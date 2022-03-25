package com.tikoJar.DTO;


/*
Authors (by Function)
Nathan Wolf -

 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikoJar.DAO.Message;
import com.tikoJar.tests.JSON_Handler;
import kotlin._Assertions;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.javacord.api.event.message.MessageCreateEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class QueryHandler {

    private MessageCreateEvent event;

    private Long serverId;  // serverID are Long data types
    private String serverName; // serverNames are string

    ResponseBuilder responseBuilder;  // instantiated based on need

    // JSON Helper functions to assist with serialization and deserialization of queries
    JSON_Handler jsonHelper;

    // OKHTTP3 Library Objects, they are initliazed in the process query function
    // but declared within the class for each query method to access relevant data
    OkHttpClient client;
    MediaType mediaType;
    RequestBody body;
    Request request;
    Response response;

    String postResponseBody;
    int responseCode;


    public QueryHandler(MessageCreateEvent event){
        this.event = event;

        // Anytime query handler called, since it is within the context of
        // an individual discord server, constructors retrieves serverId and
        // and serverName, may change, in actuality serverId may be all that is required here
        event.getServer().ifPresentOrElse(sv -> this.serverName = sv.getName(),
                () -> System.out.println("Error retrieving Server name")
        );
        event.getServer().ifPresentOrElse(sv -> this.serverId = sv.getId(),
                () -> System.out.println("Error retrieving Server ID from Javacord API")
        );
        System.out.println("Server Name:" + serverName);
        System.out.println("Server Id:" + serverId);
    }

    public void addMessage(String message) throws IOException {
        responseBuilder = new ResponseBuilder(event); // Always a response of some kind, thus initialize
        if(checkIfJarExists()){  // HTTP Requests to see if jar exists
            if(checkIfMessageAdded(
                    new Message(event.getMessageAuthor().getDisplayName().toString(), message)))
            responseBuilder.addMessageResponse(true);  // Calls message added true response
            if(checkMessageLimit()){
                pullJar()
            }
            else{
                responseBuilder.addMessageResponse(false);
            }
        }else{
            responseBuilder.jarExistsResponse(false);  // Jar does not exist, pass to response builder to indicate error
        }

//        boolean messageAdded = false;
//
//        // TODO: if server has jar, store the message in it.
//        // TODO: else, messageAdded = false
//
//        this.responseBuilder = new ResponseBuilder(null, event);
//
//        responseBuilder.addMessageResponse(messageAdded);
//
//        if(messageAdded){
//
//            if(checkMessageLimit()){
//
//                // TODO: retrieve jar from database
//
//                // TODO: replace responseBuilder with new ResponseBuilder, instantiated with response object
//
//                responseBuilder.messageLimitEvent();
//
//                // TODO: delete server's jar
//
//            }
//        }

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

    public void viewMessages(boolean isAdmin) throws IOException {

        if(checkIfJarExists()){

            String viewMessagesQuery = """
                        {
                            "collection":"Jars",
                            "database":"TikoJarTest",
                            "dataSource":"PositivityJar",
                            "filter": { "serverID": "ABC123" }
                        }
                    """.stripIndent();

            processQuery(viewMessagesQuery,ENDPT.FIND.get());
             // can only call string once so need to store in string
            String toParse = stripDocument(postResponseBody);
            System.out.println(toParse);
            ObjectMapper mapper = new ObjectMapper();
            List<Message> messageJar = Arrays.asList(mapper.readValue(toParse, Message[].class));

        }else{

        }

//        // TODO: verify that server has a jar
//        // TODO: else, hasJar = false
//
//        if(hasJar){
//
//            if(isAdmin){
//
//                // TODO: query all messages for jar
//
//            } else {
//
//                // TODO: query only the user's messages for jar
//
//            }
//
//            // TODO: instantiate responseBuilder with response object
//
//        } else {
//
//            responseBuilder = new ResponseBuilder(null, event);
//
//        }
//
//        responseBuilder.viewMessagesResponse();
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

    public void processQuery(String query, String endPoint) throws IOException {

        client = new OkHttpClient().newBuilder().build();
        mediaType = MediaType.parse("application/json");
        body = RequestBody.create(query, mediaType);
        request = new Request.Builder()
                .url("https://data.mongodb-api.com/app/data-rlgbq/endpoint/data/beta/action/%s".formatted(endPoint))
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Access-Control-Request-Headers", "*")
                .addHeader("api-key", "TUGyzJPmesVH4FcrDqO0XovgYNq0L5B59xCnjFsB9nLFE7qkofdTvzYjBn2ID120")
                .build();

        response = client.newCall(request).execute();  // execute the request
        postResponseBody = Objects.requireNonNull(response.body()).string(); // stores response body as a String
        responseCode = response.code();  // Stores response code as an int

        response.close();  // Close the client

    }

    public Boolean checkIfJarExists() throws IOException {

        String checkJarExistsQuery = """
                {"collection":"Jars",
                "database":"TikoJarTest",
                "dataSource":"PositivityJar",
                "filter": { "serverID": "%s" }}
                """.formatted(serverId);

        processQuery(checkJarExistsQuery,ENDPT.FIND.get());
        return responseCode == 200;
    }

    public Boolean checkIfMessageAdded(Message addMessage) throws IOException {
        jsonHelper = new JSON_Handler();   // initialize JSON helper

        // Block quotes query, is a NoSql Query that adds a message to the message array
        String addMessageQuery = """
                {"collection":"Jars",
                "database":"TikoJarTest",
                "dataSource":"PositivityJar",
                "filter": { "serverID": "%s" },
                "update": {
                    "$push": {"messages": %s}}}
                """.formatted(serverId, jsonHelper.getObjAsJSONString(addMessage)).stripIndent();  // converts newMessage to JSON format
        processQuery(addMessageQuery,ENDPT.UPDATE.get());  // Sends query to HTTP Request Template
        return responseCode == 201;
    }

    public String stripDocument(String preStrip){  // strips document encapsulation from projected HTTP NoSql Queries
        // this formats that string representative in a way of how it's inserted into the database thus deserilaization
        // should be easier
        return StringUtils.substring(preStrip,12, preStrip.length() - 1);  // removes Document enclosure
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
}



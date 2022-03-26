package com.tikoJar.DTO;
/*
Authors (by Function)
Nathan Wolf - QueryHandler Constructors, addMessage, viewMessage, checkMessageLimits
    - checkIfJarExists, checkIfMessageAdded, pullJar, processQuery, stripJar, createJarQuery
Joel Santos - createJar, checkTimeLimits, deleteMessage, deleteJar
Matt Brown - initial class Skeleton, getHelp, inValidCommand, hello

 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikoJar.DAO.Jar;
import com.tikoJar.DAO.Message;
import com.tikoJar.tests.JSON_Handler;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.javacord.api.event.message.MessageCreateEvent;

import java.io.IOException;
import java.util.Objects;

public class QueryHandler {

    private final MessageCreateEvent event;

    private Long serverId;  // serverID are Long data types
    private String serverName; // serverNames are string

    ResponseBuilder responseBuilder;  // instantiated based on need

    // JSON Helper functions to assist with serialization and deserialization of queries
    JSON_Handler jsonHelper;

    // OKHTTP3 Library Objects, they are initialized in the process query function
    // but declared within the class for each query method to access relevant data
    OkHttpClient client;
    MediaType mediaType;
    RequestBody body;
    Request request;
    Response response;

    // Stores variables from response from HTTP client, client is closed after call so values in Response are volatile
    String postResponseBody;
    int responseCode;

    Jar currentJar;

    public QueryHandler(MessageCreateEvent event){
        this.event = event;

        // Anytime query handler called, since it is within the context of
        // an individual discord server, constructors retrieves serverId
        // and serverName, may change, in actuality serverId may be all that is required here
        event.getServer().ifPresentOrElse(sv -> this.serverName = sv.getName(),
                () -> System.out.println("Error retrieving Server name")
        );
        event.getServer().ifPresentOrElse(sv -> this.serverId = sv.getId(),
                () -> System.out.println("Error retrieving Server ID from Javacord API")
        );
        System.out.printf("""
                    Initializing QueryHandler for
                    %s : %s
                    """, serverName, serverId);
    }

    public void addMessage(String message) throws IOException {
        responseBuilder = new ResponseBuilder(event); // Always a response of some kind, thus initialize
        if(checkIfJarExists()){  // HTTP Requests to see if jar exists
            System.out.printf("""
                    Jar Exists for Server: %s : %s
                    Checking if Message Added...
                    """, serverName, serverId);
            if(checkIfMessageAdded(
                    new Message(event.getMessageAuthor().getDisplayName().toString(), message)))
                System.out.printf("""
                    Message Added for: %s : %s
                    Checking if Message Added...
                    """, serverName, serverId);
            responseBuilder.addMessageResponse(true);  // Calls message added true response
            deserializeJarFromResponseBody(); // deserializes jar form ResponseBody to prepare for checkingMessage Limits
            if(checkMessageLimit()){
                // currentJar gets sent to ResponseBuilder
                // currentJar sent to response builder message limit event
                // this.responseBuilder.messageLimitEvent(currentJar);
            }
        }else{
            System.out.printf("""
                    Message not Added for
                    %s : %s
                    """, serverName, serverId);
            responseBuilder.addMessageResponse(false);  // Jar does not exist, pass to response builder to indicate error
        }

    }

    public void createJar(boolean validSyntax, boolean isAdmin, int messageLimit, int timeLimitInDays) throws IOException {

        if(validSyntax && isAdmin){

            // TODO: check if server has jar. If it does, set hasJar to true.

            if (!checkIfJarExists()){

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

            }else{
                responseBuilder.createJarResponse(validSyntax, isAdmin,true);
            }
        }else{
            responseBuilder.createJarResponse(validSyntax, isAdmin, false);
        }

    }

    public void viewMessages(boolean isAdmin) throws IOException {

        if(checkIfJarExists()){
            deserializeJarFromResponseBody();
            // passing Admin and currentJar for extrapolation in response builder
            // this.responseBuilder.viewMessagesResponse(isAdmin, currentJar);
        }
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
        return currentJar.getOpeningCondition().getMessageLimit() == currentJar.getMessages().size();
    }

    public static void checkTimeLimits(){
    }

    public void processQuery(String query, String endPoint) throws IOException {

        client = new OkHttpClient().newBuilder().build();
        mediaType = MediaType.parse("application/json");
        body = RequestBody.create(query, mediaType);
        String url = "https://data.mongodb-api.com/app/data-rlgbq/endpoint/data/beta/action/%s".formatted(endPoint);
        request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Access-Control-Request-Headers", "*")
                .addHeader("api-key", "TUGyzJPmesVH4FcrDqO0XovgYNq0L5B59xCnjFsB9nLFE7qkofdTvzYjBn2ID120")
                .build();

        response = client.newCall(request).execute();  // execute the request
        postResponseBody = Objects.requireNonNull(response.body()).string(); // stores response body as a String
        responseCode = response.code();  // Stores response code as an int

        response.close();  // Close the client

        System.out.printf("""
        
        ----HTTP Request Results----:
            ::  Sent Query ::
        %s
            ::     URL     ::
        %s
            ::  Response   ::
        Status Code: %d
        Response Body:
        %s
        """, query,url,responseCode,postResponseBody);
    }

    public Boolean checkIfJarExists() throws IOException {

        String checkJarExistsQuery = """
                {"collection":"Jars",
                "database":"TikoJarTest",
                "dataSource":"PositivityJar",
                "filter": { "serverID": "%s" }}
                """.formatted(serverId);

        processQuery(checkJarExistsQuery,ENDPT.FIND.get());
        return !Objects.equals(postResponseBody.trim(), "{\"document\":null}");

    }

    private void deserializeJarFromResponseBody() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();  // Instantiate JSON Object Mapper
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);//Ignores properties it does recognize, value swill be null
        this.currentJar = objectMapper.readValue(  // Initialize Jar Object, Jackson mapper reads values
                stripDocument(postResponseBody), //from post http request response body which document enclosure stripped
                Jar.class);  // stores it in currentJar object in class
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
        return !Objects.equals(postResponseBody.trim(), "{\"document\":null}");

    }

    public String stripDocument(String preStrip){  // strips document encapsulation from projected HTTP NoSql Queries
        // this formats that string representative in a way of how it's inserted into the database thus deserialization
        // should be easier
        return StringUtils.substring(preStrip,12, preStrip.length() - 1);  // removes Document enclosure
    }

    public void createJarQuery(Jar jar) throws IOException {

        String createJarQuery = """
                {
                    "collection":"Jars",
                    "database":"TikoJarTest",
                    "dataSource":"PositivityJar",
                    "filter": { "serverID": "%s" },
                    "document": %s
                }
                """.formatted(serverId, jsonHelper.getObjAsJSONString(jar).stripIndent());

        processQuery(createJarQuery,ENDPT.INSERT.get());
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



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
import com.tikoJar.DAO.OpeningCondition;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.javacord.api.DiscordApi;
import org.javacord.api.event.message.MessageCreateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class QueryHandler {

    public static final Logger LOGGER = LogManager.getLogger("QueryHandler.class");
    private final MessageCreateEvent event;
    DiscordApi api;

    String serverId;  // serverID are Long data types
    String serverName; // serverNames are string

    ResponseBuilder responseBuilder;  // instantiated based on need
    // JSON Helper functions to assist with serialization and deserialization of queries

    // Stores variables from response from HTTP client, client is closed after call so values in Response are volatile
    String postResponseBody;

    Jar currentJar;  // is deserialized to if function is called ot do so
    Jar[] currentJars;

    final static String defaultEmpty = "{\"document\":null}";

    public QueryHandler(MessageCreateEvent event, DiscordApi api) {
        this.event = event;
        this.api = api;
        this.responseBuilder = new ResponseBuilder(this.event, this.api);

        // Anytime query handler called, since it is within the context of
        // an individual discord server, constructors retrieves serverID

        event.getServer().ifPresentOrElse(sv -> this.serverId = sv.getIdAsString(),
                () -> LOGGER.warn("Error retrieving Server ID from Java-cord API"));

        event.getServer().ifPresentOrElse(sv -> this.serverName = sv.getName(),
                () -> LOGGER.warn("Error retrieving Server name"));

        LOGGER.debug("""
                Initializing QueryHandler for %s : %s "
                """.formatted(serverId, serverName));
    }


    public void addMessage(String message) {
        if(checkIfJarExists()){  // HTTP Requests to see if jar exists
            LOGGER.info("""
                    Jar Exists for Server: %s
                    """.formatted(serverId));
            if(checkIfMessageAdded(
                    new Message(event.getMessageAuthor().getIdAsString(), message)))
                LOGGER.info("""
                    Checking if Message Added: %s
                    """.formatted(serverId));
            responseBuilder.addMessageResponse(true);  // Calls message added true response
            if(checkMessageLimit()){
                 responseBuilder.messageLimitEvent(currentJar);
            }
        }else{
            LOGGER.info("""
                    Message not Added for %s
                    """.formatted(serverId));
            responseBuilder.addMessageResponse(false);  // Jar does not exist, pass to response builder to indicate error
        }

    }

    public void createJar(boolean validSyntax, boolean isAdmin, int messageLimit, int timeLimitInDays)  {
        if(validSyntax && isAdmin){
            if (!checkIfJarExists()){
                if (messageLimit != 0){
                    createJarQuery(new Jar(this.serverId,new OpeningCondition(
                    true, messageLimit, 0 , event.getChannel().getIdAsString())));
                } else { createJarQuery(new Jar(this.serverId,
                 new OpeningCondition(false, 0, timeLimitInDays, event.getChannel().getIdAsString())));}
            }else{ responseBuilder.createJarResponse(true, true,true); }
        }else{ responseBuilder.createJarResponse(validSyntax, isAdmin, false);}}

    public void viewMessages(boolean isAdmin) {
        if(checkIfJarExists()){
            deserializeAllJars();
//            responseBuilder.viewMessagesResponse(isAdmin, currentJars);
        }{
            LOGGER.debug("No Jar found for : %s : %s".formatted(serverName, serverId));
        }
    }

    public void deleteMessage(boolean includedMessageID, String messageID){
        boolean messageDeleted = true;
        if(includedMessageID){
            // TODO: delete message if it exists
            // TODO: else, messageDeleted = false;
        }
        responseBuilder.deleteMessageResponse(includedMessageID, messageDeleted);
    }

    public void deleteJar(boolean isAdmin){  // Completed finished testing and working
        boolean jarDeleted = false;
        if(isAdmin){
            if(checkIfJarExists()){
                deleteJarQuery();
                jarDeleted = true;
            }
        }
        responseBuilder.deleteJarResponse(isAdmin, jarDeleted);
    }

    public boolean checkMessageLimit(){  // Checks Message Limit store in Opening Condition with Size
        return currentJar.getOpeningCondition().getMessageLimit() == currentJar.getMessages().size();
    }

    public void checkTimeLimits(){
        deserializeExpiredJars();
//        responseBuilder.timeLimitEvent(this.currentJars);
    }

    public String processQuery(String query, String endPoint) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(query, mediaType);
            String url = "https://data.mongodb-api.com/app/data-rlgbq/endpoint/data/beta/action/%s".formatted(endPoint);
            Request request = new Request.Builder()
                    .url(url)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Access-Control-Request-Headers", "*")
                    .addHeader("api-key", "TUGyzJPmesVH4FcrDqO0XovgYNq0L5B59xCnjFsB9nLFE7qkofdTvzYjBn2ID120")
                    .build();
            Response response = client.newCall(request).execute();  // execute the request
            LOGGER.debug("Process Query for Endpoint %s Called for %s %s - \nQuery: \n%s".formatted(endPoint, serverName, serverId, query));
            String tempResponse = Objects.requireNonNull(response.body()).string();
            response.close();
            return tempResponse;
        } catch (IOException e) {
            LOGGER.warn(e.getMessage());
            return defaultEmpty;
        }
    }

    public Boolean checkIfJarExists() {
        LOGGER.debug("Checking if Jar Exists for %s %s".formatted(serverName, serverId));
        String checkJarExistsQuery = """
                {"collection":"Jars",
                "database":"TikoJarTest",
                "dataSource":"PositivityJar",
                "filter": { "serverID": "%s" }}
                """.formatted(serverId);
        String postResponse = processQuery(checkJarExistsQuery,ENDPT.FIND.get());
        LOGGER.debug("-- Jar Exists Post Response --\n%s".formatted(postResponse));
        return !Objects.equals(postResponse, defaultEmpty);
    }

    public void deleteJarQuery() {
        String checkJarExistsQuery = """
                {"collection":"Jars",
                "database":"TikoJarTest",
                "dataSource":"PositivityJar",
                "filter": { "serverID": "%s" }}
                """.formatted(serverId);
        String postResponse = processQuery(checkJarExistsQuery,ENDPT.DELETE.get());
        LOGGER.debug("-- Jar Deleted Post Response --\n%s".formatted(postResponse));
    }

    private void deserializeExpiredJars() {
        try {
            String checkAndReturnExpired = """
                {"collection":"Jars",
                "database":"TikoJarTest",
                "dataSource":"PositivityJar",
                "filter": { "openingCondition.hasMessageLimit": { $eq : false },
                            "openingCondition.openingDate": { $eq : "%s" }}}
                """.formatted(LocalDate.now().toString());
            String postResponse = processQuery(checkAndReturnExpired,ENDPT.FINDALL.get());
            LOGGER.debug("Deserialize All Expired Post Response/n%s".formatted(postResponse));
            String stripped = stripDocument(postResponse, true);
            LOGGER.debug("Deserialize All Expired Post Strip/n%s".formatted(stripped));
            this.currentJars = new ObjectMapper()
                    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    .readValue(  // Initialize Jar Object, Jackson mapper reads values
                            stripDocument(postResponse, true), //from post http request response body which document enclosure stripped
                            Jar[].class);  // stores it in currentJar object in class
            LOGGER.debug("Deserialized Jar Output) " + new JSON_Handler().getObjAsJSONString(this.currentJar));
        }catch (JsonProcessingException e){
            LOGGER.warn(e.getMessage());
        }
    }

    private void deserializeAllJars() {
        try {
            String checkJarExistsQuery = """
                {"collection":"Jars",
                "database":"TikoJarTest",
                "dataSource":"PositivityJar"}
                """;
            String postResponse = processQuery(checkJarExistsQuery,ENDPT.FINDALL.get());
            LOGGER.debug("Deserialize All Post Response/n%s".formatted(postResponse));
            String stripped = stripDocument(postResponse, true);
            LOGGER.debug("Deserialize All Post Strip/n%s".formatted(stripped));
            this.currentJars = new ObjectMapper()
                    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    .readValue(  // Initialize Jar Object, Jackson mapper reads values
                            stripDocument(postResponse, true), //from post http request response body which document enclosure stripped
                            Jar[].class);  // stores it in currentJar object in class
            LOGGER.debug("Deserialized Jar Output) " + new JSON_Handler().getObjAsJSONString(this.currentJar));
        }catch (JsonProcessingException e){
            LOGGER.warn(e.getMessage());
        }
    }

    public Boolean checkIfMessageAdded(Message addMessage) {
        // Block quotes query, is a NoSql Query that adds a message to the message array
        String addMessageQuery = """
                {"collection":"Jars",
                "database":"TikoJarTest",
                "dataSource":"PositivityJar",
                "filter": { "serverID": "%s" },
                "update": {
                    "$push": {"messages": %s}}}
                """.formatted(serverId, new JSON_Handler().getObjAsJSONString(addMessage).stripIndent());  // converts newMessage to JSON format
        String postResponse = processQuery(addMessageQuery,ENDPT.UPDATE.get());  // Sends query to HTTP Request Template
        LOGGER.debug("-- Check if Message Added Post Response --\n%s".formatted(postResponse));
        return !Objects.equals(postResponse, defaultEmpty);
    }

    public String stripDocument(String preStrip, Boolean isArray){  // strips document encapsulation from projected HTTP NoSql Queries
        // this formats that string representative in a way of how it's inserted into the database thus deserialization
        // should be easier
        if (isArray){
            return StringUtils.substring(preStrip,13, preStrip.length() - 1);
        }else {
            return StringUtils.substring(preStrip,12, preStrip.length() - 1);  // removes Document enclosure in JSON
        }

    }
    public void createJarQuery(Jar jar) {
        String createJarQuery = """
                {"collection":"Jars",
                    "database":"TikoJarTest",
                    "dataSource":"PositivityJar",
                    "document": %s}
                """.formatted(new JSON_Handler().getObjAsJSONString(jar).stripIndent());
        String postResponse = processQuery(createJarQuery,ENDPT.INSERT.get());
        LOGGER.debug("-- Check if Jar Created Post Response --\n%s".formatted(postResponse));
    }
    public void getHelp(){
        LOGGER.info("""
                getHelp() Function Called for: %s
                """.formatted(serverId));
        responseBuilder.getHelpResponse();
    }
    public void hello(){
        LOGGER.info("""
                hello() Function Called for: %s
                """.formatted(serverId));
        responseBuilder.helloResponse();
    }
    public void invalidCommand(){
        LOGGER.info("""
                invalidCommand() Function Called for: %s
                """.formatted(serverId));
        responseBuilder.invalidCommandResponse();
    }
}



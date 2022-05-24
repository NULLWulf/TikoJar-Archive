package com.tikoJar.DTO;
/*
Authors (by Function)
Nathan Wolf - QueryHandler Constructors, addMessage, viewMessage, checkMessageLimits
    - checkIfJarExists, checkIfMessageAdded, pullJar, processQuery, stripJar, createJarQuery
Joel Santos - createJar, checkTimeLimits, deleteMessage, deleteJar
Matthew Brown - initial class Skeleton, getHelp, inValidCommand, hello
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.tikoJar.DAO.Jar;
import com.tikoJar.DAO.Message;
import com.tikoJar.DAO.OpeningCondition;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.javacord.api.DiscordApi;
import org.javacord.api.event.message.MessageCreateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class QueryHandler implements MNDPOINT {

    public static final Logger LOGGER = LogManager.getLogger("QueryHandler.class");
    private MessageCreateEvent event;
    DiscordApi api;

    String serverId;  // serverID are Long data types
    String serverName; // serverNames are strings
    ResponseBuilder responseBuilder;  // instantiated based on need

    Jar currentJar;  // is deserialized to if function is called ot do so
    ArrayList<Jar> jarLists;

    final static String defaultEmpty = "{\"document\":null}";
    final static String found1Updated1 = """
            {"matchedCount":1,"modifiedCount":1}""";


    public QueryHandler(){
        responseBuilder = new ResponseBuilder();
    }

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

    public void addMessage(String message, boolean lengthExceedsLimit) {
        boolean messageAdded = false;
        if (checkIfJarExists()) {  // HTTP Requests to see if jar exists
            if (!lengthExceedsLimit) {
                LOGGER.info("""
                        Jar Exists for Server: %s
                        """.formatted(serverId));
                messageAdded = checkIfMessageAdded(new Message(event.getMessageAuthor().getIdAsString(), message));
                responseBuilder.addMessageResponse(messageAdded, false);
                LOGGER.info("""
                        Checking if Message Added: %s
                        """.formatted(serverId));
                if (messageAdded) {
                    if (checkMessageLimit()) {
                        responseBuilder.messageLimitEvent(currentJar);
                        deleteJarQuery(this.serverId);
                    }
                }
            } else {
                responseBuilder.addMessageResponse(false, true);
            }
        } else {
            responseBuilder.addMessageResponse(false, lengthExceedsLimit);
        }
    }

    public void createJar(boolean validSyntax, boolean isAdmin, int messageLimit, int timeLimitInDays)  {
        boolean positiveLimitSet = true;
        boolean createdJar = false;
        if(validSyntax && isAdmin){
            if (!checkIfJarExists()){
                if (messageLimit <= 0 && timeLimitInDays <= 0){
                    positiveLimitSet = false;
                }else {
                    if (messageLimit > 0) {
                        createJarQuery(new Jar(this.serverId, new OpeningCondition(true, messageLimit,
                                0, event.getChannel().getIdAsString())));
                    } else {
                        createJarQuery(new Jar(this.serverId, new OpeningCondition(false, 0,
                                timeLimitInDays, event.getChannel().getIdAsString())));
                    }
                    createdJar = true;
                }
            }
        }
        responseBuilder.createJarResponse(validSyntax, isAdmin, createdJar, positiveLimitSet);
    }

    public void viewMessages(boolean isAdmin) {
        if(checkIfJarExists()){
            deserializeJar();
            responseBuilder.viewMessagesResponse(isAdmin, currentJar);
        }else{
            LOGGER.debug("No Jar found for : %s : %s".formatted(serverName, serverId));
            responseBuilder.viewMessagesResponse(isAdmin, null);
        }
    }

    public void deleteMessage(boolean includedMessageID, String messageID){
        boolean messageDeleted = false;
        if(includedMessageID){
            if(checkIfJarExists()){
                String compare = deleteMessageQuery(messageID);
                if(Objects.equals(compare, found1Updated1)){
                    messageDeleted = true;
                }
            }
        }
        responseBuilder.deleteMessageResponse(messageDeleted);
    }

    public void deleteJar(boolean isAdmin){  // Completed finished testing and working
        boolean jarDeleted = false;
        if(isAdmin){
            if(checkIfJarExists()){
                deleteJarQuery(this.serverId);
                jarDeleted = true;
            }
        }
        responseBuilder.deleteJarResponse(isAdmin, jarDeleted);
    }

    public boolean checkMessageLimit(){  // Checks Message Limit store in Opening Condition with Size
        deserializeJar();
        return this.currentJar.getOpeningCondition().getMessageLimit() == this.currentJar.getMessages().size();
    }

   public String processQuery(String query, String endPoint) {
       try {
           OkHttpClient client = new OkHttpClient().newBuilder().build();
           MediaType mediaType = MediaType.parse("application/json");
           RequestBody body = RequestBody.create(query, mediaType);
           String url = "https://data.mongodb-api.com/app/data-XXXXX/endpoint/data/beta/action/%s".formatted(endPoint);
           Request request = new Request.Builder()
                   .url(url)
                   .method("POST", body)
                   .addHeader("Content-Type", "application/json")
                   .addHeader("Access-Control-Request-Headers", "*")
                   .addHeader("api-key", "MY-API-KEY")
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
        String postResponse = processQuery(checkJarExistsQuery,MNDPOINT.FIND1);
        LOGGER.debug("-- Jar Exists Post Response --\n%s".formatted(postResponse));
        return !Objects.equals(postResponse, defaultEmpty);
    }

    public void deleteJarQuery(String servId) {
        String deleteJarQuery = """
                {"collection":"Jars",
                "database":"TikoJarTest",
                "dataSource":"PositivityJar",
                "filter": { "serverID": "%s" }}
                """.formatted(servId);
        String postResponse = processQuery(deleteJarQuery,MNDPOINT.DELETE1);
        LOGGER.debug("-- Jar Deleted Post Response --\n%s".formatted(postResponse));
    }

    public String deleteMessageQuery(String messageId) {
        String deleteJarQuery = """
               {"collection": "Jars",
               "database": "TikoJarTest",
               "dataSource": "PositivityJar",
               "filter": {
                   "serverID": "%s"
               },
               "update": {
                   "$pull": {"messages":{"messageId":"%s"}}}}
                """.formatted(serverId, messageId);
        String postResponse = processQuery(deleteJarQuery,MNDPOINT.UPDATE1);
        LOGGER.debug("-- Jar Deleted Post Response --\n%s".formatted(postResponse));
        return postResponse;
    }

    private void deserializeExpiredJars() {
        try {
            String checkAndReturnExpired = """
                {"collection":"Jars",
                "database":"TikoJarTest",
                "dataSource":"PositivityJar",
                "filter": { "openingCondition.hasMessageLimit": { "$eq" : false },
                            "openingCondition.openingDate": { "$eq" : "%s" }}}
                """.formatted(LocalDate.now().toString());
            String postResponse = processQuery(checkAndReturnExpired,MNDPOINT.FINDALL);
            LOGGER.debug("Deserialize All Expired Post Response/n%s".formatted(postResponse));
            String stripped = stripDocument(postResponse, true);
            LOGGER.debug("Deserialize All Expired Post Strip/n%s".formatted(stripped));
            ObjectMapper mapper = new ObjectMapper();

            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            this.jarLists = mapper.readValue(stripped, new TypeReference<>() {
            });
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
            String postResponse = processQuery(checkJarExistsQuery,MNDPOINT.FINDALL);
            LOGGER.debug("Deserialize All Post Response/n%s".formatted(postResponse));
            String stripped = stripDocument(postResponse, true);
            LOGGER.debug("Deserialize All Post Strip/n%s".formatted(stripped));

            ObjectMapper mapper = new ObjectMapper();

            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            this.jarLists = mapper.readValue(stripped, new TypeReference<>() {
            });

            LOGGER.debug("Deserialized Jar Output) " + new JSON_Handler().getObjAsJSONString(this.jarLists));
        }catch (JsonProcessingException e){
            LOGGER.warn(e.getMessage());
        }
    }

    private void deserializeJar() {
        try {
            String pullJar = """
                {"collection":"Jars",
                "database":"TikoJarTest",
                "dataSource":"PositivityJar",
                "filter": { "serverID": "%s" }}}
                """.formatted(serverId);
            String postResponse = processQuery(pullJar,MNDPOINT.FIND1);
            LOGGER.debug("Deserialize pullJar Post Response/n%s".formatted(postResponse));
            String stripped = stripDocument(postResponse, false);
            LOGGER.debug("Deserialize pullJar Strip/n%s".formatted(stripped));

            this.currentJar = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(stripped, Jar.class);  // Initialize Jar Object, Jackson mapper reads values
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
        String postResponse = processQuery(addMessageQuery,MNDPOINT.UPDATE1);  // Sends query to HTTP Request Template
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
        String postResponse = processQuery(createJarQuery,MNDPOINT.INSERT1);
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

    public void checkExpiredJars() {
        deserializeExpiredJars();
        if(jarLists != null) {
            responseBuilder.timeLimitEvent(this.jarLists);
            for(Jar j : this.jarLists){
                String tempServerId = j.getServerID();
                deleteJarQuery(tempServerId);
            }
        }
    }
}

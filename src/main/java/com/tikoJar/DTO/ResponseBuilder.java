package com.tikoJar.DTO;

import com.tikoJar.DAO.Jar;
import com.tikoJar.DAO.Message;
import jakarta.json.JsonObject;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.ArrayList;

public class ResponseBuilder {

    private String responseCode;
    private final MessageCreateEvent event;
    private Jar jar;

    public ResponseBuilder(MessageCreateEvent event){
        this.event = event;
    }

    public ResponseBuilder(JsonObject responseObject, MessageCreateEvent event){

        this.event = event;

        if(responseObject == null){

            responseCode = null;

        } else {

            deserializeResponse(responseObject);

        }

    }

    public void deserializeResponse(JsonObject responseObject){

        // TODO: deserialize responseObject

    }

    public void addMessageResponse(boolean messageAdded){

        if(messageAdded){

            String nickname = getNickname();
            event.getChannel().sendMessage("Thanks, " + nickname + "! Your message has " +
                    "been added to the jar!");

        } else {

            event.getChannel().sendMessage("Sorry, it looks like a jar has not been set up for your server. " +
                    "If you're a server admin, you can create a jar! " +
                    "Please use '!tiko help' to see a list of my commands.");

        }
    }

    public void createJarResponse(boolean validSyntax, boolean isAdmin, boolean hasJar) {

        if(!validSyntax){

            event.getChannel().sendMessage("I'm sorry, I can't create a jar with those details. " +
                    "Please go to OUR WEB URL to see how to properly create a jar.");

        } else if(!isAdmin){

            event.getChannel().sendMessage("I'm sorry, only a server admin can perform this task.");

        } else if (hasJar) {

            event.getChannel().sendMessage("Hmmm... it looks like your server already has a jar. " +
                    "If you would like to replace it, please delete the current jar first. " +
                    "You may then create a new jar.");

        } else {

            event.getChannel().sendMessage("Thanks! Your new jar is ready to go!");

        }
    }

    public void viewMessagesResponse(boolean isAdmin, Jar jar){

        if(responseCode == null){

            event.getChannel().sendMessage("I'm sorry, your server does not currently have a jar. If you" +
                    "are a server admin, you can create one! Use '!tiko help' for a list of my commands.");

        } else {

            StringBuilder responseString = new StringBuilder("");

            ArrayList<Message> messages = jar.getMessages();

            if (messages.size() > 0) {

                String userID = event.getMessageAuthor().getIdAsString();

                if (isAdmin) {

                    responseString.append("**Showing all messages:**\n\n");

                } else {

                    String nickname = getNickname();

                    responseString.append("**Showing messages submitted by " + nickname + ":**\n\n");

                }

                for (int i = 0; i < messages.size(); i++){

                    if (isAdmin || messages.get(i).getUserID().equals(userID)){



                    }

                }

            } else {

                event.getChannel().sendMessage("This jar is currently empty.");

            }

        }

    }

    public void deleteMessageResponse(boolean includedMessageID, boolean messageDeleted){

        if(!includedMessageID){

            // TODO: replace OUR WEB URL with actual URL
            event.getChannel().sendMessage("I'm sorry, the message ID you have provided is invalid. " +
                    "Please go to OUR WEB URL to see how to properly delete a message.");

        } else if(!messageDeleted){

            event.getChannel().sendMessage("I'm sorry, I can't delete that message. Please make sure you " +
                    "are entering the message ID correctly");

        } else {

            event.getChannel().sendMessage("" +
                    "No problem, " + getNickname() + ", I have deleted that message for you");

        }
    }

    public void deleteJarResponse(boolean isAdmin, boolean jarDeleted){

        if(!isAdmin){

            event.getChannel().sendMessage("I'm sorry, only a server admin can delete your server's jar.");

        } else if(!jarDeleted){

            event.getChannel().sendMessage("Your server does not currently have a jar.");

        } else {

            event.getChannel().sendMessage("No problem, " + getNickname() + ", I have deleted your server's " +
                    "jar. You are now welcome to create a new one!");

        }

    }

    public void getHelpResponse(){

        event.getChannel().sendMessage("" +
                "**Here's a list of my commands:** ```" +
                "!tiko add <positive message>\n" +
                "!tiko create <JAR DETAILS>\n" + // TODO: replace JAR DETAILS with proper syntax options
                "!tiko delete jar\n" +
                "!tiko delete message <message ID>\n" +
                "!tiko hello\n" +
                "!tiko help\n" +
                "!tiko view messages```\n" +
                "**For more detailed instructions, visit OUR WEB URL**"); // TODO: replace OUR WEB URL with actual URL

    }

    public void timeLimitEvent(){

        // TODO: format and deliver messages

    }

    public void messageLimitEvent(){

        // TODO: format and deliver messages

    }

    public void helloResponse(){

        String nickname = getNickname();
        event.getChannel().sendMessage("Hi, " + nickname + "!");

    }

    public void invalidCommandResponse(){

        event.getChannel().sendMessage("I'm sorry, I don't understand. Please make " +
                "sure you are entering the command correctly. Use '!tiko help' for a list of " +
                "valid commands.");

    }

    private String getNickname(){

        Server server = event.getServer().orElse(null);
        User user = event.getMessageAuthor().asUser().orElse(null);
        if (user != null){
            return user.getNickname(server).orElse(user.getDisplayName(server));
        }

        return "";

    }
}

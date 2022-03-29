/*
Author: Matthew Brown
 */

package com.tikoJar.DTO;

import com.tikoJar.DAO.Jar;
import com.tikoJar.DAO.Message;
import com.tikoJar.tikoService.TokenHandler;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.ArrayList;

public class ResponseBuilder {

    private MessageCreateEvent event;
    private DiscordApi api;

    public ResponseBuilder(){}

    public ResponseBuilder(MessageCreateEvent event, DiscordApi api){

        this.event = event;
        this.api = api;

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

    public void createJarResponse(boolean validSyntax, boolean isAdmin, boolean createdJar) {

        if(!validSyntax){

            event.getChannel().sendMessage("I'm sorry, I can't create a jar with those details. " +
                    "Please go to OUR WEB URL to see how to properly create a jar.");

        } else if(!isAdmin){

            event.getChannel().sendMessage("I'm sorry, only a server admin can perform this task.");

        } else if (!createdJar) {

            event.getChannel().sendMessage("Hmmm... it looks like your server already has a jar. " +
                    "If you would like to replace it, please delete the current jar first. " +
                    "You may then create a new jar.");

        } else {

            event.getChannel().sendMessage("Thanks! Your new jar is ready to go!");

        }
    }

    public void viewMessagesResponse(boolean isAdmin, Jar jar){

        // if no jar exists
        if(jar == null){

            event.getChannel().sendMessage("I'm sorry, your server does not currently have a jar. If you" +
                    "are a server admin, you can create one! Use '!tiko help' for a list of my commands.");

        } else {

            StringBuilder responseString = new StringBuilder();

            ArrayList<Message> messages = jar.getMessages();

            if (messages.size() > 0) {

                Server server = event.getServer().orElse(null);
                String userID = event.getMessageAuthor().getIdAsString();

                int messageDisplayCount = 0;

                if (isAdmin) {

                    responseString.append("**Showing all messages:**\n\n");

                } else {

                    String nickname = getNickname();

                    responseString.append("**Showing messages submitted by ").append(nickname).append(":**\n\n");

                }

                for (Message message : messages) {


                    if (isAdmin || message.getUserID().equals(userID)) {

                        String date = message.getDatePosted();
                        String messageID = message.getMessageId();
                        String messageContent = message.getMessageContent();

                        User user = api.getCachedUserById(message.getUserID()).orElse(null);

                        String nickname = "Unknown User";

                        if (user != null) {

                            nickname = user.getNickname(server).orElse(user.getDisplayName(server));

                        }

                        responseString
                                .append("***Message submitted by ").append(nickname).append(" on ").append(date)
                                .append(" (message ID #").append(messageID).append("):***\n").append(messageContent)
                                .append("\n\n")
                        ;

                        messageDisplayCount++;

                    }

                }

                if (messageDisplayCount > 0) {

                    event.getMessageAuthor().asUser().ifPresent(invokingUser ->
                            invokingUser.sendMessage(responseString.toString()));

                    event.getChannel().sendMessage("I have sent you a list via DM.");

                } else {

                    event.getChannel().sendMessage("You have not added any messages to this jar.");

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

        event.getChannel().sendMessage("""
                **Here's a list of my commands:** ```!tiko add <positive message>
                !tiko create <JAR DETAILS>
                !tiko delete jar
                !tiko delete message <message ID>
                !tiko hello
                !tiko help
                !tiko view messages```
                **For more detailed instructions, visit OUR WEB URL**"""); // TODO: replace OUR WEB URL with actual URL

    }

    public void timeLimitEvent(ArrayList<Jar> expiredJarsList){

        String token = TokenHandler.TOKEN;

        DiscordApi api = new DiscordApiBuilder().setToken(token).setAllIntents().login().join();

        for(Jar jar: expiredJarsList){

            ArrayList<Message> messages = jar.getMessages();

            Server server = api.getServerById(jar.getServerID()).orElse(null);

            // skips jar if server no longer exists
            if(server != null) {

                String responseString = eventResponseBuilder(server, messages, api, jar);

                String channelID = jar.getOpeningCondition().getServerChannelID();
                Channel channel = server.getChannelById(channelID).orElse(null);

                TextChannel textChannel = null;

                if (channel != null) {

                    textChannel = channel.asTextChannel().orElse(null);

                }

                if (textChannel != null) {

                    textChannel.sendMessage(responseString);

                }

            }

        }

    }

    public void messageLimitEvent(Jar jar){

        ArrayList<Message> messages = jar.getMessages();

        Server server = event.getServer().orElse(null);

        if (server != null) {

            String responseString = eventResponseBuilder(server, messages, api, jar);

            event.getChannel().sendMessage(responseString);

        }

    }

    public String eventResponseBuilder(Server server, ArrayList<Message> messages, DiscordApi api, Jar jar){

        StringBuilder responseString = new StringBuilder();

        String serverName = server.getName();
        String creationDate = jar.getOpeningCondition().getCreationDate();

        responseString.append("**Good day, ").append(serverName).append("! ")
                .append("""
                        And what a glorious day it is, for today is the day of your server's jar opening event!

                        """).append("I remember when you created this jar, back on ").append(creationDate).append(". ")
                .append("Since then, you've shared so many wonderful moments with me. ").append(messages.size())
                .append(" moments, in fact! ")
                .append("Now, it is my pleasure to re-share all those moments with all of *you!* \n\n")
                .append("Please enjoy reflecting on all these fond memories** :)\n\n\n");

        for (Message message : messages) {

            String date = message.getDatePosted();
            String messageContent = message.getMessageContent();

            User user = api.getCachedUserById(message.getUserID()).orElse(null);

            String nickname = "Unknown User";

            if (user != null) {

                nickname = user.getNickname(server).orElse(user.getDisplayName(server));

            }

            responseString
                    .append("***Message submitted by ").append(nickname).append(" on ").append(date)
                    .append(":***\n").append(messageContent).append("\n\n")
            ;

        }

        return responseString.toString();
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

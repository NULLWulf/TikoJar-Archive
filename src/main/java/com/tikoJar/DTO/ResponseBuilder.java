/*
Author: Matthew Brown
 */

package com.tikoJar.DTO;

import com.tikoJar.DAO.Jar;
import com.tikoJar.DAO.Message;
import com.tikoJar.tikoService.TokenHandler;
import jakarta.ws.rs.HEAD;
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

    public void addMessageResponse(boolean messageAdded, boolean lengthExceedsLimit){

        if (lengthExceedsLimit) {

            event.getChannel().sendMessage("I'm sorry, your message is too long. Please limit your message " +
                    "to 250 words or less.");

        } else if(messageAdded){

            String nickname = getNickname();
            event.getChannel().sendMessage("Thanks, " + nickname + "! Your message has " +
                    "been added to the jar!");

        } else {

            event.getChannel().sendMessage("Sorry, it looks like a jar has not been set up for your server. " +
                    "If you're a server admin, you can create a jar! " +
                    "Please use '!tiko help' to see a list of my commands.");

        }
    }

    public void createJarResponse(boolean validSyntax, boolean isAdmin, boolean createdJar, boolean nonZeroLimitSet) {

        if(!validSyntax){

            event.getChannel().sendMessage("I'm sorry, I can't create a jar with those details. " +
                    "Please visit tikojar.com to see how to properly create a jar.");

        } else if(!isAdmin){

            event.getChannel().sendMessage("I'm sorry, only a server admin can perform this task.");

        } else if (!nonZeroLimitSet) {

            event.getChannel().sendMessage("I'm sorry, I can't create a jar with those details. " +
                    "Please visit tikojar.com to see how to properly create a jar.");

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

            ArrayList<String> messagesOutput = new ArrayList<>();

            ArrayList<Message> messages = jar.getMessages();

            if (messages.size() > 0) {

                Server server = event.getServer().orElse(null);
                String userID = event.getMessageAuthor().getIdAsString();

                String serverName = "Unknown Server";

                if (server != null) {

                    serverName = server.getName();

                }

                int messageDisplayCount = 0;

                if (isAdmin) {

                    messagesOutput.add("```Showing all messages on " + serverName + ":```");

                } else {

                    String nickname = getNickname();

                    messagesOutput.add("**Showing messages submitted by " + nickname + " on " +
                            "" + serverName + ":**\n\n");

                }

                for (Message message : messages) {


                    if (isAdmin || message.getUserID().equals(userID)) {

                        StringBuilder messageOutput = new StringBuilder();

                        String date = message.getDatePosted();
                        String messageID = message.getMessageId();
                        String messageContent = message.getMessageContent();

                        User user = api.getCachedUserById(message.getUserID()).orElse(null);

                        String nickname = "Unknown User";

                        if (user != null) {

                            nickname = user.getNickname(server).orElse(user.getDisplayName(server));

                        }

                        messageOutput
                                .append("***Message submitted by ").append(nickname).append(" on ").append(date)
                                .append(" (message ID #").append(messageID).append("):***\n").append(messageContent)
                                .append("\n\n")
                        ;

                        messagesOutput.add(messageOutput.toString());

                        messageDisplayCount++;

                    }

                }

                if (messageDisplayCount > 0) {

                    for (String message: messagesOutput){

                        event.getMessageAuthor().asUser().ifPresent(invokingUser ->
                                invokingUser.sendMessage(message));

                    }

                    event.getChannel().sendMessage("I have sent you a list via DM.");

                } else {

                    event.getChannel().sendMessage("You have not added any messages to this jar.");

                }

            } else {

                event.getChannel().sendMessage("This jar is currently empty.");

            }



        }

    }

    public void deleteMessageResponse(boolean messageDeleted){

        if(!messageDeleted){

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
                !tiko create m <number of messages>
                !tiko create t <number of days>
                !tiko delete jar
                !tiko delete message <message ID>
                !tiko hello
                !tiko help
                !tiko view messages```
                **For more detailed instructions, visit http://www.tikojar.com**""");

    }

    public void timeLimitEvent(ArrayList<Jar> expiredJarsList){

        String token = TokenHandler.TOKEN;

        DiscordApi api = new DiscordApiBuilder().setToken(token).setAllIntents().login().join();

        for(Jar jar: expiredJarsList){

            ArrayList<Message> messages = jar.getMessages();

            Server server = api.getServerById(jar.getServerID()).orElse(null);

            // skips jar if server no longer exists
            if(server != null) {

                ArrayList<String> messageOutput = eventResponseBuilder(server, messages, api, jar);

                String channelID = jar.getOpeningCondition().getServerChannelID();
                Channel channel = server.getChannelById(channelID).orElse(null);

                TextChannel textChannel = null;

                if (channel != null) {

                    textChannel = channel.asTextChannel().orElse(null);

                }

                if (textChannel != null) {

                    for (String message: messageOutput){

                        textChannel.sendMessage(message);

                    }

                }

            }

        }

    }

    public void messageLimitEvent(Jar jar){

        ArrayList<Message> messages = jar.getMessages();

        Server server = event.getServer().orElse(null);

        if (server != null) {

            ArrayList<String> messageOutput = eventResponseBuilder(server, messages, api, jar);

            String channelID = jar.getOpeningCondition().getServerChannelID();
            Channel channel = server.getChannelById(channelID).orElse(null);

            TextChannel textChannel = null;

            if (channel != null) {

                textChannel = channel.asTextChannel().orElse(null);

            }

            if (textChannel != null) {

                for (String message: messageOutput){

                    textChannel.sendMessage(message);

                }

            }

        }

    }

    public ArrayList<String> eventResponseBuilder(Server server, ArrayList<Message> messages, DiscordApi api, Jar jar){

        String serverName = server.getName();
        String creationDate = jar.getOpeningCondition().getCreationDate();

        ArrayList<String> messagesOutput = new ArrayList<>();

        messagesOutput.add("**Good day, " + serverName + "!\n\n" +
                "And what a glorious day it is, for today is the day of your server's jar opening event!\n\n" +
                "I remember when you created this jar, back on " + creationDate + ". Since then, you've shared so " +
                "many wonderful moments with me. " + messages.size() +  " moments, in fact!\n\n" +
                "Now, it is my pleasure to re-share all those moments with all of *you!* \n\n" +
                "Please enjoy reflecting on all these fond memories** :slight_smile:\n");

        for (Message message : messages) {

            StringBuilder messageString = new StringBuilder();

            String date = message.getDatePosted();
            String messageContent = message.getMessageContent();

            User user = api.getCachedUserById(message.getUserID()).orElse(null);

            String nickname = "Unknown User";

            if (user != null) {

                nickname = user.getNickname(server).orElse(user.getDisplayName(server));

            }

            messageString
                    .append("***Message submitted by ").append(nickname).append(" on ").append(date)
                    .append(":***\n").append(messageContent).append("\n\n")
            ;

            messagesOutput.add(messageString.toString());
        }

        return messagesOutput;
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

package com.codenamebear.rest;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

public class CommandHandler {
    public static void main(String[] args) {
        DiscordApi api = new DiscordApiBuilder().setToken("" +
                "OTM5NjA5NTU0MzUyNzYyODgw.Yf7Vlg.gah8DpVv9ONLnTTxcqzrWrKCh1g").setAllIntents().login().join();

        api.addMessageCreateListener(event -> {

            new Thread(() -> {

                String messageContent = event.getMessageContent();
                // Proceed only if message is at least 10 characters
                if(messageContent.length() >= 5){

                    // Proceed only if message begins with !tiko
                    if(messageContent.substring(0, 5).equalsIgnoreCase("!tiko")){

                        // Parse message contents if message is >= 10 characters. Else, give user an error response.
                        if(messageContent.length() >= 10){
                            // Check message contents for command and respond
                            if (messageContent.substring(0, 10).equalsIgnoreCase("!tiko add ")){
                                String nickname = getNickname(event);
                                event.getChannel().sendMessage("Thanks, " + nickname + "! Your message has " +
                                        "been added to the jar!");
                            } else if (event.getMessageContent().equalsIgnoreCase("!tiko hello")) {
                                String nickname = getNickname(event);
                                event.getChannel().sendMessage("Hi, " + nickname + "!");
                            } else if (event.getMessageContent().equalsIgnoreCase("!tiko help")){
                                event.getChannel().sendMessage(getHelp());
                            } else if (event.getMessageContent().equalsIgnoreCase("!Tiko view messages")) {
                                event.getChannel().sendMessage("" +
                                        "Sorry, I'm still learning how to perform this task.");
                            } else {
                                event.getChannel().sendMessage("I'm sorry, I don't understand. Please make " +
                                        "sure you are entering the command correctly. Use '!tiko help' for a list of " +
                                        "valid commands.");
                            }
                        } else {
                            event.getChannel().sendMessage("I'm sorry, I don't understand. Please make sure " +
                                    "you are entering the command correctly. Use '!tiko help' for a list of valid " +
                                    "commands.");
                        }
                    }
                }
            }).start();
        });
    }

    public static String getHelp(){
        return ("**Here's a list of my commands:** ```" +
                "!tiko add <message>\n" +
                "!tiko hello\n") +
                "!tiko help\n" +
                "!tiko view messages```";
    }

    public static String getNickname(MessageCreateEvent event){
        Server server = event.getServer().orElse(null);
        User user = event.getMessageAuthor().asUser().orElse(null);
        if (user != null){
            return user.getNickname(server).orElse(user.getDisplayName(server));
        }

        return "";
    }
}

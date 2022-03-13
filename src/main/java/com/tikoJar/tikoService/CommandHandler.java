package com.tikoJar.tikoService;

import com.tikoJar.DTO.QueryHandler;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class CommandHandler {
    public static void main(String[] args) {
        DiscordApi api = new DiscordApiBuilder().setToken("" +
                "OTM5NjA5NTU0MzUyNzYyODgw.Yf7Vlg.gah8DpVv9ONLnTTxcqzrWrKCh1g").setAllIntents().login().join();

        api.addMessageCreateListener(event -> {

            new Thread(() -> {

                String messageContent = event.getMessageContent();

                // Proceed only if message is at least 5 characters
                if(messageContent.length() >= 5){

                    // Proceed only if message begins with !tiko
                    if(messageContent.substring(0, 5).equalsIgnoreCase("!tiko")){

                        QueryHandler queryHandler = new QueryHandler(event);

                        if(messageContent.length() >= 12){

                            if (event.getMessageContent().substring(0,12).equalsIgnoreCase("" +
                                    "!tiko create")){

                                boolean validSyntax = true;
                                boolean isAdmin = event.getMessageAuthor().isServerAdmin();

                                // TODO: check syntax and update validSyntax boolean

                                queryHandler.createJar(validSyntax, isAdmin);

                            }

                        } else if (messageContent.length() >= 10){

                            // Check message contents for command and respond
                            if (messageContent.substring(0, 10).equalsIgnoreCase("!tiko add ")){

                                queryHandler.addMessage();

                            } else if (event.getMessageContent().equalsIgnoreCase("!tiko hello")) {

                                queryHandler.hello();

                            } else if (event.getMessageContent().equalsIgnoreCase("!tiko help")){

                                queryHandler.getHelp();

                            } else if (event.getMessageContent().equalsIgnoreCase("!Tiko view messages")) {

                                queryHandler.viewMessages(event.getMessageAuthor().isServerAdmin());

                            } else {

                                queryHandler.invalidCommand();

                            }

                        } else {

                            queryHandler.invalidCommand();

                        }
                    }
                }
            }).start();
        });
    }
}

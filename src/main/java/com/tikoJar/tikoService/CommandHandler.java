/*
Author: Matthew Brown
 */

package com.tikoJar.tikoService;

import com.tikoJar.DTO.QueryHandler;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.server.Server;

public class CommandHandler {
    private enum MethodID{

        COMMANDPREFIX("!tiko"),
        ADDMESSAGE("add"),
        CREATEJAR("create"),
        DELETEJAR("delete jar"),
        DELETEMESSAGE("delete message"),
        HELLO("hello"),
        HELP("help"),
        MESSAGELIMIT("m"),
        TIMELIMIT("t"),
        VIEWMESSAGES("view messages");

        private final String command;

        MethodID(String command) {
            this.command = command;
        }

        public String getCommand() {
            return command;
        }

    }

    public static void main(String[] args) {


        String token = TokenHandler.TOKEN;

        DiscordApi api = new DiscordApiBuilder().setToken(token).setAllIntents().login().join();

        new TimedEventHandler();

        api.addMessageCreateListener(event -> new Thread(() -> {

            Server server = event.getServer().orElse(null);

            if (server != null) {

                String[] messageContent = event.getMessageContent().split("\\s");

                // Proceed only if message has content
                if (messageContent.length > 0) {

                    // Proceed only if message begins with !tiko
                    if (messageContent[0].equalsIgnoreCase(MethodID.COMMANDPREFIX.getCommand())) {

                        // Determine whether message author is server admin
                        boolean isAdmin = event.getMessageAuthor().isServerAdmin();

                        // Instantiate QueryHandler for method calls
                        QueryHandler queryHandler = new QueryHandler(event, api);

                        // Determine number of words contained in user message
                        if (messageContent.length >= 3) {

                            if (messageContent[1].equalsIgnoreCase(MethodID.ADDMESSAGE.getCommand())) {

                                StringBuilder message = new StringBuilder();

                                message.append(messageContent[2]);

                                if (messageContent.length > 3){

                                    for (int i = 3; i < messageContent.length; i++){

                                        message.append(" ");
                                        message.append(messageContent[i]);

                                    }

                                }

                                if (message.toString().length() <= 250) {

                                    queryHandler.addMessage(message.toString(), false);

                                } else {

                                    queryHandler.addMessage(null, true);

                                }

                            } else if ((messageContent[1] + " " + messageContent[2]).equalsIgnoreCase(
                                    MethodID.DELETEMESSAGE.getCommand())) {

                                boolean includedMessageID = true;
                                String messageID = "";

                                if (messageContent.length == 4) {

                                    messageID = messageContent[3];

                                } else {

                                    includedMessageID = false;

                                }

                                queryHandler.deleteMessage(includedMessageID, messageID);

                            } else if (messageContent[1].equalsIgnoreCase(MethodID.CREATEJAR.getCommand())) {

                                boolean validSyntax = true;
                                int messageLimit = 0;
                                int timeLimit = 0;

                                if (messageContent.length == 4) {

                                    if (messageContent[2].equalsIgnoreCase(MethodID.MESSAGELIMIT.getCommand())){

                                        try {

                                            messageLimit = Integer.parseInt(messageContent[3]);

                                        } catch (NumberFormatException nfe) {

                                            validSyntax = false;

                                        }

                                    } else if (messageContent[2].equalsIgnoreCase(MethodID.TIMELIMIT.getCommand())){

                                        try {

                                            timeLimit = Integer.parseInt(messageContent[3]);

                                        } catch (NumberFormatException nfe) {

                                            validSyntax = false;

                                        }

                                    } else {

                                        validSyntax = false;

                                    }

                                } else {

                                    validSyntax = false;

                                }

                                queryHandler.createJar(validSyntax, isAdmin, messageLimit, timeLimit);


                            } else if ((messageContent[1] + " " + messageContent[2]).equalsIgnoreCase(
                                    MethodID.VIEWMESSAGES.getCommand())) {

                                if (messageContent.length > 3){

                                    queryHandler.invalidCommand();

                                } else {

                                    queryHandler.viewMessages(isAdmin);

                                }

                            } else if ((messageContent[1] + " " + messageContent[2]).equalsIgnoreCase(
                                    MethodID.DELETEJAR.getCommand())) {

                                if (messageContent.length > 3){

                                    queryHandler.invalidCommand();

                                } else {

                                    queryHandler.deleteJar(isAdmin);

                                }

                            } else {

                                queryHandler.invalidCommand();

                            }

                        } else if (messageContent.length >= 2) {

                            if (messageContent[1].equalsIgnoreCase(MethodID.HELLO.getCommand())) {

                                queryHandler.hello();

                            } else if (messageContent[1].equalsIgnoreCase(MethodID.HELP.getCommand())) {

                                queryHandler.getHelp();

                            } else {

                                queryHandler.invalidCommand();

                            }

                        } else {

                            queryHandler.invalidCommand();

                        }

                    }

                }

            }

        }).start());

    }

}

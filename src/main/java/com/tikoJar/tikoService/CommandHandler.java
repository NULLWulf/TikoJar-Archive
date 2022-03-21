package com.tikoJar.tikoService;

import com.tikoJar.DTO.QueryHandler;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.util.StringTokenizer;

// TODO: Use enumeration
// TODO: Check for tokens instead of char length

public class CommandHandler {

    private enum MethodID{

        ADDMESSAGE("add"),
        COMMANDPREFIX("!tiko"),
        CREATEJAR("create"),
        DELETEJAR("delete jar"),
        DELETEMESSAGE("delete message"),
        HELLO("hello"),
        HELP("help"),
        VIEWMESSAGES("view messages");

        private String command;

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

        api.addMessageCreateListener(event -> {

            new Thread(() -> {

                String[] messageContent = event.getMessageContent().split("\\s");

                // Proceed only if message has content
                if (messageContent.length > 0) {

                    // Proceed only if message begins with !tiko
                    if (messageContent[0].equalsIgnoreCase(MethodID.COMMANDPREFIX.getCommand())) {

                        // Determine whether message author is server admin
                        boolean isAdmin = event.getMessageAuthor().isServerAdmin();

                        QueryHandler queryHandler = new QueryHandler(event);

                        if (messageContent.length >= 3) {

                            if (messageContent[1].equalsIgnoreCase(MethodID.ADDMESSAGE.getCommand())) {

                                queryHandler.addMessage();

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

                                // TODO: check syntax and update validSyntax boolean

                                queryHandler.createJar(validSyntax, isAdmin);

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

            }).start();

        });

    }

}

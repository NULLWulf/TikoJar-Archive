package com.tikoJar.DTO;

import com.tikoJar.DAO.Jar;
import jakarta.json.JsonObject;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

public class ResponseBuilder {

    private String responseCode;
    private final MessageCreateEvent event;
    private Jar jar;

    public ResponseBuilder(JsonObject responseObject, MessageCreateEvent event){

        this.event = event;
        deserializeResponse(responseObject);

    }

    public void deserializeResponse(JsonObject responseObject){

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
                    "Please go to OUR WEB URL to see how to create a jar.");

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

    public void viewMessagesResponse(){

    }

    public void deleteMessageResponse(){

    }

    public void deleteJarResponse(){

    }

    public void getHelpResponse(){

        event.getChannel().sendMessage("" +
                "**Here's a list of my commands:** ```" +
                "!tiko add <message>\n" +
                "!tiko hello\n" +
                "!tiko help\n" +
                "!tiko view messages```");

    }

    public void timeLimitEvent(){

    }

    public void messageLimitEvent(){

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

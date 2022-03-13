package com.tikoJar.DTO;

import com.tikoJar.DAO.Jar;
import jakarta.json.JsonObject;
import org.javacord.api.event.message.MessageCreateEvent;

public class ResponseBuilder {
    private String responseCode;
    private JsonObject responseObject;
    private MessageCreateEvent event;
    private Jar jar;

    public ResponseBuilder(JsonObject responseObject, MessageCreateEvent event){
        this.responseObject = responseObject;
        this.event = event;
    }

    public void deserializeResponse(){

    }

    public void addMessageResponse(){

    }

    public void createJarResponse(){

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

    //"" + "**Here's a list of my commands:** ```" + "!tiko add <message>\n" + "!tiko hello\n") + "!tiko help\n" + "!tiko view messages```"

    public void timeLimitEvent(){

    }

    public void messageLimitEvent(){

    }
}

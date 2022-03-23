package com.tikoJar.tests;

import com.tikoJar.DAO.Jar;
import com.tikoJar.DAO.Message;
import com.tikoJar.DAO.OpeningCondition;
import okhttp3.*;

import java.time.LocalDate;
import java.util.ArrayList;

import static java.util.Objects.requireNonNull;

public class testInserts {

    public testInserts(){};

    public void run(){

        OpeningCondition openingCondition = new OpeningCondition(true, 10, LocalDate.now(), null,"ABC123");

        ArrayList<Message> messages = new ArrayList<>();

        Message message1 = new Message("Bob", LocalDate.now(),"Something positive");
        Message message2 = new Message("Samander", LocalDate.now(),"Something positive again");
        Message message3 = new Message("Jake", LocalDate.now(),"Something negative");

        messages.add(message1);
        messages.add(message2);
        messages.add(message3);

        Jar jar = new Jar("ABC123",openingCondition,messages);

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(Auth.getDBString()+"\"document\": " + army + "}",mediaType);
        Request request = new Request.Builder()
                .url("https://data.mongodb-api.com/app/data-okszo/endpoint/data/beta/action/insertOne")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Access-Control-Request-Headers", "*")
                .addHeader("api-key", Auth.getMongoKey())
                .build();
        Response response = client.newCall(request).execute();
        JSON_Handler json = new JSON_Handler();
        System.out.println(json.getObjAsJSONString(requireNonNull(response.body()).string()));
        System.out.println(response.code());
    }
    }
}

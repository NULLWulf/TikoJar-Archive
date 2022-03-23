package com.tikoJar.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tikoJar.DAO.Jar;
import com.tikoJar.DAO.Message;
import com.tikoJar.DAO.OpeningCondition;
import okhttp3.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import static java.util.Objects.requireNonNull;

public class testInserts {

    public testInserts(){};

    public static void run() throws IOException {

        String databaseStr = "{\"collection\":\"Jars\",\"database\":\"TikoJarTest\",\"dataSource\":\"PositivityJar\",";

        JSON_Handler json_handler = new JSON_Handler();

        OpeningCondition openingCondition = new OpeningCondition(true, 10, "Hi", null,"ABC123");

        ArrayList<Message> messages = new ArrayList<>();

        Message message1 = new Message("Bob", "Hi","Something positive");
        Message message2 = new Message("Samander", "Hi","Something positive again");
        Message message3 = new Message("Jake", "Hi","Something negative");

        messages.add(message1);
        messages.add(message2);
        messages.add(message3);

        Jar jar = new Jar("ABC123",openingCondition,messages);

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(databaseStr+"\"document\": " + json_handler.getObjAsJSONString(jar) + "}",mediaType);
        Request request = new Request.Builder()
                .url("https://data.mongodb-api.com/app/data-okszo/endpoint/data/beta/action/insertOne")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Access-Control-Request-Headers", "*")
                .addHeader("api-key", "TUGyzJPmesVH4FcrDqO0XovgYNq0L5B59xCnjFsB9nLFE7qkofdTvzYjBn2ID120")
                .build();

        Response response = client.newCall(request).execute();
        JSON_Handler json = new JSON_Handler();
        System.out.println(json.getObjAsJSONString(requireNonNull(response.body()).string()));
        System.out.println(response.code());
    }

}

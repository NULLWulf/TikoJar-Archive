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

//    public static void run() throws IOException {
////        JSON_Handler json_handler = new JSON_Handler();
////        ArrayList<Message> messages = new ArrayList<>();
////
//////        Message message1 = new Message("Bob", "Hi","Something positive");
//////        Message message2 = new Message("Samander", "Hi","Something positive again");
//////        Message message3 = new Message("Jake", "Hi","Something negative");
////
////        Jar jar = new Jar("ABC500",openingCondition,messages);
//
//        String newDBStr = """
//                {"collection":"Jars",
//                "database":"TikoJarTest",
//                "dataSource":"PositivityJar",
//                "document":%s}
//                """.formatted(json_handler.getObjAsJSONString(jar)).stripIndent();
//
//        OkHttpClient client = new OkHttpClient().newBuilder().build();
//        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(newDBStr, mediaType);
//        Request request = new Request.Builder()
//                .url("https://data.mongodb-api.com/app/data-rlgbq/endpoint/data/beta/action/insertOne")
//                .method("POST", body)
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Access-Control-Request-Headers", "*")
//                .addHeader("api-key", "TUGyzJPmesVH4FcrDqO0XovgYNq0L5B59xCnjFsB9nLFE7qkofdTvzYjBn2ID120")
//                .build();
//
//        json_handler.displayObjectAsJson(jar);
//        Response response = client.newCall(request).execute();
//        JSON_Handler json = new JSON_Handler();
//        System.out.println(json.getObjAsJSONString(requireNonNull(response.body()).string()));
//        System.out.println(response.code());
//    }

}

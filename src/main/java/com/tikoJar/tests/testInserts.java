package com.tikoJar.tests;

import java.io.IOException;

public class testInserts {

    public testInserts(){}

    public static void run() throws IOException {
//        JSON_Handler json_handler = new JSON_Handler();
//        ArrayList<Message> messages = new ArrayList<>();
//
//
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
    }

//    public void testAddMessage(String serverId, String user, String messageBody) throws IOException {
//
//
//
//        OkHttpClient client = new OkHttpClient().newBuilder().build();
//        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(addMessageQuery, mediaType);
//        Request request = new Request.Builder()
//                .url("https://data.mongodb-api.com/app/data-rlgbq/endpoint/data/beta/action/updateOne")
//                .method("POST", body)
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Access-Control-Request-Headers", "*")
//                .addHeader("api-key", "TUGyzJPmesVH4FcrDqO0XovgYNq0L5B59xCnjFsB9nLFE7qkofdTvzYjBn2ID120")
//                .build();
//
//        Response response = client.newCall(request).execute();
//
//        System.out.println(json.getObjAsJSONString(requireNonNull(response.body()).string()));
//        System.out.println(response.code());
//
//    }

}

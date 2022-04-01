package com.tikoJar.DTO;

import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

public class HTTPClient {

    public static final Logger LOGGER = LogManager.getLogger("HTTPClient.class");
    String url = "https://data.mongodb-api.com/app/data-rlgbq/endpoint/data/beta/action/%s".formatted(endPoint);

    OkHttpClient client;
    MediaType mediaType;
    RequestBody body;

    public HTTPClient(){
        client = new OkHttpClient().newBuilder().build();
    }

    public void prepareMongoDBAtlasRequest(String query, String endPoint){
        this.mediaType = MediaType.parse("application/json");
        body = RequestBody.create(query, mediaType);
    }

    public String processQuery(String query, String endPoint) {
        try {

            Request request = new Request.Builder()
                    .url(url)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Access-Control-Request-Headers", "*")
                    .addHeader("api-key", "TUGyzJPmesVH4FcrDqO0XovgYNq0L5B59xCnjFsB9nLFE7qkofdTvzYjBn2ID120")
                    .build();
            Response response = client.newCall(request).execute();  // execute the request
            LOGGER.debug("Process Query for Endpoint %s Called for %s %s - \nQuery: \n%s".formatted(endPoint, serverName, serverId, query));
            String tempResponse = Objects.requireNonNull(response.body()).string();
            response.close();
            return tempResponse;
        } catch (IOException e) {
            LOGGER.warn(e.getMessage());
            return defaultEmpty;
        }
    }
}

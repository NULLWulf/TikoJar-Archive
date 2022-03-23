package com.tikoJar.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikoJar.DAO.Jar;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class JSON_Handler {

    // Creates object mapper
    ObjectMapper objectMapper = new ObjectMapper();

    public void ObjectMapper()
    {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }
    // simply returns passed object as formatted JSON string
    public String getObjAsJSONString(Object object) throws JsonProcessingException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);

    }
    public void displayObjectAsJson(Object object) throws JsonProcessingException {objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


        System.out.println(this.getObjAsJSONString(object));
    }

    // Writes passed object as JSON file based on it's specified path and passed object
    public void writeObjectAsJson(String objectName,Object object) throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get("jsonfiles/" + objectName + ".json").toFile(),object);
    }

    // Specific Function for returning army saved as JSON file
    public Jar getJarJson(String path) throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(new File(path), Jar.class);
    }

}

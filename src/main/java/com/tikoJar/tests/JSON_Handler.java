package com.tikoJar.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikoJar.DAO.Jar;

public class JSON_Handler {

    // Creates object mapper
    ObjectMapper objectMapper = new ObjectMapper();

    public void ObjectMapper()
    {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    // simply returns passed object as formatted JSON string
    public String getObjAsJSONString(Object object) throws JsonProcessingException {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);

    }
    public void displayObjectAsJson(Object object) throws JsonProcessingException {
        System.out.println(this.getObjAsJSONString(object));
    }

}
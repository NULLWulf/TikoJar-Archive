package com.tikoJar.DTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSON_Handler {

    // Creates object mapper
    ObjectMapper objectMapper = new ObjectMapper();

    public void ObjectMapper()
    {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    // simply returns passed object as formatted JSON string
    public String getObjAsJSONString(Object object)  {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        }catch (JsonProcessingException e){
            e.printStackTrace();
            return null;
        }
    }
    public void displayObjectAsJson(Object object)  {
        System.out.println(this.getObjAsJSONString(object));
    }

}

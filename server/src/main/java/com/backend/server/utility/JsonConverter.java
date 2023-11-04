package com.backend.server.utility;



import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;


//Json converter luokka, joka käyttää AttributeConverter rajapintaa, jotta voidaan tallentaa ja hakea käyttöön esim. company settings JSON muodossa
public class JsonConverter implements AttributeConverter<Map<String, Object>, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    // muuntaa Map olion JSON stringiksi tietokantaa varten
    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Map muunnos JSON string fail", e);
        }
    }

    // muuntaa JSON stringin Map olioksi kun tieto haetaan tietokannasta
    @Override
    public Map<String, Object> convertToEntityAttribute(String databaseData) {
        try {
            // typereferencellä varmistetaan type safety
            return objectMapper.readValue(databaseData, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Json string muunnos Map fail", e);
        }
    }
    
}

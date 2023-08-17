package com.trainingpeaks.db;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.trainingpeaks.data.*;

import java.io.File;
import java.io.IOException;

public class JsonDbClient extends StreamDbClient{
    public JsonDbClient(String exercisesFilename, String usersFilename, String workoutsFilename) throws IOException {
        super(null, null, null);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        users = objectMapper.readValue(new File(usersFilename), new TypeReference<>(){});
        exercises = objectMapper.readValue(new File(exercisesFilename), new TypeReference<>(){});
        workouts = objectMapper.readValue(new File(workoutsFilename), new TypeReference<>(){});
    }
}

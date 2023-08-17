package com.trainingpeaks.db;

import com.trainingpeaks.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonDbClientTest {

    private JsonDbClient jsonDbClient;
    @BeforeEach
    public void setup() throws IOException, URISyntaxException {
        String exercisesPath = Path.of(this.getClass().getResource("/exercises.json").toURI()).toString();
        String usersPath = Path.of(this.getClass().getResource("/users.json").toURI()).toString();
        String workoutsPath = Path.of(this.getClass().getResource("/workouts.json").toURI()).toString();
        jsonDbClient = new JsonDbClient(exercisesPath, usersPath, workoutsPath);
    }

    @Test
    public void standard_getExerciseByName(){
        Exercise expectedExercise = ImmutableExercise.builder()
                .id(568)
                .name("Bench Press")
                .build();

        Exercise actualExercise = jsonDbClient.getExerciseByName("Bench Press").get();

        assertEquals(expectedExercise, actualExercise);
    }

    @Test
    public void standard_getUserByName(){

        List<User> expectedUsers = List.of(ImmutableUser.builder()
                .id(5101)
                .firstName("Abby")
                .lastName("Smith")
                .build());

        List<User> actualUser= jsonDbClient.getUserByName("Abby", "Smith");

        assertEquals(expectedUsers, actualUser);
    }

}

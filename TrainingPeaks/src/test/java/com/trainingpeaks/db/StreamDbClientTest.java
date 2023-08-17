package com.trainingpeaks.db;

import com.trainingpeaks.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StreamDbClientTest {

    private StreamDbClient streamDbClient;
    private List<User> users;
    private List<Exercise> exercises;
    private List<Workout> workouts;
    @BeforeEach
    public void setup(){
        users = List.of(ImmutableUser.builder()
                .id(1)
                .firstName("Alan")
                .lastName("Nguyen")
                .build());
        exercises = List.of(ImmutableExercise.builder()
                        .name("benchpress")
                        .id(1)
                        .build());
        workouts = List.of(ImmutableWorkout.builder()
                        .userId(users.get(0).id())
                        .dateCompleted(LocalDate.of(2021,12,01))
                        .addBlocks(ImmutableBlock.builder()
                                .exerciseId(exercises.get(0).id())
                                .addSets(ImmutableSet.builder()
                                        .reps(1)
                                        .weight(5)
                                        .build())
                                .build())
                        .build(),
                        ImmutableWorkout.builder()
                        .userId(users.get(0).id())
                        .dateCompleted(LocalDate.of(2022,12,01))
                        .addBlocks(ImmutableBlock.builder()
                                .exerciseId(exercises.get(0).id())
                                .addSets(ImmutableSet.builder()
                                        .reps(1)
                                        .weight(5)
                                        .build())
                                .build())
                        .build(),
                ImmutableWorkout.builder()
                        .userId(users.get(0).id())
                        .dateCompleted(LocalDate.of(2023,12,01))
                        .addBlocks(ImmutableBlock.builder()
                                .exerciseId(exercises.get(0).id())
                                .addSets(ImmutableSet.builder()
                                        .reps(1)
                                        .weight(5)
                                        .build())
                                .build())
                        .build()
                );
        streamDbClient = new StreamDbClient(exercises, users, workouts);
    }

    @Test
    public void standard_getUserWorkouts(){
        List<Workout> expectedWorkouts = workouts;

        List<Workout> actualWorkouts = streamDbClient.getUserWorkouts(users.get(0));

        assertEquals(expectedWorkouts, actualWorkouts);
    }

    @Test
    public void standard_getUserWorkoutsWithDate(){
        List<Workout> expectedWorkouts = workouts.subList(1,2);

        List<Workout> actualWorkouts = streamDbClient.getUserWorkouts(users.get(0), LocalDate.of(2022, 06, 01), LocalDate.of(2023, 06,01));

        assertEquals(expectedWorkouts, actualWorkouts);
    }

    @Test
    public void standard_getExerciseByName(){
        Exercise expectedExercise = exercises.get(0);

        Optional<Exercise> exerciseOptional = streamDbClient.getExerciseByName("benchpress");

        assertEquals(expectedExercise, exerciseOptional.get());
    }

    @Test
    public void standard_getUserByName(){
        List<User> expectedUsers = List.of(users.get(0));

        List<User> actualUsers = streamDbClient.getUserByName("Alan", "Nguyen");

        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    public void standard_getWorkoutsByExercise(){
        List<Workout> expectedWorkouts = workouts;

        List<Workout> actualWorkouts = streamDbClient.getWorkoutByExercise(exercises.get(0));

        assertEquals(expectedWorkouts, actualWorkouts);
    }
}

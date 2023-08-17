package com.trainingpeaks.aggregation;

import com.trainingpeaks.data.*;
import com.trainingpeaks.db.DbClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AggregatorTest {
    DbClient dbClient = mock(DbClient.class);
    private Aggregator aggregator;

    @BeforeEach
    public void setup(){
        aggregator = new Aggregator(dbClient);
    }


    @Test
    public void standard_totalAthleticBenchPress(){
        long expectedTotal = 1000;
        String exerciseName = "bench press";
        User user = ImmutableUser.builder()
                .firstName("test")
                .lastName("user")
                .id(314)
                .build();
        Exercise exercise = ImmutableExercise.builder()
                .id(1)
                .name(exerciseName)
                .build();
        Set set = ImmutableSet.builder()
                .weight(200)
                .reps(5)
                .build();
        Block block = ImmutableBlock.builder()
                .addSets(set)
                .exerciseId(exercise.id())
                .build();
        Workout workout = ImmutableWorkout.builder()
                .userId(user.id())
                .addBlocks(block)
                .dateCompleted(LocalDate.of(2023, 12, 01))
                .build();
        when(dbClient.getWorkoutByExercise(exercise)).thenReturn(List.of(workout));
        when(dbClient.getExerciseByName(exerciseName)).thenReturn(Optional.of(exercise));

        long actualTotal = aggregator.totalUserExercise(exerciseName);

        assertEquals(expectedTotal, actualTotal);
    }

    @Test
    public void standard_userTotalWeightByExerciseAndDate(){

        String firstname = "Alan";
        String lastname = "Nguyen";
        LocalDate from = LocalDate.of(2021, 01, 01);
        LocalDate to = LocalDate.of(2022, 01, 01);
        String exerciseName = "back squat";
        User user = ImmutableUser.builder()
                .firstName(firstname)
                .lastName(lastname)
                .id(314)
                .build();
        Exercise exercise = ImmutableExercise.builder()
                .name(exerciseName)
                .id(2)
                .build();
        Set set = ImmutableSet.builder()
                .weight(2000)
                .reps(5)
                .build();
        Block block = ImmutableBlock.builder()
                .addSets(set)
                .exerciseId(exercise.id())
                .build();
        Workout workout = ImmutableWorkout.builder()
                .userId(user.id())
                .addBlocks(block)
                .dateCompleted(LocalDate.of(2022, 06, 01))
                .build();
        when(dbClient.getUserByName(firstname, lastname)).thenReturn(List.of(user));
        when(dbClient.getExerciseByName(exerciseName)).thenReturn(Optional.of(exercise));
        when(dbClient.getUserWorkouts(user,from, to)).thenReturn(List.of(workout));

        long expectedWeight = 10000;

        long actualWeight = aggregator.userTotalWeightByExerciseAndDate(firstname, lastname, from, to, exerciseName);

        assertEquals(expectedWeight, actualWeight);
    }

    @Test
    public void userHighestLiftByMonth(){

        String firstname = "Alan";
        String lastname = "Nguyen";
        String exerciseName = "back squat";
        User user = ImmutableUser.builder()
                .firstName(firstname)
                .lastName(lastname)
                .id(314)
                .build();
        Exercise exercise = ImmutableExercise.builder()
                .name(exerciseName)
                .id(2)
                .build();
        Set set = ImmutableSet.builder()
                .weight(2000)
                .reps(5)
                .build();
        Block block = ImmutableBlock.builder()
                .addSets(set)
                .exerciseId(exercise.id())
                .build();
        Workout workout = ImmutableWorkout.builder()
                .userId(user.id())
                .addBlocks(block)
                .dateCompleted(LocalDate.of(2022, 06, 01))
                .build();
        Set set2 = ImmutableSet.builder()
                .weight(2000)
                .reps(6)
                .build();
        Block block2 = ImmutableBlock.builder()
                .addSets(set2)
                .exerciseId(exercise.id())
                .build();
        Workout workout2 = ImmutableWorkout.builder()
                .userId(user.id())
                .addBlocks(block2)
                .dateCompleted(LocalDate.of(2022, 07, 01))
                .build();
        when(dbClient.getUserByName(firstname, lastname)).thenReturn(List.of(user));
        when(dbClient.getExerciseByName(exerciseName)).thenReturn(Optional.of(exercise));
        when(dbClient.getUserWorkouts(user)).thenReturn(List.of(workout, workout2));

        YearMonth expectedYearMonth = YearMonth.of(2022, 07);

        YearMonth actualLiftByMonth = aggregator.userHighestLiftByMonth(firstname, lastname, exerciseName);

        assertEquals(expectedYearMonth, actualLiftByMonth);
    }

    @Test
    public void nonexistentLift_userHighestLiftByMonth(){

        String firstname = "Alan";
        String lastname = "Nguyen";
        String exerciseName = "back squat";
        User user = ImmutableUser.builder()
                .firstName(firstname)
                .lastName(lastname)
                .id(314)
                .build();
        Exercise exercise = ImmutableExercise.builder()
                .name(exerciseName)
                .id(2)
                .build();
        Set set = ImmutableSet.builder()
                .weight(2000)
                .reps(5)
                .build();
        Block block = ImmutableBlock.builder()
                .addSets(set)
                .exerciseId(exercise.id())
                .build();
        Workout workout = ImmutableWorkout.builder()
                .userId(1)
                .addBlocks(block)
                .userId(user.id())
                .dateCompleted(LocalDate.of(2022, 06, 01))
                .build();
        Set set2 = ImmutableSet.builder()
                .weight(2000)
                .reps(6)
                .build();
        Block block2 = ImmutableBlock.builder()
                .addSets(set2)
                .exerciseId(exercise.id())
                .build();
        Workout workout2 = ImmutableWorkout.builder()
                .userId(user.id())
                .addBlocks(block2)
                .dateCompleted(LocalDate.of(2022, 07, 01))
                .build();
        when(dbClient.getUserByName(firstname, lastname)).thenReturn(List.of(user));
        when(dbClient.getExerciseByName(exerciseName)).thenReturn(Optional.of(exercise));
        when(dbClient.getUserWorkouts(user)).thenReturn(List.of(workout, workout2));

        assertThrows(RuntimeException.class, () ->aggregator.userHighestLiftByMonth(firstname, lastname, "bench press"));
    }

}

package com.trainingpeaks.db;

import com.trainingpeaks.data.Exercise;
import com.trainingpeaks.data.User;
import com.trainingpeaks.data.Workout;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DbClient {

    List<Workout> getWorkoutByExercise(Exercise exercise);

    List<Workout> getUserWorkouts(User user, LocalDate from, LocalDate to);

    List<Workout> getUserWorkouts(User user);

    Optional<Exercise> getExerciseByName(String exerciseName);

    List<User> getUserByName(String firstname, String lastname);

}

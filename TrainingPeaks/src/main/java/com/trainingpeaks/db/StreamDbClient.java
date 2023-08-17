package com.trainingpeaks.db;

import com.trainingpeaks.data.Block;
import com.trainingpeaks.data.Exercise;
import com.trainingpeaks.data.User;
import com.trainingpeaks.data.Workout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StreamDbClient implements DbClient{
    protected List<Exercise> exercises;
    protected List<User> users;
    protected List<Workout> workouts;

    public StreamDbClient(List<Exercise> exercises, List<User> users, List<Workout> workouts){
        this.exercises = exercises;
        this.users = users;
        this.workouts = workouts;
    }

    @Override
    public List<Workout> getWorkoutByExercise(Exercise exercise) {
        List<Workout> foundWorkouts = new ArrayList<>();
        for(Workout workout : workouts){
            boolean addWorkout = false;
            for(Block block : workout.blocks()){
                if(block.exerciseId() == exercise.id()){
                    addWorkout = true;
                    break;
                }
            }
            if(addWorkout){
                foundWorkouts.add(workout);
            }
        }
        return foundWorkouts;
    }

    @Override
    public List<Workout> getUserWorkouts(User user, LocalDate from, LocalDate to) {
        return workouts.stream()
                .filter(workout -> workout.userId() == (user.id()))
                .filter(workout -> workout.dateCompleted().isBefore(to))
                .filter(workout -> workout.dateCompleted().isAfter(from))
                .toList();
    }

    @Override
    public List<Workout> getUserWorkouts(User user) {
        return workouts.stream()
                .filter(workout -> workout.userId()==(user.id()))
                .toList();
    }

    @Override
    public Optional<Exercise> getExerciseByName(String exerciseName) {
        List<Exercise> foundExercises = exercises.stream()
                .filter(exercise -> exercise.name().equals(exerciseName))
                .toList();
        if(foundExercises.size() > 1){
            throw new DBException("DB EXCEPTION, MORE THAN ONE EXERCISE FOUND FOR NAME:" + exerciseName);
        }
        if(foundExercises.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(foundExercises.get(0));
    }

    @Override
    public List<User> getUserByName(String firstname, String lastname) {
        return users.stream().filter(user -> user.firstName().equals(firstname) &&
                        user.lastName().equals(lastname))
                .toList();
    }
}

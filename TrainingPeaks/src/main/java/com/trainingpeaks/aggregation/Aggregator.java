package com.trainingpeaks.aggregation;

import com.trainingpeaks.data.Block;
import com.trainingpeaks.data.Exercise;
import com.trainingpeaks.data.User;
import com.trainingpeaks.data.Workout;
import com.trainingpeaks.data.Set;
import com.trainingpeaks.db.DbClient;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class Aggregator {
    DbClient dbClient;
    public Aggregator(DbClient dbClient){
     this.dbClient = dbClient;
    }

    public long totalUserExercise(String exerciseName){
        Exercise exercise = dbClient.getExerciseByName(exerciseName).orElseThrow(() -> new RuntimeException("Exercise not found"));
        List<Workout> exerciseWorkouts = dbClient.getWorkoutByExercise(exercise);

       return totalWeightFromWorkoutsByExercise(exerciseWorkouts, exercise);
    }

    public long userTotalWeightByExerciseAndDate(String firstname, String lastname, LocalDate from, LocalDate to, String exerciseName){
        User user = findUser(firstname, lastname);
        Exercise exercise = dbClient.getExerciseByName(exerciseName).orElseThrow(() -> new RuntimeException("No exercise found for:" + exerciseName));
        List<Workout> workouts = dbClient.getUserWorkouts(user, from, to);

        return totalWeightFromWorkoutsByExercise(workouts, exercise);
    }

    public YearMonth userHighestLiftByMonth(String firstname, String lastname, String exerciseName){
        User user = findUser(firstname, lastname);
        Exercise exercise = getExercise(exerciseName);
        List<Workout> workouts = dbClient.getUserWorkouts(user);

        Map<YearMonth, List<Workout>> monthSplitWorkouts = splitByMonth(workouts);

        long highestWeight = 0;
        YearMonth highestYearMonth = null;

        for( YearMonth yearMonth : monthSplitWorkouts.keySet()){
            long monthWeight = totalWeightFromWorkoutsByExercise(monthSplitWorkouts.get(yearMonth), exercise);

            if (monthWeight > highestWeight){
                highestWeight = monthWeight;
                highestYearMonth = yearMonth;
            }
        }

        if(highestYearMonth == null){
            throw new RuntimeException("no workouts found of type:" + exercise.name());
        }

        return highestYearMonth;
    }

    public long userAllTimeHighestPr(String firstname, String lastname, String exerciseName){
        User user = findUser(firstname, lastname);
        Exercise exercise = getExercise(exerciseName);
        List<Workout> workouts = dbClient.getUserWorkouts(user);
        long max = 0;

        for(Workout workout : workouts) {
            for(Block block : workout.blocks()){
                if(block.exerciseId()==exercise.id()){
                    for( Set set : block.sets()){
                        if(set.weight() > max && set.reps() > 0){
                            max = set.weight();
                        }
                    }
                }
            }
        }
        return max;
    }

    private Map<YearMonth, List<Workout>> splitByMonth(List<Workout> workouts){
        Map<YearMonth, List<Workout>> monthSplitWorkouts = new HashMap<>();
        for(Workout workout : workouts){
           YearMonth yearmonth = YearMonth.of(workout.dateCompleted().getYear(), workout.dateCompleted().getMonth());
           if(!monthSplitWorkouts.containsKey(yearmonth)){
               monthSplitWorkouts.put(yearmonth, new ArrayList<>());
           }

           monthSplitWorkouts.get(yearmonth).add(workout);
        }
        return monthSplitWorkouts;
    }

    private List<Block> blocksByExercise(Workout workout, Exercise exercise){
        List<Block> blocks = new ArrayList<>();
        for( Block block : workout.blocks()){
            if(block.exerciseId() == (exercise.id())){
                blocks.add(block);
            }
        }
        return blocks;
    }

    private long totalWeightFromWorkoutsByExercise(List<Workout> workouts, Exercise exercise){
        long totalWeight = 0;
        for(Workout workout : workouts) {
            List<Block> benchpressBlocks = blocksByExercise(workout, exercise);
            for (Block block : benchpressBlocks) {
                for (Set set : block.sets()) {
                    totalWeight += calculateSetWeight(set);
                }
            }
        }
        return totalWeight;
    }

    private long calculateSetWeight(Set set){
        return set.reps() * set.weight();
    }

    private User findUser(String firstname, String lastname){
        List<User> foundUsers = dbClient.getUserByName(firstname, lastname);
        if(foundUsers.size() > 1){
            throw new RuntimeException("Multiple users of name:" + firstname + " " + lastname + " found");
        }
        if(foundUsers.isEmpty()){
            throw new RuntimeException("No user of name:" + firstname + " " + lastname + " found");
        }
        return foundUsers.get(0);
    }

    private Exercise getExercise(String exerciseName){
        return dbClient.getExerciseByName(exerciseName).orElseThrow(() -> new RuntimeException("No exercise found for:" + exerciseName));
    }

}

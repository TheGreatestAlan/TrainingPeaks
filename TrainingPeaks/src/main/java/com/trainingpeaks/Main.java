package com.trainingpeaks;

import com.trainingpeaks.aggregation.Aggregator;
import com.trainingpeaks.data.User;
import com.trainingpeaks.db.DbClient;
import com.trainingpeaks.db.JsonDbClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) throws URISyntaxException {

        String exercisesPath = Path.of(Main.class.getResource("/exercises.json").toURI()).toString();
        String usersPath = Path.of(Main.class.getResource("/users.json").toURI()).toString();
        String workoutsPath = Path.of(Main.class.getResource("/workouts.json").toURI()).toString();

        DbClient dbClient = null;

        try {
            dbClient = new JsonDbClient(exercisesPath, usersPath, workoutsPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Aggregator aggregator = new Aggregator(dbClient);

        System.out.println("1. How many total pounds have all of these athletes combined Bench Pressed?");
        System.out.println(aggregator.totalUserExercise("Bench Press"));
        System.out.println("2. How many total pounds did Barry Moore Back Squat in 2016?");
        System.out.println(aggregator.userTotalWeightByExerciseAndDate("Barry", "Moore", LocalDate.of(2016, 01, 01), LocalDate.of(2017, 01, 01), "Back Squat"));
        System.out.println("3. In what month of 2017 did Barry Moore Back Squat the most total weight?");
        System.out.println(aggregator.userHighestLiftByMonth("Barry","Moore","Back Squat"));
        System.out.println("4. What is Abby Smith's all-time Bench Press PR weight?");
        System.out.println(aggregator.userAllTimeHighestPr("Abby", "Smith", "Bench Press"));


    }
}

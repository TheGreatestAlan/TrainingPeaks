package com.trainingpeaks.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableWorkout.class)
@JsonDeserialize(as = ImmutableWorkout.class)
public interface Workout {
    @JsonProperty("user_id")
    long userId();

    @JsonProperty("datetime_completed")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDate dateCompleted();
    List<Block> blocks();
}

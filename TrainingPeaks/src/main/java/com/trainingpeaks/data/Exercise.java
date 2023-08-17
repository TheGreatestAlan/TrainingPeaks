package com.trainingpeaks.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableExercise.class)
@JsonDeserialize(as = ImmutableExercise.class)
public interface Exercise {
    long id();
    @JsonProperty("title")
    String name();
}

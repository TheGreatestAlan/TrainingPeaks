package com.trainingpeaks.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableBlock.class)
@JsonDeserialize(as = ImmutableBlock.class)

public interface Block {
    @JsonProperty("exercise_id")
    long exerciseId();
    List<Set> sets();
}

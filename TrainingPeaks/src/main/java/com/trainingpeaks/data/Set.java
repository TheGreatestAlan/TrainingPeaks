package com.trainingpeaks.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableSet.class)
@JsonDeserialize(as = ImmutableSet.class)
public interface Set {
   int reps();
   int weight();
}


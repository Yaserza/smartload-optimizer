package com.smartload.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Truck {

    @JsonProperty("id")
    private String id;

    @JsonProperty("max_weight_lbs")
    private long maxWeightLbs;

    @JsonProperty("max_volume_cuft")
    private long maxVolumeCuft;
}


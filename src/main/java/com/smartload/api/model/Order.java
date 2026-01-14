package com.smartload.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @JsonProperty("id")
    private String id;

    @JsonProperty("payout_cents")
    private long payoutCents;

    @JsonProperty("weight_lbs")
    private long weightLbs;

    @JsonProperty("volume_cuft")
    private long volumeCuft;

    @JsonProperty("origin")
    private String origin;

    @JsonProperty("destination")
    private String destination;

    @JsonProperty("pickup_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate pickupDate;

    @JsonProperty("delivery_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate deliveryDate;

    @JsonProperty("is_hazmat")
    private boolean isHazmat;
}


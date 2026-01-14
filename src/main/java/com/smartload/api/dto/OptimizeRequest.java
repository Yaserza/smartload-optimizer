package com.smartload.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartload.api.model.Order;
import com.smartload.api.model.Truck;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptimizeRequest {

    @JsonProperty("truck")
    private Truck truck;

    @JsonProperty("orders")
    private List<Order> orders;
}

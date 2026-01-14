package com.smartload.api.controller;

import com.smartload.api.dto.OptimizeRequest;
import com.smartload.api.dto.OptimizeResponse;
import com.smartload.api.service.LoadOptimizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/load-optimizer")
@RequiredArgsConstructor
public class LoadOptimizerController {

    private final LoadOptimizerService optimizerService;

    @PostMapping("/optimize")
    public ResponseEntity<OptimizeResponse> optimize(@RequestBody OptimizeRequest request) {
        if (request == null || request.getTruck() == null || request.getOrders() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(optimizerService.optimize(request.getTruck(), request.getOrders()));
    }
}

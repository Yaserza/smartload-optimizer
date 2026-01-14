package com.smartload.api.service;

import com.smartload.api.dto.OptimizeResponse;
import com.smartload.api.model.Order;
import com.smartload.api.model.Truck;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class LoadOptimizerService {

    public OptimizeResponse optimize(Truck truck, List<Order> orders) {
        if (truck == null) return emptyResponse(null);
        if (orders == null || orders.isEmpty()) return emptyResponse(truck);

        int n = orders.size();
        long[] dp = new long[1 << n];
        boolean[] valid = new boolean[1 << n];
        valid[0] = true;

        for (int mask = 1; mask < (1 << n); mask++) {
            processMask(mask, orders, truck, dp, valid);
        }

        int bestMask = findBestMask(dp, valid);
        long bestRevenue = dp[bestMask];
        List<Order> bestSet = extractOrders(bestMask, orders);

        return buildResponse(truck, bestSet, bestRevenue);
    }

    /**
     * Process one subset mask
     */
    private void processMask(int mask, List<Order> orders, Truck truck,
                             long[] dp, boolean[] valid) {
        for (int i = 0; i < orders.size(); i++) {
            if ((mask & (1 << i)) != 0) {
                int prevMask = mask ^ (1 << i);
                if (!valid[prevMask]) continue;

                List<Order> subset = extractOrders(mask, orders);
                if (checkConstraints(subset, truck)) {
                    valid[mask] = true;
                    dp[mask] = subset.stream().mapToLong(Order::getPayoutCents).sum();
                }
            }
        }
    }

    /**
     * Find best mask using DP results
     */
    private int findBestMask(long[] dp, boolean[] valid) {
        return IntStream.range(0, dp.length)
                .filter(mask -> valid[mask])
                .boxed()
                .max((m1, m2) -> Long.compare(dp[m1], dp[m2]))
                .orElse(0);
    }

    /**
     * Extract orders corresponding to a bitmask
     */
    private List<Order> extractOrders(int mask, List<Order> orders) {
        return IntStream.range(0, orders.size())
                .filter(i -> (mask & (1 << i)) != 0)
                .mapToObj(orders::get)
                .toList();
    }

    /**
     * Check all constraints
     */
    private boolean checkConstraints(List<Order> subset, Truck truck) {
        return checkHazmat(subset)
                && checkRoute(subset)
                && checkTimeWindows(subset)
                && checkCapacity(subset, truck);
    }

    private boolean checkHazmat(List<Order> subset) {
        return subset.stream().filter(Order::isHazmat).count() <= 1;
    }

    private boolean checkRoute(List<Order> subset) {
        if (subset.isEmpty()) return true;
        String origin = subset.get(0).getOrigin();
        String destination = subset.get(0).getDestination();
        return subset.stream().allMatch(o ->
                origin.equals(o.getOrigin()) && destination.equals(o.getDestination()));
    }

    private boolean checkTimeWindows(List<Order> subset) {
        return subset.stream().allMatch(o ->
                o.getPickupDate() == null || o.getDeliveryDate() == null ||
                        !o.getPickupDate().isAfter(o.getDeliveryDate()));
    }

    private boolean checkCapacity(List<Order> subset, Truck truck) {
        long totalWeight = subset.stream().mapToLong(Order::getWeightLbs).sum();
        long totalVolume = subset.stream().mapToLong(Order::getVolumeCuft).sum();
        return totalWeight <= truck.getMaxWeightLbs()
                && totalVolume <= truck.getMaxVolumeCuft();
    }

    /**
     * Build final response
     */
    private OptimizeResponse buildResponse(Truck truck, List<Order> bestSet, long bestRevenue) {
        long totalWeight = bestSet.stream().mapToLong(Order::getWeightLbs).sum();
        long totalVolume = bestSet.stream().mapToLong(Order::getVolumeCuft).sum();

        return OptimizeResponse.builder()
                .truckId(truck.getId())
                .selectedOrderIds(bestSet.stream().map(Order::getId).toList())
                .totalPayoutCents(bestRevenue)
                .totalWeightLbs(totalWeight)
                .totalVolumeCuft(totalVolume)
                .utilizationWeightPercent(truck.getMaxWeightLbs() == 0 ? 0.0 :
                        (double) totalWeight / truck.getMaxWeightLbs() * 100.0)
                .utilizationVolumePercent(truck.getMaxVolumeCuft() == 0 ? 0.0 :
                        (double) totalVolume / truck.getMaxVolumeCuft() * 100.0)
                .build();
    }

    private OptimizeResponse emptyResponse(Truck truck) {
        return OptimizeResponse.builder()
                .truckId(truck == null ? null : truck.getId())
                .selectedOrderIds(List.of())
                .totalPayoutCents(0L)
                .totalWeightLbs(0L)
                .totalVolumeCuft(0L)
                .utilizationWeightPercent(0.0)
                .utilizationVolumePercent(0.0)
                .build();
    }
}

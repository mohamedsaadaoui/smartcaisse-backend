package tn.smartcaisse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.smartcaisse.dto.*;
import tn.smartcaisse.repository.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final OrderRepository orderRepo;
    private final OrderLineRepository orderLineRepo;

    @GetMapping("/daily")
    public ResponseEntity<ApiResponse<DailyReportDto>> daily(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        LocalDate target = date != null ? date : LocalDate.now();

        long totalOrders   = orderRepo.countByDate(target);
        double netRevenue  = orderRepo.sumRevenueByDate(target);
        long voided        = orderRepo.countVoidedByDate(target);
        double avgBasket   = totalOrders > 0 ? netRevenue / totalOrders : 0;

        // Top items
        List<TopItemDto> topItems = orderLineRepo.findTopItemsByDate(target).stream()
            .map(row -> new TopItemDto(
                (String) row[0],
                ((Number) row[1]).longValue(),
                ((Number) row[2]).doubleValue()
            )).toList();

        // Revenue by hour
        List<HourlyDto> byHour = orderLineRepo.findRevenueByHour(target).stream()
            .map(row -> new HourlyDto(
                (String) row[0],
                ((Number) row[1]).longValue(),
                ((Number) row[2]).doubleValue()
            )).toList();

        var report = DailyReportDto.builder()
            .date(target.toString())
            .totalOrders(totalOrders)
            .netRevenue(netRevenue)
            .voidedOrders(voided)
            .averageBasket(Math.round(avgBasket * 1000.0) / 1000.0)
            .topItems(topItems)
            .byHour(byHour)
            .build();

        return ResponseEntity.ok(ApiResponse.ok(report));
    }
}

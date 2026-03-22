package tn.smartcaisse.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailySummaryDto {
    private long totalOrders;
    private double netRevenue;
    private long voidedOrders;
    private LocalDateTime firstOrder;
    private LocalDateTime lastOrder;
}

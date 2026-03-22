package tn.smartcaisse.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyReportDto {
    private String date;
    private long totalOrders;
    private double netRevenue;
    private long voidedOrders;
    private double averageBasket;
    private List<TopItemDto> topItems;
    private List<HourlyDto> byHour;
}

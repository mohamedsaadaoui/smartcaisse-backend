package tn.smartcaisse.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockDto {
    private Long id;
    private String name;
    private String unit;
    private Double quantity;
    private Double lowThreshold;
    private boolean lowStock;
}

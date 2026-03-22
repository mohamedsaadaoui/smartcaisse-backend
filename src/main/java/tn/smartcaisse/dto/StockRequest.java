package tn.smartcaisse.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StockRequest {
    @NotBlank
    private String name;
    private String unit;
    @NotNull
    private Double quantity;
    private Double lowThreshold;
}

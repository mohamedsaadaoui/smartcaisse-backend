package tn.smartcaisse.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class OrderLineRequest {
    private Long itemId;
    @NotBlank
    private String itemName;
    @NotNull
    private Double unitPrice;
    @NotNull @Min(1)
    private Integer quantity;
    @NotNull
    private Double lineTotal;
}

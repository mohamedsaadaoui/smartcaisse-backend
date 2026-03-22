package tn.smartcaisse.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineDto {
    private Long id;
    private String itemName;
    private Double unitPrice;
    private Integer quantity;
    private Double lineTotal;
}

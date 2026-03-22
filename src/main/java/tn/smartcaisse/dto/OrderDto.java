package tn.smartcaisse.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private Integer orderNumber;
    private String type;
    private String status;
    private Double total;
    private String paymentMethod;
    private String cashierName;
    private String note;
    private LocalDateTime createdAt;
    private List<OrderLineDto> lines;
}

package tn.smartcaisse.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    @NotNull @NotEmpty
    private List<OrderLineRequest> lines;
    private String type;
    private String paymentMethod;
    private String cashierName;
    private String note;
    @NotNull
    private Double total;
}

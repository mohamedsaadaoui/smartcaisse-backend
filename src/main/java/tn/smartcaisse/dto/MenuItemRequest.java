package tn.smartcaisse.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MenuItemRequest {
    @NotBlank
    private String name;
    private String nameAr;
    @NotNull @Positive
    private Double price;
    @NotNull
    private Long categoryId;
    private boolean active = true;
}

package tn.smartcaisse.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemDto {
    private Long id;
    private String name;
    private String nameAr;
    private Double price;
    private Long categoryId;
    private String categoryName;
    private boolean active;
}

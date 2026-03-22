package tn.smartcaisse.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private String nameAr;
    private Integer sortOrder;
}

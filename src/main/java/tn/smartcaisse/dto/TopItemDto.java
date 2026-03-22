package tn.smartcaisse.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopItemDto {
    private String itemName;
    private long qtySold;
    private double revenue;
}

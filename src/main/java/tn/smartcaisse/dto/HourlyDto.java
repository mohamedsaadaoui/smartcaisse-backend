package tn.smartcaisse.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HourlyDto {
    private String hour;
    private long orders;
    private double revenue;
}

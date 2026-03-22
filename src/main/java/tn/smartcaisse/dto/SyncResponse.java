package tn.smartcaisse.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SyncResponse {
    private int synced;
    private int failed;
    private List<String> errors;
}

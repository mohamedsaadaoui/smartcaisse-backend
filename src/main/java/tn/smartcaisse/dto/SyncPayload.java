package tn.smartcaisse.dto;

import lombok.Data;
import java.util.List;

@Data
public class SyncPayload {
    private List<OrderRequest> orders;
    private String deviceId;
    private String syncedAt;
}

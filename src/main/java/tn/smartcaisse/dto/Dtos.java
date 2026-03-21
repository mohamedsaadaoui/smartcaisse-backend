package tn.smartcaisse.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import tn.smartcaisse.entity.Order;
import java.time.LocalDateTime;
import java.util.List;

// ── Auth ──────────────────────────────────────────────────────────────────────

@Data public class LoginRequest {
    @NotBlank String username;
    @NotBlank String password;
}

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private String role;
    private String fullName;
}

// ── Category ──────────────────────────────────────────────────────────────────

@Data public class CategoryRequest {
    @NotBlank String name;
    String nameAr;
    Integer sortOrder;
}

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private String nameAr;
    private Integer sortOrder;
}

// ── Menu Item ─────────────────────────────────────────────────────────────────

@Data public class MenuItemRequest {
    @NotBlank String name;
    String nameAr;
    @NotNull @Positive Double price;
    @NotNull Long categoryId;
    boolean active = true;
}

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class MenuItemDto {
    private Long id;
    private String name;
    private String nameAr;
    private Double price;
    private Long categoryId;
    private String categoryName;
    private boolean active;
}

// ── Order ─────────────────────────────────────────────────────────────────────

@Data public class OrderRequest {
    @NotNull @NotEmpty List<OrderLineRequest> lines;
    String type;
    String paymentMethod;
    String cashierName;
    String note;
    @NotNull Double total;
}

@Data public class OrderLineRequest {
    Long itemId;
    @NotBlank String itemName;
    @NotNull Double unitPrice;
    @NotNull @Min(1) Integer quantity;
    @NotNull Double lineTotal;
}

@Data @Builder @AllArgsConstructor @NoArgsConstructor
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

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class OrderLineDto {
    private Long id;
    private String itemName;
    private Double unitPrice;
    private Integer quantity;
    private Double lineTotal;
}

// ── Stock ─────────────────────────────────────────────────────────────────────

@Data public class StockRequest {
    @NotBlank String name;
    String unit;
    @NotNull Double quantity;
    Double lowThreshold;
}

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class StockDto {
    private Long id;
    private String name;
    private String unit;
    private Double quantity;
    private Double lowThreshold;
    private boolean lowStock;
}

// ── Reports ───────────────────────────────────────────────────────────────────

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class DailyReportDto {
    private String date;
    private long totalOrders;
    private double netRevenue;
    private long voidedOrders;
    private double averageBasket;
    private List<TopItemDto> topItems;
    private List<HourlyDto> byHour;
}

@Data @AllArgsConstructor @NoArgsConstructor
public class TopItemDto {
    private String itemName;
    private long qtySold;
    private double revenue;
}

@Data @AllArgsConstructor @NoArgsConstructor
public class HourlyDto {
    private String hour;
    private long orders;
    private double revenue;
}

// ── Sync ──────────────────────────────────────────────────────────────────────

@Data public class SyncPayload {
    private List<OrderRequest> orders;
    private String deviceId;
    private String syncedAt;
}

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class SyncResponse {
    private int synced;
    private int failed;
    private List<String> errors;
}

// ── Generic ───────────────────────────────────────────────────────────────────

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder().success(true).data(data).build();
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return ApiResponse.<T>builder().success(true).message(message).data(data).build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder().success(false).message(message).build();
    }
}

// ── Needed for JPQL constructor ───────────────────────────────────────────────

@Data @AllArgsConstructor @NoArgsConstructor
public class DailySummaryDto {
    private long totalOrders;
    private double netRevenue;
    private long voidedOrders;
    private LocalDateTime firstOrder;
    private LocalDateTime lastOrder;
}

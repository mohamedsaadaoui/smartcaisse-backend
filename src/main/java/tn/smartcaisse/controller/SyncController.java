package tn.smartcaisse.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.smartcaisse.dto.*;
import tn.smartcaisse.entity.*;
import tn.smartcaisse.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Receives batched orders from the Electron app when it comes back online.
 * The Electron app stores orders in SQLite offline, then POSTs them here.
 * This endpoint is intentionally open (auth via device key, not JWT).
 */
@RestController
@RequestMapping("/api/sync")
@RequiredArgsConstructor
@Slf4j
public class SyncController {

    private final OrderRepository orderRepo;
    private final OrderLineRepository orderLineRepo;

    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<SyncResponse>> syncOrders(@RequestBody SyncPayload payload) {
        log.info("Sync received from device: {} — {} orders", payload.getDeviceId(), payload.getOrders().size());

        int synced = 0;
        List<String> errors = new ArrayList<>();

        for (OrderRequest req : payload.getOrders()) {
            try {
                var order = Order.builder()
                    .orderNumber(orderRepo.getMaxOrderNumberToday() + 1)
                    .type(parseType(req.getType()))
                    .paymentMethod(parsePayment(req.getPaymentMethod()))
                    .total(req.getTotal())
                    .cashierName(req.getCashierName())
                    .note(req.getNote())
                    .status(Order.OrderStatus.PAID)
                    .closedAt(LocalDateTime.now())
                    .build();

                var saved = orderRepo.save(order);

                var lines = req.getLines().stream()
                    .map(l -> OrderLine.builder()
                        .order(saved)
                        .itemId(l.getItemId())
                        .itemName(l.getItemName())
                        .unitPrice(l.getUnitPrice())
                        .quantity(l.getQuantity())
                        .lineTotal(l.getLineTotal())
                        .build())
                    .toList();

                orderLineRepo.saveAll(lines);
                synced++;
            } catch (Exception e) {
                errors.add("Order failed: " + e.getMessage());
                log.error("Sync error for order: {}", e.getMessage());
            }
        }

        return ResponseEntity.ok(ApiResponse.ok(SyncResponse.builder()
            .synced(synced)
            .failed(errors.size())
            .errors(errors)
            .build()));
    }

    @GetMapping("/ping")
    public ResponseEntity<ApiResponse<String>> ping() {
        return ResponseEntity.ok(ApiResponse.ok("pong"));
    }

    private Order.OrderType parseType(String t) {
        if (t == null) return Order.OrderType.COUNTER;
        return switch (t.toLowerCase()) {
            case "takeaway" -> Order.OrderType.TAKEAWAY;
            case "delivery" -> Order.OrderType.DELIVERY;
            default         -> Order.OrderType.COUNTER;
        };
    }

    private Order.PaymentMethod parsePayment(String p) {
        if ("card".equalsIgnoreCase(p)) return Order.PaymentMethod.CARD;
        return Order.PaymentMethod.CASH;
    }
}

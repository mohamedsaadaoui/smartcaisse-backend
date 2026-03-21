package tn.smartcaisse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.smartcaisse.dto.*;
import tn.smartcaisse.entity.*;
import tn.smartcaisse.repository.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepo;
    private final OrderLineRepository orderLineRepo;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDto>> create(@RequestBody OrderRequest req) {
        int nextNum = orderRepo.getMaxOrderNumberToday() + 1;

        var order = Order.builder()
            .orderNumber(nextNum)
            .type(parseType(req.getType()))
            .paymentMethod(parsePayment(req.getPaymentMethod()))
            .total(req.getTotal())
            .cashierName(req.getCashierName())
            .note(req.getNote())
            .status(Order.OrderStatus.PAID)
            .closedAt(java.time.LocalDateTime.now())
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

        return ResponseEntity.ok(ApiResponse.ok("Commande enregistrée", toDto(saved, lines)));
    }

    @GetMapping("/today")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getToday() {
        var orders = orderRepo.findByDate(LocalDate.now())
            .stream().map(o -> toDto(o, List.of())).toList();
        return ResponseEntity.ok(ApiResponse.ok(orders));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        var orders = orderRepo.findByDate(date)
            .stream().map(o -> toDto(o, List.of())).toList();
        return ResponseEntity.ok(ApiResponse.ok(orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDto>> getById(@PathVariable Long id) {
        return orderRepo.findById(id)
            .map(o -> {
                var lines = o.getLines() != null ? o.getLines() : List.<OrderLine>of();
                return ResponseEntity.ok(ApiResponse.ok(toDto(o, lines)));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/void")
    public ResponseEntity<ApiResponse<Void>> voidOrder(@PathVariable Long id) {
        orderRepo.findById(id).ifPresent(o -> {
            o.setStatus(Order.OrderStatus.VOIDED);
            orderRepo.save(o);
        });
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

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

    private OrderDto toDto(Order o, List<OrderLine> lines) {
        return OrderDto.builder()
            .id(o.getId())
            .orderNumber(o.getOrderNumber())
            .type(o.getType().name().toLowerCase())
            .status(o.getStatus().name().toLowerCase())
            .total(o.getTotal())
            .paymentMethod(o.getPaymentMethod().name().toLowerCase())
            .cashierName(o.getCashierName())
            .note(o.getNote())
            .createdAt(o.getCreatedAt())
            .lines(lines.stream().map(l -> OrderLineDto.builder()
                .id(l.getId()).itemName(l.getItemName())
                .unitPrice(l.getUnitPrice()).quantity(l.getQuantity())
                .lineTotal(l.getLineTotal()).build()).toList())
            .build();
    }
}

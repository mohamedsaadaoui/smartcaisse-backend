package tn.smartcaisse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.smartcaisse.dto.*;
import tn.smartcaisse.entity.StockItem;
import tn.smartcaisse.repository.StockItemRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockItemRepository stockRepo;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StockDto>>> getAll() {
        var list = stockRepo.findAll().stream().map(this::toDto).toList();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/alerts")
    public ResponseEntity<ApiResponse<List<StockDto>>> getLowStock() {
        var list = stockRepo.findLowStockItems().stream().map(this::toDto).toList();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StockDto>> createOrUpdate(@RequestBody StockRequest req) {
        var existing = stockRepo.findByName(req.getName());
        StockItem item;
        if (existing.isPresent()) {
            item = existing.get();
            item.setQuantity(req.getQuantity());
            item.setUpdatedAt(LocalDateTime.now());
        } else {
            item = StockItem.builder()
                .name(req.getName())
                .unit(req.getUnit() != null ? req.getUnit() : "unité")
                .quantity(req.getQuantity())
                .lowThreshold(req.getLowThreshold() != null ? req.getLowThreshold() : 5.0)
                .build();
        }
        return ResponseEntity.ok(ApiResponse.ok(toDto(stockRepo.save(item))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StockDto>> update(
            @PathVariable Long id, @RequestBody StockRequest req) {
        return stockRepo.findById(id).map(item -> {
            item.setQuantity(req.getQuantity());
            if (req.getLowThreshold() != null) item.setLowThreshold(req.getLowThreshold());
            item.setUpdatedAt(LocalDateTime.now());
            return ResponseEntity.ok(ApiResponse.ok(toDto(stockRepo.save(item))));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        stockRepo.deleteById(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    private StockDto toDto(StockItem s) {
        return StockDto.builder()
            .id(s.getId()).name(s.getName()).unit(s.getUnit())
            .quantity(s.getQuantity()).lowThreshold(s.getLowThreshold())
            .lowStock(s.getQuantity() <= s.getLowThreshold())
            .build();
    }
}

package tn.smartcaisse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.smartcaisse.dto.*;
import tn.smartcaisse.entity.*;
import tn.smartcaisse.repository.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuController {

    private final MenuItemRepository menuRepo;
    private final CategoryRepository categoryRepo;

    // ── Categories ────────────────────────────────────────────────────────────

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getCategories() {
        var list = categoryRepo.findAllByOrderBySortOrderAsc().stream()
            .map(c -> CategoryDto.builder()
                .id(c.getId()).name(c.getName())
                .nameAr(c.getNameAr()).sortOrder(c.getSortOrder())
                .build())
            .toList();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<CategoryDto>> createCategory(@RequestBody CategoryRequest req) {
        var saved = categoryRepo.save(Category.builder()
            .name(req.getName()).nameAr(req.getNameAr())
            .sortOrder(req.getSortOrder() != null ? req.getSortOrder() : 0)
            .build());
        return ResponseEntity.ok(ApiResponse.ok(toDto(saved)));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryRepo.deleteById(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    // ── Menu Items ────────────────────────────────────────────────────────────

    @GetMapping("/menu-items")
    public ResponseEntity<ApiResponse<List<MenuItemDto>>> getAll(
            @RequestParam(required = false) Long categoryId) {
        var items = categoryId != null
            ? menuRepo.findByCategoryIdAndActiveTrue(categoryId)
            : menuRepo.findByActiveTrueOrderByCategoryNameAscNameAsc();
        return ResponseEntity.ok(ApiResponse.ok(items.stream().map(this::toDto).toList()));
    }

    @PostMapping("/menu-items")
    public ResponseEntity<ApiResponse<MenuItemDto>> create(@RequestBody MenuItemRequest req) {
        var category = categoryRepo.findById(req.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found"));
        var saved = menuRepo.save(MenuItem.builder()
            .name(req.getName()).nameAr(req.getNameAr())
            .price(req.getPrice()).category(category).active(true)
            .build());
        return ResponseEntity.ok(ApiResponse.ok(toDto(saved)));
    }

    @PutMapping("/menu-items/{id}")
    public ResponseEntity<ApiResponse<MenuItemDto>> update(
            @PathVariable Long id, @RequestBody MenuItemRequest req) {
        var item = menuRepo.findById(id).orElseThrow();
        var category = categoryRepo.findById(req.getCategoryId()).orElseThrow();
        item.setName(req.getName());
        item.setNameAr(req.getNameAr());
        item.setPrice(req.getPrice());
        item.setCategory(category);
        item.setActive(req.isActive());
        return ResponseEntity.ok(ApiResponse.ok(toDto(menuRepo.save(item))));
    }

    @DeleteMapping("/menu-items/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        menuRepo.findById(id).ifPresent(item -> {
            item.setActive(false);
            menuRepo.save(item);
        });
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    // ── Mappers ───────────────────────────────────────────────────────────────

    private CategoryDto toDto(Category c) {
        return CategoryDto.builder()
            .id(c.getId()).name(c.getName())
            .nameAr(c.getNameAr()).sortOrder(c.getSortOrder())
            .build();
    }

    private MenuItemDto toDto(MenuItem m) {
        return MenuItemDto.builder()
            .id(m.getId()).name(m.getName()).nameAr(m.getNameAr())
            .price(m.getPrice()).active(m.isActive())
            .categoryId(m.getCategory() != null ? m.getCategory().getId() : null)
            .categoryName(m.getCategory() != null ? m.getCategory().getName() : null)
            .build();
    }
}

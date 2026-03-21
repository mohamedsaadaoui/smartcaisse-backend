package tn.smartcaisse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.smartcaisse.entity.StockItem;
import java.util.List;
import java.util.Optional;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {
    Optional<StockItem> findByName(String name);

    @Query("SELECT s FROM StockItem s WHERE s.quantity <= s.lowThreshold ORDER BY s.quantity ASC")
    List<StockItem> findLowStockItems();
}

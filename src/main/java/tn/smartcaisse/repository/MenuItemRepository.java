package tn.smartcaisse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.smartcaisse.entity.MenuItem;
import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByActiveTrueOrderByCategoryNameAscNameAsc();
    List<MenuItem> findByCategoryIdAndActiveTrue(Long categoryId);
}

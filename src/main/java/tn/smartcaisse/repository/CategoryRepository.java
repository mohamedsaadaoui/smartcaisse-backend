package tn.smartcaisse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.smartcaisse.entity.Category;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByOrderBySortOrderAsc();
}

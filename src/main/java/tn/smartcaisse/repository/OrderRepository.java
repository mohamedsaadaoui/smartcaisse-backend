package tn.smartcaisse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.smartcaisse.entity.Order;
import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE CAST(o.createdAt AS date) = :date AND o.status <> 'VOIDED' ORDER BY o.createdAt DESC")
    List<Order> findByDate(@Param("date") LocalDate date);

    @Query("SELECT COALESCE(MAX(o.orderNumber), 0) FROM Order o WHERE CAST(o.createdAt AS date) = CURRENT_DATE")
    int getMaxOrderNumberToday();

    @Query("SELECT COUNT(o) FROM Order o WHERE CAST(o.createdAt AS date) = :date AND o.status <> 'VOIDED'")
    long countByDate(@Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Order o WHERE CAST(o.createdAt AS date) = :date AND o.status = 'PAID'")
    double sumRevenueByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(o) FROM Order o WHERE CAST(o.createdAt AS date) = :date AND o.status = 'VOIDED'")
    long countVoidedByDate(@Param("date") LocalDate date);
}

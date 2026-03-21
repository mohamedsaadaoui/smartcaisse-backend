package tn.smartcaisse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.smartcaisse.entity.OrderLine;
import java.time.LocalDate;
import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

    @Query("""
        SELECT ol.itemName, SUM(ol.quantity), SUM(ol.lineTotal)
        FROM OrderLine ol JOIN ol.order o
        WHERE CAST(o.createdAt AS date) = :date AND o.status = 'PAID'
        GROUP BY ol.itemName
        ORDER BY SUM(ol.quantity) DESC
        LIMIT 10
    """)
    List<Object[]> findTopItemsByDate(@Param("date") LocalDate date);

    @Query("""
        SELECT FUNCTION('to_char', o.createdAt, 'HH24') as hour,
               COUNT(o.id), SUM(o.total)
        FROM Order o
        WHERE CAST(o.createdAt AS date) = :date AND o.status = 'PAID'
        GROUP BY hour ORDER BY hour
    """)
    List<Object[]> findRevenueByHour(@Param("date") LocalDate date);
}

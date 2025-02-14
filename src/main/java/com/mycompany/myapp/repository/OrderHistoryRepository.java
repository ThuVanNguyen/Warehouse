package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.domain.OrderHistory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    @Query(
        """
            SELECT o
            FROM OrderHistory o
            WHERE (:keyword IS NULL OR :keyword = '' OR
                   LOWER(o.warehouseReceiptCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                   LOWER(o.waybill) LIKE LOWER(CONCAT('%', :keyword, '%')))
        """
    )
    Page<OrderHistory> findAll(String keyword, Pageable pageable);
}

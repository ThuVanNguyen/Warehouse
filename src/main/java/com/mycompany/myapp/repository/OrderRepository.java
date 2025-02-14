package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.enums.Status;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

/** Spring Data JPA repository for the Order entity. */
@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(
        """
          SELECT o
          FROM Order o
          WHERE
              o.warehouseReceiptCode = :warehouseReceiptCode
              AND o.productCn = :nameCn
        """
    )
    Optional<Order> findByWarehouseReceiptCodeAndProductNameCnAndWaybill(
        @Param("warehouseReceiptCode") String warehouseReceiptCode,
        @Param("nameCn") String nameCn
    );

    //    @Query(
    //        """
    //            SELECT o
    //            FROM Order o
    //            WHERE (:keyword IS NULL OR :keyword = '' OR
    //                   LOWER(o.warehouseReceiptCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
    //                   LOWER(o.waybill) LIKE LOWER(CONCAT('%', :keyword, '%')))
    //        """
    //    )
    //    Page<Order> findAll(String keyword, Pageable pageable);

    @Query(
        "SELECT o FROM Order o WHERE " +
        "((o.status = 'CANCELED' AND o.cancelDate <= :oneMonthAgo) OR " +
        "(o.status = 'CUSTOMER_SIGNED' AND o.customerSignedDate <= :oneMonthAgo))"
    )
    List<Order> findRecordsWithExactOneMonthAgo(@Param("oneMonthAgo") LocalDate oneMonthAgo);

    @Query(
        """
            SELECT o
            FROM Order o
            LEFT JOIN o.client c
            WHERE (:warehouseReceiptCode IS NULL OR o.warehouseReceiptCode = :warehouseReceiptCode)
              AND (:status IS NULL OR o.status = :status)
              AND (
                  (:date = '9999-12-30') OR
                  (:status IS NULL AND (
                      o.receiptDate = CAST(:date AS date) OR
                      o.loadedOnVehicleDate = CAST(:date AS date) OR
                      o.arrivedAtHNDate = CAST(:date AS date) OR
                      o.deliveredDate = CAST(:date AS date) OR
                      o.customerSignedDate = CAST(:date AS date) OR
                      o.cancelDate = CAST(:date AS date)
                  )) OR
                  (:status IS NOT NULL AND (
                      (o.status = 'BANG_TUONG_WARE_HOUSE' AND o.receiptDate = CAST(:date AS date)) OR
                      (o.status = 'LOADED_ON_VEHICLE' AND o.loadedOnVehicleDate = CAST(:date AS date)) OR
                      (o.status = 'ARRIVED_AT_HN' AND o.arrivedAtHNDate = CAST(:date AS date)) OR
                      (o.status = 'DELIVERED' AND o.deliveredDate = CAST(:date AS date)) OR
                      (o.status = 'CUSTOMER_SIGNED' AND o.customerSignedDate = CAST(:date AS date)) OR
                      (o.status = 'CANCELED' AND o.cancelDate = CAST(:date AS date))
                  ))
              )
               AND (:clientCode IS NULL OR c.code = :clientCode)
               AND (:hasAdminOrEditorRole = true OR c.code = :currentUserCode)
               AND (:productName IS NULL OR (o.productCn LIKE %:productName% OR o.productVn LIKE %:productName%))
        """
    )
    Page<Order> findAll(
        @Param("warehouseReceiptCode") String warehouseReceiptCode,
        @Param("status") Status status,
        @Param("date") String date,
        @Param("clientCode") String clientCode,
        @Param("hasAdminOrEditorRole") boolean hasAdminOrEditorRole,
        @Param("currentUserCode") String currentUserCode,
        @Param("productName") String productName,
        Pageable pageable
    );

    @Query("SELECT o FROM Order o WHERE o.id IN :ids")
    List<Order> findAllById(@Param("ids") List<Long> ids);

    @Query("SELECT o FROM Order o WHERE o.id IN :orderIds")
    Page<Order> findAllByIdIn(@Param("orderIds") List<Long> orderIds, Pageable pageable);
}

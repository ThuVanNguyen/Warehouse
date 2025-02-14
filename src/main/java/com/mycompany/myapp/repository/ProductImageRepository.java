package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    @Query(
        """
            SELECT o
            FROM ProductImage o
            WHERE (:keyword IS NULL OR :keyword = '' OR
                   LOWER(o.base64) LIKE LOWER(CONCAT('%', :keyword, '%')))
        """
    )
    Page<ProductImage> findAll(String keyword, Pageable pageable);
}

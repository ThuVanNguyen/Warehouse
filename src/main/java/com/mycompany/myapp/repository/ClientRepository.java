package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Client;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Client entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByCode(String code);

    @Query(
        """
            SELECT o
            FROM Client o
            WHERE (:keyword IS NULL OR :keyword = '' OR
                LOWER(o.code) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR
                LOWER(o.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(o.phone) LIKE LOWER(CONCAT('%', :keyword, '%'))
        """
    )
    Page<Client> findAll(String keyword, Pageable pageable);
}

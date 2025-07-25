package com.inviggo.demo.repository;

import com.inviggo.demo.model.Ad;
import com.inviggo.demo.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdRepository extends JpaRepository<Ad,Long> {
    @Query("SELECT a FROM Ad a WHERE " +
            "(:category IS NULL OR a.category = :category) AND " +
            "(:title IS NULL OR LOWER(CAST(a.title AS string)) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:minPrice IS NULL OR a.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR a.price <= :maxPrice) AND " +
            "(:userId IS NULL OR a.user.id = :userId) " +
            "ORDER BY a.createdAt DESC")
    Page<Ad> findFilteredAds(
            @Param("category") Category category,
            @Param("title") String title,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("userId") Long userId,
            Pageable pageable
    );
    Page<Ad> findByUser_Username(String username, Pageable pageable);
    @Query("SELECT a FROM Ad a WHERE (:minPrice IS NULL OR a.price >= :minPrice) AND (:maxPrice IS NULL OR a.price <= :maxPrice)")
    Page<Ad> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, Pageable pageable);
}

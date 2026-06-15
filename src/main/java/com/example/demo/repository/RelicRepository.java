package com.example.demo.repository;

import com.example.demo.entity.Relic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface RelicRepository extends JpaRepository<Relic, Integer> {
    List<Relic> findByCategoryId(Integer categoryId);
    List<Relic> findByCategoryIdOrderByPublishDateDesc(Integer categoryId);
    Page<Relic> findByCategoryId(Integer categoryId, Pageable pageable);

    /**
     * Fuzzy search relics by keyword matching title or publish_date.
     * Uses MySQL DATE_FORMAT to allow date string matching.
     */
    @Query(value = "SELECT * FROM relics WHERE " +
            "title LIKE CONCAT('%', :keyword, '%') OR " +
            "DATE_FORMAT(publish_date, '%Y-%m-%d') LIKE CONCAT('%', :keyword, '%')",
            countQuery = "SELECT count(*) FROM relics WHERE " +
            "title LIKE CONCAT('%', :keyword, '%') OR " +
            "DATE_FORMAT(publish_date, '%Y-%m-%d') LIKE CONCAT('%', :keyword, '%')",
            nativeQuery = true)
    Page<Relic> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}

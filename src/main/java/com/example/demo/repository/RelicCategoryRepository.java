package com.example.demo.repository;

import com.example.demo.entity.RelicCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelicCategoryRepository extends JpaRepository<RelicCategory, Integer> {
    List<RelicCategory> findAllByOrderBySortOrderAsc();
}

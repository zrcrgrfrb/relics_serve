package com.example.demo.repository;

import com.example.demo.entity.Relic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelicRepository extends JpaRepository<Relic, Integer> {
    List<Relic> findByCategoryId(Integer categoryId);
    List<Relic> findByCategoryIdOrderByPublishDateDesc(Integer categoryId);
    Page<Relic> findByCategoryId(Integer categoryId, Pageable pageable);
}

package com.example.demo.repository;

import com.example.demo.entity.RelicImageFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RelicImageFeatureRepository extends JpaRepository<RelicImageFeature, Integer> {
    Optional<RelicImageFeature> findByRelicId(Integer relicId);
}

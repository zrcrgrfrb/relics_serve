package com.example.demo.service;

import com.example.demo.entity.Relic;
import com.example.demo.entity.RelicCategory;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RelicService {
    List<RelicCategory> getAllCategories();
    RelicCategory getCategoryById(Integer id);
    List<Relic> getAllRelics();
    Relic getRelicById(Integer id);
    List<Relic> getRelicsByCategory(Integer categoryId);
    Page<Relic> getRelicsPage(Integer categoryId, int page, int pageSize);
    Page<Relic> searchRelics(String keyword, int page, int pageSize);
    Relic createRelic(Relic relic);
    Relic updateRelic(Integer id, Relic relic);
    void deleteRelic(Integer id);
    void deleteRelics(List<Integer> ids);
}

package com.example.demo.service.impl;

import com.example.demo.entity.Relic;
import com.example.demo.entity.RelicCategory;
import com.example.demo.repository.RelicCategoryRepository;
import com.example.demo.repository.RelicRepository;
import com.example.demo.service.RelicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RelicServiceImpl implements RelicService {
    
    @Autowired
    private RelicRepository relicRepository;
    
    @Autowired
    private RelicCategoryRepository categoryRepository;
    
    @Override
    public List<RelicCategory> getAllCategories() {
        return categoryRepository.findAllByOrderBySortOrderAsc();
    }
    
    @Override
    public RelicCategory getCategoryById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }
    
    @Override
    public List<Relic> getAllRelics() {
        return relicRepository.findAll();
    }
    
    @Override
    public Relic getRelicById(Integer id) {
        return relicRepository.findById(id).orElse(null);
    }
    
    @Override
    public List<Relic> getRelicsByCategory(Integer categoryId) {
        return relicRepository.findByCategoryIdOrderByPublishDateDesc(categoryId);
    }

    @Override
    public Page<Relic> getRelicsPage(Integer categoryId, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(
                page - 1,
                pageSize,
                Sort.by(Sort.Direction.DESC, "publishDate").and(Sort.by(Sort.Direction.DESC, "id"))
        );
        return relicRepository.findByCategoryId(categoryId, pageRequest);
    }
    
    @Override
    public Relic createRelic(Relic relic) {
        return relicRepository.save(relic);
    }
    
    @Override
    public Relic updateRelic(Integer id, Relic relic) {
        Optional<Relic> existingRelic = relicRepository.findById(id);
        if (existingRelic.isPresent()) {
            Relic updatedRelic = existingRelic.get();
            updatedRelic.setTitle(relic.getTitle());
            updatedRelic.setCategoryId(relic.getCategoryId());
            updatedRelic.setContent(relic.getContent());
            updatedRelic.setImageUrl(relic.getImageUrl());
            updatedRelic.setPeriod(relic.getPeriod());
            updatedRelic.setLocation(relic.getLocation());
            updatedRelic.setPublishDate(relic.getPublishDate());
            return relicRepository.save(updatedRelic);
        }
        return null;
    }
    
    @Override
    public void deleteRelic(Integer id) {
        relicRepository.deleteById(id);
    }
}

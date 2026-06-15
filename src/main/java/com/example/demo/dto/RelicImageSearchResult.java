package com.example.demo.dto;

import com.example.demo.entity.Relic;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RelicImageSearchResult {
    private Integer id;
    private String title;
    private String imageUrl;
    private LocalDate publishDate;
    private double similarity;

    public static RelicImageSearchResult from(Relic relic, double similarity) {
        return new RelicImageSearchResult(
                relic.getId(),
                relic.getTitle(),
                relic.getImageUrl(),
                relic.getPublishDate(),
                similarity
        );
    }
}

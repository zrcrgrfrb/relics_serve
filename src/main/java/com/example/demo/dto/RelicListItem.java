package com.example.demo.dto;

import com.example.demo.entity.Relic;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RelicListItem {
    private Integer id;
    private String title;
    private Integer categoryId;
    private String content;
    private String imageUrl;
    private String period;
    private String location;
    private LocalDate publishDate;

    public static RelicListItem from(Relic relic) {
        return new RelicListItem(
                relic.getId(),
                relic.getTitle(),
                relic.getCategoryId(),
                relic.getContent(),
                relic.getImageUrl(),
                relic.getPeriod(),
                relic.getLocation(),
                relic.getPublishDate()
        );
    }
}

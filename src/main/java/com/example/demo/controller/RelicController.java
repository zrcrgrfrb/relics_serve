package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.PageResult;
import com.example.demo.dto.RelicImageSearchResult;
import com.example.demo.dto.RelicListItem;
import com.example.demo.entity.Relic;
import com.example.demo.entity.RelicCategory;
import com.example.demo.service.RelicImageSearchService;
import com.example.demo.service.RelicService;
import com.example.demo.util.PublicUrlBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RelicController {

    @Autowired
    private RelicService relicService;

    @Autowired
    private RelicImageSearchService relicImageSearchService;

    @Autowired
    private PublicUrlBuilder publicUrlBuilder;

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<RelicCategory>>> getAllCategories() {
        return ResponseEntity.ok(ApiResponse.ok(relicService.getAllCategories()));
    }

    @GetMapping("/relics")
    public ResponseEntity<ApiResponse<List<Relic>>> getRelicsByCategory(@RequestParam(required = false) Integer type,
                                                                        HttpServletRequest request) {
        List<Relic> relics = type != null
                ? relicService.getRelicsByCategory(type)
                : relicService.getAllRelics();
        relics.forEach(relic -> fillImageUrl(relic, request));
        return ResponseEntity.ok(ApiResponse.ok(relics));
    }

    @GetMapping("/relics/page")
    public ResponseEntity<ApiResponse<PageResult<RelicListItem>>> getRelicsPage(
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            HttpServletRequest request) {
        if (type == null || page == null || pageSize == null || page < 1 || pageSize < 1) {
            return ResponseEntity.badRequest().body(ApiResponse.error("invalid parameters"));
        }
        if (relicService.getCategoryById(type) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("category not found"));
        }

        Page<Relic> relicPage = relicService.getRelicsPage(type, page, pageSize);
        List<RelicListItem> list = relicPage.getContent().stream()
                .map(RelicListItem::from)
                .peek(item -> item.setImageUrl(publicUrlBuilder.resolveAssetUrl(item.getImageUrl(), request)))
                .toList();
        PageResult<RelicListItem> result = new PageResult<>(
                list,
                relicPage.getTotalElements(),
                page,
                pageSize
        );
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/relics/search")
    public ResponseEntity<ApiResponse<PageResult<RelicListItem>>> searchRelics(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            HttpServletRequest request) {
        if (keyword == null || keyword.isBlank()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("keyword required"));
        }
        if (page == null || pageSize == null || page < 1 || pageSize < 1) {
            return ResponseEntity.badRequest().body(ApiResponse.error("invalid parameters"));
        }

        Page<Relic> relicPage = relicService.searchRelics(keyword, page, pageSize);
        List<RelicListItem> list = relicPage.getContent().stream()
                .map(RelicListItem::from)
                .peek(item -> item.setImageUrl(publicUrlBuilder.resolveAssetUrl(item.getImageUrl(), request)))
                .toList();
        PageResult<RelicListItem> result = new PageResult<>(
                list,
                relicPage.getTotalElements(),
                page,
                pageSize
        );
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PostMapping("/relics/image-search")
    public ResponseEntity<ApiResponse<PageResult<RelicImageSearchResult>>> searchRelicsByImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            HttpServletRequest request) {
        if (limit == null || limit < 1) {
            return ResponseEntity.badRequest().body(ApiResponse.error("invalid parameters"));
        }

        try {
            int resultLimit = Math.min(limit, 20);
            List<RelicImageSearchResult> list = relicImageSearchService.search(file, resultLimit);
            list.forEach(item -> item.setImageUrl(publicUrlBuilder.resolveAssetUrl(item.getImageUrl(), request)));
            PageResult<RelicImageSearchResult> result = new PageResult<>(
                    list,
                    list.size(),
                    1,
                    resultLimit
            );
            return ResponseEntity.ok(ApiResponse.ok(result));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("invalid image"));
        }
    }

    @GetMapping("/relics/{id}")
    public ResponseEntity<ApiResponse<Relic>> getRelicById(@PathVariable Integer id,
                                                           HttpServletRequest request) {
        Relic relic = relicService.getRelicById(id);
        if (relic == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("relic not found"));
        }
        fillImageUrl(relic, request);
        return ResponseEntity.ok(ApiResponse.ok(relic));
    }

    private void fillImageUrl(Relic relic, HttpServletRequest request) {
        relic.setImageUrl(publicUrlBuilder.resolveAssetUrl(relic.getImageUrl(), request));
    }

    @PostMapping("/relics")
    public ResponseEntity<ApiResponse<Relic>> createRelic(@RequestBody Relic relic) {
        // title 为必填字段
        if (relic.getTitle() == null || relic.getTitle().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("参数校验失败：title 为必填字段"));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(relicService.createRelic(relic)));
    }

    @PutMapping("/relics/{id}")
    public ResponseEntity<ApiResponse<Relic>> updateRelic(@PathVariable Integer id, @RequestBody Relic relic) {
        if (relic.getTitle() == null || relic.getTitle().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("参数校验失败：title 为必填字段"));
        }
        Relic updatedRelic = relicService.updateRelic(id, relic);
        if (updatedRelic == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("relic not found"));
        }
        return ResponseEntity.ok(ApiResponse.ok(updatedRelic));
    }

    @DeleteMapping("/relics/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRelic(@PathVariable Integer id) {
        // 先检查资源是否存在
        if (relicService.getRelicById(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("relic not found"));
        }
        relicService.deleteRelic(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(Exception ignored) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("invalid parameters"));
    }
}

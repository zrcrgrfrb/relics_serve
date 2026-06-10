package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.PageResult;
import com.example.demo.dto.RelicListItem;
import com.example.demo.entity.Relic;
import com.example.demo.entity.RelicCategory;
import com.example.demo.service.RelicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RelicController {

    @Autowired
    private RelicService relicService;

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<RelicCategory>>> getAllCategories() {
        return ResponseEntity.ok(ApiResponse.ok(relicService.getAllCategories()));
    }

    @GetMapping("/relics")
    public ResponseEntity<ApiResponse<List<Relic>>> getRelicsByCategory(@RequestParam(required = false) Integer type) {
        List<Relic> relics = type != null
                ? relicService.getRelicsByCategory(type)
                : relicService.getAllRelics();
        return ResponseEntity.ok(ApiResponse.ok(relics));
    }

    @GetMapping("/relics/page")
    public ResponseEntity<ApiResponse<PageResult<RelicListItem>>> getRelicsPage(
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) {
        if (type == null || page == null || pageSize == null || page < 1 || pageSize < 1) {
            return ResponseEntity.badRequest().body(ApiResponse.error("invalid parameters"));
        }
        if (relicService.getCategoryById(type) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("category not found"));
        }

        Page<Relic> relicPage = relicService.getRelicsPage(type, page, pageSize);
        List<RelicListItem> list = relicPage.getContent().stream()
                .map(RelicListItem::from)
                .toList();
        PageResult<RelicListItem> result = new PageResult<>(
                list,
                relicPage.getTotalElements(),
                page,
                pageSize
        );
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/relics/{id}")
    public ResponseEntity<ApiResponse<Relic>> getRelicById(@PathVariable Integer id) {
        Relic relic = relicService.getRelicById(id);
        if (relic == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("relic not found"));
        }
        return ResponseEntity.ok(ApiResponse.ok(relic));
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

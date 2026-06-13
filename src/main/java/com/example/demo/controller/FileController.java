package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class FileController {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    private static final Map<String, Set<String>> ALLOWED_CONTENT_TYPES = Map.of(
            "jpg", Set.of("image/jpeg"),
            "jpeg", Set.of("image/jpeg"),
            "png", Set.of("image/png"),
            "gif", Set.of("image/gif"),
            "webp", Set.of("image/webp"),
            "bmp", Set.of("image/bmp", "image/x-ms-bmp")
    );

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> uploadFile(@RequestParam("file") MultipartFile file,
                                                          HttpServletRequest request) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("上传文件不能为空"));
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("文件大小不能超过 10MB"));
        }

        String extension = extensionOf(file.getOriginalFilename());
        if (!ALLOWED_CONTENT_TYPES.containsKey(extension)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("不支持的文件格式"));
        }
        String contentType = file.getContentType() == null ? "" : file.getContentType().toLowerCase(Locale.ROOT);
        if (!ALLOWED_CONTENT_TYPES.get(extension).contains(contentType)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("文件类型不匹配"));
        }

        try {
            if (!hasValidImageSignature(file, extension)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("文件内容不是有效图片"));
            }

            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            String newFilename = UUID.randomUUID() + "." + extension;
            Path targetPath = uploadPath.resolve(newFilename).normalize();
            if (!targetPath.startsWith(uploadPath)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("非法文件路径"));
            }

            file.transferTo(targetPath.toFile());
            String fileUrl = request.getContextPath() + "/uploads/" + newFilename;
            return ResponseEntity.ok(ApiResponse.ok(fileUrl));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("文件上传失败"));
        }
    }

    private String extensionOf(String originalFilename) {
        if (originalFilename == null) {
            return "";
        }
        String filename = Paths.get(originalFilename).getFileName().toString();
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
    }

    private boolean hasValidImageSignature(MultipartFile file, String extension) throws IOException {
        byte[] header = new byte[12];
        int length;
        try (InputStream inputStream = file.getInputStream()) {
            length = inputStream.read(header);
        }
        if (length < 4) {
            return false;
        }

        return switch (extension) {
            case "jpg", "jpeg" -> (header[0] & 0xFF) == 0xFF && (header[1] & 0xFF) == 0xD8 && (header[2] & 0xFF) == 0xFF;
            case "png" -> length >= 8
                    && (header[0] & 0xFF) == 0x89 && header[1] == 0x50 && header[2] == 0x4E && header[3] == 0x47
                    && header[4] == 0x0D && header[5] == 0x0A && header[6] == 0x1A && header[7] == 0x0A;
            case "gif" -> header[0] == 0x47 && header[1] == 0x49 && header[2] == 0x46 && header[3] == 0x38;
            case "webp" -> length >= 12
                    && header[0] == 0x52 && header[1] == 0x49 && header[2] == 0x46 && header[3] == 0x46
                    && header[8] == 0x57 && header[9] == 0x45 && header[10] == 0x42 && header[11] == 0x50;
            case "bmp" -> header[0] == 0x42 && header[1] == 0x4D;
            default -> false;
        };
    }
}

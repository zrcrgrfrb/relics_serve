package com.example.demo.service;

import com.example.demo.dto.RelicImageSearchResult;
import com.example.demo.entity.Relic;
import com.example.demo.entity.RelicImageFeature;
import com.example.demo.repository.RelicImageFeatureRepository;
import com.example.demo.repository.RelicRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class RelicImageSearchService {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    private final RelicRepository relicRepository;
    private final RelicImageFeatureRepository featureRepository;
    private final ImageFeatureExtractor featureExtractor;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public RelicImageSearchService(RelicRepository relicRepository,
                                   RelicImageFeatureRepository featureRepository,
                                   ImageFeatureExtractor featureExtractor) {
        this.relicRepository = relicRepository;
        this.featureRepository = featureRepository;
        this.featureExtractor = featureExtractor;
    }

    public List<RelicImageSearchResult> search(MultipartFile file, int limit) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("empty image");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IOException("image too large");
        }

        ImageFeatureExtractor.ImageFeature queryFeature;
        try (InputStream inputStream = file.getInputStream()) {
            queryFeature = featureExtractor.extract(inputStream);
        }

        return relicRepository.findAll().stream()
                .filter(relic -> relic.getImageUrl() != null && !relic.getImageUrl().isBlank())
                .map(relic -> score(relic, queryFeature))
                .flatMap(Optional::stream)
                .sorted(Comparator.comparingDouble(RelicImageSearchResult::getSimilarity).reversed())
                .limit(Math.max(1, limit))
                .toList();
    }

    private Optional<RelicImageSearchResult> score(Relic relic, ImageFeatureExtractor.ImageFeature queryFeature) {
        try {
            RelicImageFeature storedFeature = getOrCreateFeature(relic);
            double[] vector = featureExtractor.deserialize(storedFeature.getFeatureVector());
            if (vector.length == 0 || storedFeature.getImageHash() == null) {
                return Optional.empty();
            }

            double vectorSimilarity = featureExtractor.cosineSimilarity(queryFeature.vector(), vector);
            double hashSimilarity = featureExtractor.hashSimilarity(queryFeature.hash(), storedFeature.getImageHash());
            double similarity = vectorSimilarity * 0.72 + hashSimilarity * 0.28;
            return Optional.of(RelicImageSearchResult.from(relic, Math.max(0, Math.min(1, similarity))));
        } catch (IOException ignored) {
            return Optional.empty();
        }
    }

    private RelicImageFeature getOrCreateFeature(Relic relic) throws IOException {
        Optional<RelicImageFeature> existing = featureRepository.findByRelicId(relic.getId());
        if (existing.isPresent() && relic.getImageUrl().equals(existing.get().getImageUrl())) {
            return existing.get();
        }

        ImageFeatureExtractor.ImageFeature imageFeature;
        try (InputStream inputStream = openRelicImage(relic.getImageUrl())) {
            imageFeature = featureExtractor.extract(inputStream);
        }

        RelicImageFeature feature = existing.orElseGet(RelicImageFeature::new);
        feature.setRelicId(relic.getId());
        feature.setImageUrl(relic.getImageUrl());
        feature.setFeatureVector(featureExtractor.serialize(imageFeature.vector()));
        feature.setImageHash(imageFeature.hash());
        return featureRepository.save(feature);
    }

    private InputStream openRelicImage(String imageUrl) throws IOException {
        if (imageUrl.startsWith("/uploads/")) {
            String filename = imageUrl.substring("/uploads/".length());
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path targetPath = uploadPath.resolve(filename).normalize();
            if (!targetPath.startsWith(uploadPath)) {
                throw new IOException("invalid image path");
            }
            return Files.newInputStream(targetPath);
        }

        if (imageUrl.startsWith("uploads/")) {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path targetPath = uploadPath.resolve(imageUrl.substring("uploads/".length())).normalize();
            if (!targetPath.startsWith(uploadPath)) {
                throw new IOException("invalid image path");
            }
            return Files.newInputStream(targetPath);
        }

        if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
            URL url = URI.create(imageUrl).toURL();
            return url.openStream();
        }

        throw new IOException("unsupported image url");
    }
}

package com.example.demo.service;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Service
public class ImageFeatureExtractor {

    private static final int HISTOGRAM_BINS = 64;
    private static final int BRIGHTNESS_GRID = 16;
    private static final int VECTOR_SIZE = HISTOGRAM_BINS + BRIGHTNESS_GRID;

    public ImageFeature extract(InputStream inputStream) throws IOException {
        BufferedImage source = ImageIO.read(inputStream);
        if (source == null) {
            throw new IOException("invalid image");
        }

        BufferedImage featureImage = resize(source, 64, 64);
        double[] vector = buildVector(featureImage);
        long hash = buildDHash(resize(source, 9, 8));
        return new ImageFeature(vector, hash);
    }

    public double cosineSimilarity(double[] left, double[] right) {
        if (left.length != right.length || left.length != VECTOR_SIZE) {
            return 0;
        }

        double dot = 0;
        double leftNorm = 0;
        double rightNorm = 0;
        for (int i = 0; i < left.length; i++) {
            dot += left[i] * right[i];
            leftNorm += left[i] * left[i];
            rightNorm += right[i] * right[i];
        }
        if (leftNorm == 0 || rightNorm == 0) {
            return 0;
        }
        return dot / (Math.sqrt(leftNorm) * Math.sqrt(rightNorm));
    }

    public double hashSimilarity(long left, long right) {
        return 1.0 - (Long.bitCount(left ^ right) / 64.0);
    }

    public String serialize(double[] vector) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < vector.length; i++) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(vector[i]);
        }
        return builder.toString();
    }

    public double[] deserialize(String value) {
        if (value == null || value.isBlank()) {
            return new double[0];
        }
        return Arrays.stream(value.split(","))
                .mapToDouble(part -> {
                    try {
                        return Double.parseDouble(part);
                    } catch (NumberFormatException ignored) {
                        return 0;
                    }
                })
                .toArray();
    }

    private double[] buildVector(BufferedImage image) {
        double[] vector = new double[VECTOR_SIZE];
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y));
                int rBin = color.getRed() / 64;
                int gBin = color.getGreen() / 64;
                int bBin = color.getBlue() / 64;
                vector[rBin * 16 + gBin * 4 + bBin] += 1;

                int gridX = Math.min(3, x * 4 / width);
                int gridY = Math.min(3, y * 4 / height);
                double brightness = (color.getRed() * 0.299 + color.getGreen() * 0.587 + color.getBlue() * 0.114) / 255.0;
                vector[HISTOGRAM_BINS + gridY * 4 + gridX] += brightness;
            }
        }

        int pixels = width * height;
        for (int i = 0; i < HISTOGRAM_BINS; i++) {
            vector[i] = vector[i] / pixels;
        }
        int cellPixels = pixels / BRIGHTNESS_GRID;
        for (int i = HISTOGRAM_BINS; i < VECTOR_SIZE; i++) {
            vector[i] = cellPixels == 0 ? 0 : vector[i] / cellPixels;
        }
        return vector;
    }

    private long buildDHash(BufferedImage image) {
        long hash = 0L;
        int bit = 0;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                int left = grayscale(image.getRGB(x, y));
                int right = grayscale(image.getRGB(x + 1, y));
                if (left > right) {
                    hash |= (1L << bit);
                }
                bit++;
            }
        }
        return hash;
    }

    private int grayscale(int rgb) {
        Color color = new Color(rgb);
        return (int) (color.getRed() * 0.299 + color.getGreen() * 0.587 + color.getBlue() * 0.114);
    }

    private BufferedImage resize(BufferedImage source, int width, int height) {
        BufferedImage target = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = target.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.drawImage(source, 0, 0, width, height, null);
        graphics.dispose();
        return target;
    }

    public record ImageFeature(double[] vector, long hash) {
    }
}

package com.example.bkb.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
public class S3PresignedUrlService {

    private final S3Presigner presigner;
    private final String bucket;
    private final Duration uploadTtl;
    private final Duration downloadTtl;

    public S3PresignedUrlService(S3Presigner presigner,
                                 @Value("${app.s3.bucket}") String bucket,
                                 @Value("${app.s3.presigned-url-ttl-minutes}") int ttlMinutes) {
        this.presigner = presigner;
        this.bucket = bucket;
        this.uploadTtl = Duration.ofMinutes(ttlMinutes);
        this.downloadTtl = Duration.ofHours(1);
    }

    // --- Upload (PUT) --- Фронт завантажує частини відео
    public List<String> generateUploadUrls(UUID attemptId, int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> generateUploadUrl(attemptId))
                .toList();
    }

    private String generateUploadUrl(UUID attemptId) {
        var key = "attempts/%s/chunks/%s.webm".formatted(attemptId, UUID.randomUUID());
        var putRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType("video/webm")
                .build();
        var presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(uploadTtl)
                .putObjectRequest(putRequest)
                .build();
        return presigner.presignPutObject(presignRequest).url().toString();
    }

    // --- Download (GET) --- Examiner переглядає докази у браузері
    public List<String> generateDownloadUrls(List<String> s3Keys) {
        return s3Keys.stream()
                .map(this::generateDownloadUrl)
                .toList();
    }

    public String generateDownloadUrl(String key) {
        var getRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        var presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(downloadTtl)
                .getObjectRequest(getRequest)
                .build();
        return presigner.presignGetObject(presignRequest).url().toString();
    }
}

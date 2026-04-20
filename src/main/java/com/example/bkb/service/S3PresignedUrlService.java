package com.example.bkb.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
public class S3PresignedUrlService {

    private final S3Presigner presigner;
    private final String bucket;
    private final Duration ttl;

    public S3PresignedUrlService(S3Presigner presigner,
                                 @Value("${app.s3.bucket}") String bucket,
                                 @Value("${app.s3.presigned-url-ttl-minutes}") int ttlMinutes) {
        this.presigner = presigner;
        this.bucket = bucket;
        this.ttl = Duration.ofMinutes(ttlMinutes);
    }

    public List<String> generateUploadUrls(UUID attemptId, int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> generateSingleUrl(attemptId))
                .toList();
    }

    private String generateSingleUrl(UUID attemptId) {
        var key = "attempts/%s/chunks/%s.webm".formatted(attemptId, UUID.randomUUID());

        var putRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType("video/webm")
                .build();

        var presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(ttl)
                .putObjectRequest(putRequest)
                .build();

        return presigner.presignPutObject(presignRequest).url().toString();
    }
}

package com.project.montap.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class S3Service {

    private AmazonS3 s3Client;
    private final Environment env;

    @PostConstruct
    public void setS3Client() {
        String accessKey = env.getProperty("cloud.aws.credentials.access-key");
        String secretKey = env.getProperty("cloud.aws.credentials.secret-key");
        String region = env.getProperty("cloud.aws.region.static");

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(region).build();
    }

    public String upload(MultipartFile file) throws IOException {
        String bucket = env.getProperty("cloud.aws.s3.bucket");
        StringBuilder sb = new StringBuilder("");
        sb.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        sb.append("-");
        // sb.append(file.getOriginalFilename());
        String fileName = sb.toString();

        s3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return s3Client.getUrl(bucket, fileName).toString();
    }
}

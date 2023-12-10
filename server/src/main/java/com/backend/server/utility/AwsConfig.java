package com.backend.server.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;



@Configuration
public class AwsConfig {

    @Value("${aws.secret-access-key}")
    private String accessKey;

    @Value("${aws.access-key-id}")
    private String accessKeyId;

    @Value("${aws.region}")
    private String region;

    @Bean
    public AmazonS3 s3client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, accessKey);
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }



    

}

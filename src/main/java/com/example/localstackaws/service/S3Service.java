package com.example.localstackaws.service;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.File;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Autowired
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void putFile(String bucket, String key, File file) {
        s3Client.putObject(builder -> builder.bucket(bucket).key(key), RequestBody.fromFile(file));
    }

    public File getFile(String bucket, String key) {
        var file = new File("build/output/getFile.txt");
        var res = s3Client.getObject(builder -> builder.bucket(bucket).key(key));

        try {
            FileUtils.writeByteArrayToFile(file, res.readAllBytes());
        } catch (Exception e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }

        return file;
    }
}
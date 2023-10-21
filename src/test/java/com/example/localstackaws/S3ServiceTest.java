package com.example.localstackaws;

import com.example.localstackaws.service.S3Service;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class S3ServiceTest extends IntegrationTest {

    private static final String TEST_BUCKET = "test-bucket";
    private static final String SAMPLE_KEY = "sampleObject.txt";

    @Autowired
    private S3Service s3Service;

    @Test
    public void s3PutAndGetTest() throws Exception {
        // given
        File sampleFile = createSampleFile("hello world");

        // when
        s3Service.putFile(TEST_BUCKET, SAMPLE_KEY, sampleFile);

        // then
        File resultFile = s3Service.getFile(TEST_BUCKET, SAMPLE_KEY);
        Assertions.assertIterableEquals(
                FileUtils.readLines(sampleFile, StandardCharsets.UTF_8),
                FileUtils.readLines(resultFile, StandardCharsets.UTF_8)
        );

        // finish
        sampleFile.delete();
    }

    private File createSampleFile(String content) throws IOException {
        File tempFile = File.createTempFile("temp", ".txt");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(content);
        }
        return tempFile;
    }
}

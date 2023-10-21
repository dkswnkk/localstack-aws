package com.example.localstackaws;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;

@Disabled
@SpringBootTest
@ContextConfiguration(initializers = IntegrationTest.IntegrationTestInitializer.class)
public class IntegrationTest {
    private static final LocalStackContainer AWS_CONTAINER = new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.11.2"))
            .withServices(LocalStackContainer.Service.S3);

    @BeforeAll
    public static void setupContainers() {
        AWS_CONTAINER.start();
    }

    static class IntegrationTestInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            Map<String, String> properties = new HashMap<>();

            setAwsProperties(properties);

            TestPropertyValues.of(properties).applyTo(applicationContext);
        }

        private void setAwsProperties(Map<String, String> properties) {
            try {
                AWS_CONTAINER.execInContainer(
                        "awslocal",
                        "s3api",
                        "create-bucket",
                        "--bucket",
                        "test-bucket");
                properties.put("cloud.aws.endpoint", AWS_CONTAINER.getEndpoint().toString());

            } catch (Exception e) {
                // nothing
            }
        }
    }
}

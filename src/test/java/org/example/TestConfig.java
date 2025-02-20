package org.example;

public class TestConfig {
    private final String baseUrl = "https://petstore.swagger.io/v2/";
    private final String apiKey = "special-key";

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }
}

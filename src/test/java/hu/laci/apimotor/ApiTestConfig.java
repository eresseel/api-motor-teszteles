package hu.laci.apimotor;

import io.restassured.RestAssured;

final class ApiTestConfig {

    private ApiTestConfig() {
    }

    static void configure() {
        RestAssured.baseURI = System.getProperty("base.url", "http://localhost:18080");
        RestAssured.basePath = "/api";
    }
}

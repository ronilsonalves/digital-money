package com.APITesting.APITesting;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Tag;

import static io.restassured.RestAssured.given;

public class LogoutTest {

    @Test
    @Tag("Spring 1")
    public void LogoutTest_validTokenShouldReturnCode200() {
//        RestAssured.baseURI = "http://localhost:8081";
        RestAssured.baseURI = "http://18.231.109.51:8081";
        RequestSpecification request = given();
        request.header("Content-Type", "application/json");
        String jsonBody = "{"
                + "\"email\": \"johndoe@gmail.com\","
                + "\"password\": \"password123\""
                + "}";
        Response response = request.body(jsonBody).post("/auth/login");
        String bearerToken = response.getBody().jsonPath().getString("token");
        String accessToken = bearerToken.replace("Bearer ", "");
        request.header("Authorization", "Bearer " + accessToken);
        response = request.post("auth/logout");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
    }

    @Test
    @Tag("Spring 1")
    public void LogoutTest_invalidTokenShouldReturnCode401AndProperMessage() {
//        RestAssured.baseURI = "http://localhost:8081";
        RestAssured.baseURI = "http://18.231.109.51:8081";
        RequestSpecification request = given();
        request.header("Content-Type", "application/json");
        String jsonBody = "{"
                + "\"email\": \"johndoe@gmail.com\","
                + "\"password\": \"password123\""
                + "}";
        Response response = request.body(jsonBody).post("/auth/login");
        String bearerToken = response.getBody().jsonPath().getString("token");
        String accessToken = bearerToken.replace("Bearer ", "xxxxxxxx");
        request.header("Authorization", "Bearer " + accessToken);
        response = request.post("auth/logout");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 401);
        System.out.println("Response body: " + response.body().asString());
    }

    @Test
    @Tag("Spring 1")
    public void LogoutTest_expiredTokenShouldReturnCode401AndProperMessage() throws Exception {
//        RestAssured.baseURI = "http://localhost:8081";
        RestAssured.baseURI = "http://18.231.109.51:8081";
        RequestSpecification request = given();
        request.header("Content-Type", "application/json");
        String jsonBody = "{"
                + "\"email\": \"johndoe@gmail.com\","
                + "\"password\": \"password123\""
                + "}";
        Response response = request.body(jsonBody).post("/auth/login");
        Thread.sleep(301000);
        String bearerToken = response.getBody().jsonPath().getString("token");
        String accessToken = bearerToken.replace("Bearer ", "");
        request.header("Authorization", "Bearer " + accessToken);
        response = request.post("auth/logout");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 401);
        System.out.println("Response body: " + response.body().asString());
    }

}

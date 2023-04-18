package com.APITesting.APITesting;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class TransfersRouteVisualizationTest {

    @Test
    @Tag("Sprint 2")
    public void CorrectId_shouldReturnCode200AndDetailedTransfersData() {
        RestAssured.baseURI = "http://18.231.109.51:8081";
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        String jsonBody = "{"
                + "\"email\": \"johnmc@gmail.com\","
                + "\"password\": \"password123\""
                + "}";
        Response response = request.body(jsonBody).post("/auth/login");
        String bearerToken = response.getBody().jsonPath().getString("token");
        String accessToken = bearerToken.replace("Bearer ", "");
        String account_id = "b0f82bff-5f5a-422d-84bd-65f94ea6ba53";
        RestAssured.baseURI = "http://18.231.109.51:8082";
        RequestSpecification request2 = RestAssured.given();
        request2.header("Authorization", "Bearer " + accessToken);
        response = request2.get("/api/accounts/" + account_id + "/transfers");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        System.out.println("Response body: " + response.body().asString());
    }

    @Test
    @Tag("Sprint 2")
    public void InCorrectId_shouldReturnCode404AndProperMessage() {
        RestAssured.baseURI = "http://18.231.109.51:8081";
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        String jsonBody = "{"
                + "\"email\": \"johnmc@gmail.com\","
                + "\"password\": \"password123\""
                + "}";
        Response response = request.body(jsonBody).post("/auth/login");
        String bearerToken = response.getBody().jsonPath().getString("token");
        String accessToken = bearerToken.replace("Bearer ", "");
        String account_id = "b8ccd122-5159-4435-9430-d81ec53f7083";
        RestAssured.baseURI = "http://18.231.109.51:8082";
        RequestSpecification request2 = RestAssured.given();
        request2.header("Authorization", "Bearer " + accessToken);
        response = request2.get("/api/accounts/" + account_id + "/transfers");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404);
        System.out.println("Response body: " + response.body().asString());
    }

    @Test
    @Tag("Sprint 2")
    public void IdFromDifferentUser_shouldReturnCode401AndProperMessage() {
        RestAssured.baseURI = "http://18.231.109.51:8081";
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        String jsonBody = "{"
                + "\"email\": \"johnmc@gmail.com\","
                + "\"password\": \"password123\""
                + "}";
        Response response = request.body(jsonBody).post("/auth/login");
        String bearerToken = response.getBody().jsonPath().getString("token");
        String accessToken = bearerToken.replace("Bearer ", "");
        String account_id = "1d7cc85c-96c8-4cbf-95ff-2a5a3ff30ba7";
        RestAssured.baseURI = "http://18.231.109.51:8082";
        RequestSpecification request2 = RestAssured.given();
        request2.header("Authorization", "Bearer " + accessToken);
        response = request2.get("/api/accounts/" + account_id + "/transfers");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 401);
        System.out.println("Response body: " + response.body().asString());
    }
}

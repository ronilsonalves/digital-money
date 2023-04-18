package com.APITesting.APITesting;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class TransactionsVisualizationByAmountTest {

    @Test
    @Tag("Sprint 4")
    public void Between0And1000_shouldReturnCode200AndActivityData() {
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
        request2.queryParam("page", "0");
        request2.queryParam("size", "10");
        request2.queryParam("sort", "transactionDate");
        request2.queryParam("direction", "DESC");
        request2.queryParam("startDate", "");
        request2.queryParam("endDate", "");
        request2.queryParam("transactionType", "DEPÓSITO");
        request2.queryParam("activityType", "AMBOS");
        request2.queryParam("minAmount", 0);
        request2.queryParam("maxAmount", 1000);
        response = request2.get("/api/accounts/" + account_id + "/activity");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        System.out.println("Response body: " + response.body().asString());
    }

    @Test
    @Tag("Sprint 4")
    public void Between1000And5000_shouldReturnCode200AndActivityData() {
        RestAssured.baseURI = "http://18.231.109.51:8081";
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        String jsonBody = "{"
                + "\"email\": \"falecom@ronilsonalves.com\","
                + "\"password\": \"12345678\""
                + "}";
        Response response = request.body(jsonBody).post("/auth/login");
        String bearerToken = response.getBody().jsonPath().getString("token");
        String accessToken = bearerToken.replace("Bearer ", "");
        String account_id = "1d7cc85c-96c8-4cbf-95ff-2a5a3ff30ba7";
        RestAssured.baseURI = "http://18.231.109.51:8082";
        RequestSpecification request2 = RestAssured.given();
        request2.header("Authorization", "Bearer " + accessToken);
        request2.queryParam("page", "0");
        request2.queryParam("size", "10");
        request2.queryParam("sort", "transactionDate");
        request2.queryParam("direction", "");
        request2.queryParam("startDate", "");
        request2.queryParam("endDate", "");
        request2.queryParam("transactionType", "DEPÓSITO");
        request2.queryParam("activityType", "");
        request2.queryParam("minAmount", 1000);
        request2.queryParam("maxAmount", 5000);
        response = request2.get("/api/accounts/" + account_id + "/activity");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        System.out.println("Response body: " + response.body().asString());
    }
}

package com.APITesting.APITesting;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class TransactionsOrderVisualization {

    @Test
    @Tag("Sprint 4")
    public void CorrectAccountId_shouldReturnCode200AndDESCOrderedActivityData() {
        RestAssured.baseURI = "http://18.231.109.51:8081";
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        String jsonBody = "{"
                + "\"email\": \"johndoe@gmail.com\","
                + "\"password\": \"password123\""
                + "}";
        Response response = request.body(jsonBody).post("/auth/login");
        String bearerToken = response.getBody().jsonPath().getString("token");
        String accessToken = bearerToken.replace("Bearer ", "");
        String account_id = "a8ccd122-5159-4435-9430-d81ec53f7089";
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
        request2.queryParam("minAmount", 50);
        request2.queryParam("maxAmount", 2000);
        response = request2.get("/api/accounts/" + account_id + "/activity");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        System.out.println("Response body: " + response.body().asString());
    }

    @Test
    @Tag("Sprint 4")
    public void CorrectAccountId_shouldReturnCode200AndASCOrderedActivityData() {
        RestAssured.baseURI = "http://18.231.109.51:8081";
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        String jsonBody = "{"
                + "\"email\": \"johndoe@gmail.com\","
                + "\"password\": \"password123\""
                + "}";
        Response response = request.body(jsonBody).post("/auth/login");
        String bearerToken = response.getBody().jsonPath().getString("token");
        String accessToken = bearerToken.replace("Bearer ", "");
        String account_id = "a8ccd122-5159-4435-9430-d81ec53f7089";
        RestAssured.baseURI = "http://18.231.109.51:8082";
        RequestSpecification request2 = RestAssured.given();
        request2.header("Authorization", "Bearer " + accessToken);
        request2.queryParam("page", "0");
        request2.queryParam("size", "10");
        request2.queryParam("sort", "transactionDate");
        request2.queryParam("direction", "ASC");
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
}

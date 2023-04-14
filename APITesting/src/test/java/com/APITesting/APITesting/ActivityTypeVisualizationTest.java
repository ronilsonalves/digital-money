package com.APITesting.APITesting;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class ActivityTypeVisualizationTest {

    @Test
    @Tag("Sprint 4")
    public void Income_shouldReturnCode200AndActivityData() {
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
        request2.queryParam("transactionType", "");
        request2.queryParam("activityType", "RECEITA");
        request2.queryParam("minAmount", "");
        request2.queryParam("maxAmount", "");
        response = request2.get("/api/accounts/" + account_id + "/activity");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        System.out.println("Response body: " + response.body().asString());
    }

    @Test
    @Tag("Sprint 4")
    public void Expense_shouldReturnCode200AndActivityData() {
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
        request2.queryParam("transactionType", "");
        request2.queryParam("activityType", "DESPESA");
        request2.queryParam("minAmount", "");
        request2.queryParam("maxAmount", "");
        response = request2.get("/api/accounts/" + account_id + "/activity");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        System.out.println("Response body: " + response.body().asString());
    }
}

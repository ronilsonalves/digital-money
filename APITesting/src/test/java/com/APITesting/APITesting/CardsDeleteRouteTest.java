package com.APITesting.APITesting;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class CardsDeleteRouteTest {

    @Test
    @Tag("Sprint 2")
    public void DeleteCardById_shouldReturnCode204() {
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
        String card_id = "9b475168-6848-4e9b-8378-84e9ff12bd2a";
        RestAssured.baseURI = "http://18.231.109.51:8082";
        RequestSpecification request2 = RestAssured.given();
        request2.header("Authorization", "Bearer " + accessToken);
        response = request2.delete("/api/accounts/" + account_id + "/cards/" + card_id);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 204);
    }

    @Test
    @Tag("Sprint 2")
    public void DeleteCardByInexistentAccountId_shouldReturnCode404AndProperMessage() {
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
        String account_id = "c8ccd122-5159-4435-9430-d81ec53f7086";
        String card_id = "eec716a2-9a1c-4603-8e44-3dd907990721";
        RestAssured.baseURI = "http://18.231.109.51:8082";
        RequestSpecification request2 = RestAssured.given();
        request2.header("Authorization", "Bearer " + accessToken);
        response = request2.delete("/api/accounts/" + account_id + "/cards/" + card_id);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404);
        System.out.println("Response body: " + response.body().asString());
    }

    @Test
    @Tag("Sprint 2")
    public void DeleteCardByInexistentId_shouldReturnCode404AndProperMessage() {
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
        String card_id = "fe3b2b63-d8bc-43fb-869d-c3b397db8461";
        RestAssured.baseURI = "http://18.231.109.51:8082";
        RequestSpecification request2 = RestAssured.given();
        request2.header("Authorization", "Bearer " + accessToken);
        response = request2.delete("/api/accounts/" + account_id + "/cards/" + card_id);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404);
        System.out.println("Response body: " + response.body().asString());
    }

    @Test
    @Tag("Sprint 2")
    public void DeleteCardWithAnotherUserId_shouldReturnCode401AndProperMessage() {
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
        String card_id = "9b475168-6848-4e9b-8378-84e9ff12bd2a";
        RestAssured.baseURI = "http://18.231.109.51:8082";
        RequestSpecification request2 = RestAssured.given();
        request2.header("Authorization", "Bearer " + accessToken);
        response = request2.delete("/api/accounts/" + account_id + "/cards/" + card_id);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 401);
        System.out.println("Response body: " + response.body().asString());
    }
}

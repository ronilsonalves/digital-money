package com.APITesting.APITesting;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class CardsCreationRouteTest {

    @Test
    @Tag("Sprint 2")
    public void CorrectId_shouldReturnCode201AndCardData() {
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
        request2.headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json");
        String jsonBody2 = "{"
                + "\"cvc\": \"243\","
                + "\"first_last_name\": \"Doezzy\","
                + "\"expiration_date\": \"2023-11\","
                + "\"number\": 6214567891234566"
                + "}";
        response = request2.body(jsonBody2).post("/api/accounts/" + account_id + "/cards");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 201);
        System.out.println("Response body: " + response.body().asString());
    }

    @Test
    @Tag("Sprint 2")
    public void IncorrectId_shouldReturnCode404AndProperMessage() {
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
        String account_id = "08ccd122-5159-4435-9430-d81ec53f7086";
        RestAssured.baseURI = "http://18.231.109.51:8082";
        RequestSpecification request2 = RestAssured.given();
        request2.headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json");
        String jsonBody2 = "{"
                + "\"cvc\": \"236\","
                + "\"first_last_name\": \"Doez\","
                + "\"expiration_date\": \"2023-10\","
                + "\"number\": 9214567891234562"
                + "}";
        response = request2.body(jsonBody2).post("/api/accounts/" + account_id + "/cards");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404);
        System.out.println("Response body: " + response.body().asString());
    }

    @Test
    @Tag("Sprint 2")
    public void RepeatedCardNumber_shouldReturnCode409AndProperMessage() {
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
        request2.headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json");
        String jsonBody2 = "{"
                + "\"cvc\": \"236\","
                + "\"first_last_name\": \"Doez\","
                + "\"expiration_date\": \"2023-10\","
                + "\"number\": 9214567891234562"
                + "}";
        response = request2.body(jsonBody2).post("/api/accounts/" + account_id + "/cards");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 409);
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
        request2.headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json");
        String jsonBody2 = "{"
                + "\"cvc\": \"237\","
                + "\"first_last_name\": \"Doe\","
                + "\"expiration_date\": \"2023-09\","
                + "\"number\": 6214567891234568"
                + "}";
        response = request2.body(jsonBody2).post("/api/accounts/" + account_id + "/cards");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 401);
        System.out.println("Response body: " + response.body().asString());
    }
}

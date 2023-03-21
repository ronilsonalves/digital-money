package com.APITesting.APITesting;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class TransfersRouteTest {

    @Test
    @Tag("Sprint 3")
    public void CorrectIDs_shouldReturnCode201AndTransferData() {
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
        request2.headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json");
        String jsonBody2 = "{"
                + "\"originAccountNumber\": \"a8ccd122-5159-4435-9430-d81ec53f7089\","
                + "\"recipientAccountNumber\": \"1d7cc85c-96c8-4cbf-95ff-2a5a3ff30ba7\","
                + "\"transactionAmount\": 100,"
                + "\"transactionDate\": \"2023-03-21\","
                + "\"transactionType\": \"TRANSFERÊNCIA\","
                + "\"description\": \"Segue 100\""
                + "}";
        response = request2.body(jsonBody2).post("/api/accounts/" + account_id + "/transfers");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 201);
        System.out.println("Response body: " + response.body().asString());
    }

    //Falhando, em vias de correção.
    @Test
    @Tag("Sprint 3")
    public void IncorrectOriginID_shouldReturnCode404AndProperMessage() {
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
        request2.headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json");
        String jsonBody2 = "{"
                + "\"originAccountNumber\": \"b8ccd122-5159-4435-9430-d81ec53f7086\","
                + "\"recipientAccountNumber\": \"1d7cc85c-96c8-4cbf-95ff-2a5a3ff30ba7\","
                + "\"transactionAmount\": 100,"
                + "\"transactionDate\": \"2023-03-21\","
                + "\"transactionType\": \"TRANSFERÊNCIA\","
                + "\"description\": \"Segue 100\""
                + "}";
        response = request2.body(jsonBody2).post("/api/accounts/" + account_id + "/transfers");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404);
        System.out.println("Response body: " + response.body().asString());
    }

    @Test
    @Tag("Sprint 3")
    public void IncorrectRecipientID_shouldReturnCode404AndProperMessage() {
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
        request2.headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json");
        String jsonBody2 = "{"
                + "\"originAccountNumber\": \"a8ccd122-5159-4435-9430-d81ec53f7089\","
                + "\"recipientAccountNumber\": \"2d7cc85c-96c8-4cbf-95ff-2a5a3ff30ba5\","
                + "\"transactionAmount\": 100,"
                + "\"transactionDate\": \"2023-03-21\","
                + "\"transactionType\": \"TRANSFERÊNCIA\","
                + "\"description\": \"Segue 100\""
                + "}";
        response = request2.body(jsonBody2).post("/api/accounts/" + account_id + "/transfers");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404);
        System.out.println("Response body: " + response.body().asString());
    }

    //Falhando, em vias de correção.
    @Test
    @Tag("Sprint 3")
    public void InvalidDate_shouldReturnCode404AndProperMessage() {
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
        request2.headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json");
        String jsonBody2 = "{"
                + "\"originAccountNumber\": \"a8ccd122-5159-4435-9430-d81ec53f7089\","
                + "\"recipientAccountNumber\": \"1d7cc85c-96c8-4cbf-95ff-2a5a3ff30ba7\","
                + "\"transactionAmount\": 100,"
                + "\"transactionDate\": \"2023-02-21\","
                + "\"transactionType\": \"TRANSFERÊNCIA\","
                + "\"description\": \"Segue 100\""
                + "}";
        response = request2.body(jsonBody2).post("/api/accounts/" + account_id + "/transfers");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404);
        System.out.println("Response body: " + response.body().asString());
    }

    @Test
    @Tag("Sprint 3")
    public void IdFromDifferentUser_shouldReturnCode401AndProperMessage() {
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
        String account_id = "1d7cc85c-96c8-4cbf-95ff-2a5a3ff30ba7";
        RestAssured.baseURI = "http://18.231.109.51:8082";
        RequestSpecification request2 = RestAssured.given();
        request2.headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json");
        String jsonBody2 = "{"
                + "\"originAccountNumber\": \"a8ccd122-5159-4435-9430-d81ec53f7089\","
                + "\"recipientAccountNumber\": \"1d7cc85c-96c8-4cbf-95ff-2a5a3ff30ba7\","
                + "\"transactionAmount\": 100,"
                + "\"transactionDate\": \"2023-03-21\","
                + "\"transactionType\": \"TRANSFERÊNCIA\","
                + "\"description\": \"Segue 100\""
                + "}";
        response = request2.body(jsonBody2).post("/api/accounts/" + account_id + "/transfers");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 401);
        System.out.println("Response body: " + response.body().asString());
    }

    @Test
    @Tag("Sprint 3")
    public void NotEnoughFunds_shouldReturnCode410AndProperMessage() {
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
        String account_id = "f81f973b-a3b1-47c0-a3cf-1b77d4bc2ee1";
        RestAssured.baseURI = "http://18.231.109.51:8082";
        RequestSpecification request2 = RestAssured.given();
        request2.headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json");
        String jsonBody2 = "{"
                + "\"originAccountNumber\": \"f81f973b-a3b1-47c0-a3cf-1b77d4bc2ee1\","
                + "\"recipientAccountNumber\": \"1d7cc85c-96c8-4cbf-95ff-2a5a3ff30ba7\","
                + "\"transactionAmount\": 100,"
                + "\"transactionDate\": \"2023-03-21\","
                + "\"transactionType\": \"TRANSFERÊNCIA\","
                + "\"description\": \"Segue 100\""
                + "}";
        response = request2.body(jsonBody2).post("/api/accounts/" + account_id + "/transfers");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 410);
        System.out.println("Response body: " + response.body().asString());
    }
}

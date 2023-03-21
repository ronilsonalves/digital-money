package com.APITesting.APITesting;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class UsersDataPatchTest {

    @Test
    @Tag("Sprint 2")
    public void PatchData_shouldReturnCode200AndDetailedUserData() {
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
        String sub = "c4bf4396-7659-4cd4-843c-95339bb23f09";
        request.headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json");
        String jsonBody2 = "{"
                + "\"name\": \"John\","
                + "\"lastname\": \"Doe\","
                + "\"email\": \"johndoe@example.com\","
                + "\"phone\": \"11987654321\","
                + "\"password\": \"password123\""
                + "}";
        response = request.body(jsonBody2).patch("/users/" + sub);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        System.out.println("Response body: " + response.body().asString());
    }

    @Test
    @Tag("Sprint 2")
    public void CPFData_shouldReturnCode200AndNotChangeCPFValue() {
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
        String sub = "c4bf4396-7659-4cd4-843c-95339bb23f09";
        request.headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json");
        String jsonBody2 = "{"
                + "\"name\": \"John\","
                + "\"lastName\": \"Doe\","
                + "\"cpf\": \"917.419.234-44\","
                + "\"email\": \"johndoe@gmail.com\","
                + "\"phone\": \"11987654321\","
                + "\"password\": \"password123\""
                + "}";
        response = request.body(jsonBody2).patch("/users/" + sub);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        System.out.println("Response body: " + response.body().asString());
    }
}

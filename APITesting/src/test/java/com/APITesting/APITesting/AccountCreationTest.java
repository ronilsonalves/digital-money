package com.APITesting.APITesting;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Test;

public class AccountCreationTest {

    @Test
    public void UsersAccountCreationTest_validInputShouldReturnCode201AndId() {
        RestAssured.baseURI = "http://localhost:8081";
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        String jsonBody = "{"
                + "\"name\": \"John\","
                + "\"lastName\": \"Doe\","
                + "\"cpf\": \"617.419.234-22\","
                + "\"email\": \"johndoe@gmail.com\","
                + "\"phone\": \"11987654321\","
                + "\"password\": \"password123\""
                + "}";
        Response response = request.body(jsonBody).post("/users/register");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 201);
        System.out.println("Response body: " + response.body().asString());
    }

    @Test
    public void UsersAccountCreationTest_invalidInputShouldGiveCode400() {
        RestAssured.baseURI = "http://localhost:8081";
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        String jsonBody = "{"
                + "\"name\": \"John\","
                + "\"lastname\": \"Doe\","
                + "\"cpf\": \"617.419.234-22\","
                + "\"email\": \"johndoe@example.com\","
                + "\"phone\": \"+5511987654321\","
                + "\"password\": \"password123\""
                + "}";
        Response response = request.body(jsonBody).post("/users/register");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 400);
    }
}

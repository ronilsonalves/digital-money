package com.APITesting.APITesting;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Tag;

public class PasswordResetTest {

    @Test
    @Tag("Spring 1")
    public void PasswordResetTest_validEmailShouldReturnCode200() {
//        RestAssured.baseURI = "http://localhost:8081";
        RestAssured.baseURI = "http://18.231.109.51:8081";
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        String jsonBody = "{"
                + "\"emailAddress\": \"johndoe@gmail.com\""
                + "}";
        Response response = request.body(jsonBody).post("/users/reset-password");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        System.out.println("Response body: " + response.body().asString());
    }

    @Test
    @Tag("Spring 1")
    public void PasswordResetTest_invalidEmailShouldReturnCode404AndProperMessage() {
        RestAssured.baseURI = "http://localhost:8081";
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        String jsonBody = "{"
                + "\"emailAddress\": \"johndoe@blablabla.com\""
                + "}";
        Response response = request.body(jsonBody).post("/users/reset-password");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404);
        System.out.println("Response body: " + response.body().asString());
    }
}

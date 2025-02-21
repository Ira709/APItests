package org.example;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RestApiTest {
        static User testUser;
        static Pet testPet;

    UserController userController = new UserController();
    @BeforeEach
    void createTestData() {
        testUser = new User(1, "TestUser", "John", "Doe", "test@example.com", "password123", "123456789", 1);
        Response userResponse = userController.addUser(testUser);
        assertNotNull(userResponse, "Response is null");
        assertEquals(200, userResponse.statusCode(), "Unexpected status code");

        testPet = new Pet(10, "Buddy", "available");
        String orderJson = "{ \"id\": 1, \"petId\": 10, \"quantity\": 1, \"shipDate\": \"2024-02-14T00:00:00.000+0000\", \"status\": \"placed\", \"complete\": true }";
        Response petResponse = given()
                .contentType("application/json")
                .body(orderJson)
                .post("https://petstore.swagger.io/v2/store/order")
                .andReturn();
        assertNotNull(petResponse, "Order creation response is null");
        assertEquals(200, petResponse.statusCode(), "Order not created");
    }
    @Test
    void testAddUser() {
        Response response = userController.addUser(testUser);
        assertNotNull(response, "Response is null");
        assertEquals(200, response.statusCode(), "Unexpected status code");
        response.then()
                .assertThat()
                .body("message", equalTo(String.valueOf(testUser.getId())));
    }

    @Test
    void testGetUserByName() {
        Response response = userController.getUserByName(testUser.getUsername());
        assertNotNull(response, "Response is null");
        assertEquals(200, response.statusCode(), "Unexpected status code");
        response.then()
                .assertThat()
                .body("username", equalTo(testUser.getUsername()))
                .body("firstName", equalTo(testUser.getFirstName()))
                .body("lastName", equalTo(testUser.getLastName()))
                .body("email", equalTo(testUser.getEmail()));
    }

    @Test
    void testUpdatePet() {
        testPet = new Pet(10, "Max", "sold");
        Response response = userController.updatePet(testPet);
        assertNotNull(response, "Response is null");
        assertEquals(200, response.statusCode(), "Unexpected status code");
        response.then()
                .assertThat()
                .body("id", equalTo((int) testPet.getId()))
                .body("name", equalTo(testPet.getName()))
                .body("status", equalTo(testPet.getStatus()));
    }
    @Test
    void testDeleteStoreOrder() {
        Response response = userController.deleteStoreOrder(1);
        assertNotNull(response, "Response is null");
        assertEquals(200, response.statusCode(), "Unexpected status code");
        response.then()
                .assertThat()
                .body("message", equalTo("1"));
        Response checkResponse = given()
                .get("https://petstore.swagger.io/v2/store/order/1")
                .andReturn();
        assertEquals(404, checkResponse.statusCode(), "Order was not deleted");
    }
}

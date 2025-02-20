package org.example;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.qameta.allure.Step;
import static io.restassured.RestAssured.given;

public class UserController {

    TestConfig config = new TestConfig();
    RequestSpecification requestSpecification = given();

    public UserController() {
        RestAssured.defaultParser = io.restassured.parsing.Parser.JSON;
        this.requestSpecification = given()
                .header("api_key", config.getApiKey())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .baseUri(config.getBaseUrl())
                .filter(new AllureRestAssured());
    }

    @Step("Add user")
    public Response addUser(User user) {
        this.requestSpecification.body(user);
        return this.requestSpecification.post("user").andReturn();
    }

    @Step("Get user by name")
    public Response getUserByName(String username) {
        return this.requestSpecification.get("user/" + username).andReturn();
    }

    @Step("Delete user by name")
    public Response deleteUserByName(String username) {
        return this.requestSpecification.delete("user/" + username).andReturn();
    }

    @Step("Delete store order")
    public Response deleteStoreOrder(int orderId) {
        return this.requestSpecification.delete("store/order/" + orderId).andReturn();
    }

    @Step("Update pet")
    public Response updatePet(Pet pet) {
        this.requestSpecification.body(pet);
        return this.requestSpecification.put("pet").andReturn();
    }
}


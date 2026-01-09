package br.com.fiap.baitersburger_orders.cucumber.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import org.springframework.boot.test.web.server.LocalServerPort;


import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class GetOrderSteps {

    @LocalServerPort
    private int port;

    private Response response;
    private String orderId;


    @Given("That exists an order registered with ID {string}")
    public void thatExistsAnOrderRegisteredWithIDString(String id) {
        this.orderId = id;
    }


    @When("I send a GET request for {string}")
    public void iSendAGETRequestForString(String path) {
        response = given()
                .port(port)
                .when()
                .get(path + "/" + orderId);
    }

    @Then("The response status should be {int}")
    public void theResponseStatusShouldBeInt(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Then("The response body should be validated against {string}")
    public void theResponseBodyShouldBeValidatedAgainst(String filePath) {
        response.then().assertThat().body(matchesJsonSchemaInClasspath(filePath));
    }

    @Given("That does not exist an order registered with ID {string}")
    public void thatDoesNotExistAnOrderRegisteredWithID(String orderId) {
        this.orderId = orderId;
    }
}

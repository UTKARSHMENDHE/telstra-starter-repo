package stepDefinitions;

import au.com.telstra.simcardactivator.entity.SimCardActivation;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimCardActivatorStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<Map> activationResponse;
    private ResponseEntity<Map> queryResponse;

    @Given("the SIM card actuator service is running")
    public void the_sim_card_actuator_service_is_running() {
        // Assuming the actuator service is already running
    }

    @When("I send an activation request with ICCID {string} and customer email {string}")
    public void i_send_an_activation_request_with_iccid_and_customer_email(String iccid, String customerEmail) {
        String requestBody = String.format("{\"iccid\":\"%s\",\"customerEmail\":\"%s\"}", iccid, customerEmail);
        activationResponse = restTemplate.postForEntity("/activate", requestBody, Map.class);
    }

    @Then("the activation should be successful")
    public void the_activation_should_be_successful() {
        assertTrue(activationResponse.getStatusCode().is2xxSuccessful());
    }

    @Then("the database record with ID {int} should have ICCID {string}, customer email {string}, and active status {string}")
    public void the_database_record_with_id_should_have_iccid_customer_email_and_active_status(int id, String iccid, String customerEmail, String activeStatus) {
        queryResponse = restTemplate.getForEntity("/query?simCardId=" + id, Map.class);
        Map<String, Object> responseBody = queryResponse.getBody();

        assertEquals(iccid, responseBody.get("iccid"));
        assertEquals(customerEmail, responseBody.get("customerEmail"));
        assertEquals(Boolean.parseBoolean(activeStatus), responseBody.get("active"));
    }

    @Then("the activation should fail")
    public void the_activation_should_fail() {
        assertTrue(activationResponse.getStatusCode().is2xxSuccessful());
    }
}

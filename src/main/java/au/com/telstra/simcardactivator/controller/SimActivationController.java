package au.com.telstra.simcardactivator.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class SimActivationController {

    @PostMapping("/activate")
    public void activateSim(@RequestBody Map<String, String> payload) {
        String iccid = payload.get("iccid");
        String customerEmail = payload.get("customerEmail");

        // Create a request to the actuator service
        Map<String, String> actuatorRequest = Map.of("iccid", iccid);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Boolean> response = restTemplate.postForObject("http://localhost:8444/actuate", actuatorRequest, Map.class);

        // Print the response
        if (response != null && response.get("success") != null) {
            System.out.println("Activation was successful: " + response.get("success"));
        } else {
            System.out.println("Failed to get a valid response from the actuator.");
        }
    }
}

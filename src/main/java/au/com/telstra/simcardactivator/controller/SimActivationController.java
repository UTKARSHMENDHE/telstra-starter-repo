package au.com.telstra.simcardactivator.controller;

import au.com.telstra.simcardactivator.entity.SimCardActivation;
import au.com.telstra.simcardactivator.repository.SimCardActivationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@RestController
public class SimActivationController {

    @Autowired
    private SimCardActivationRepository repository;

    @PostMapping("/activate")
    public void activateSim(@RequestBody Map<String, String> payload) {
        String iccid = payload.get("iccid");
        String customerEmail = payload.get("customerEmail");

        // Create a request to the actuator service
        Map<String, String> actuatorRequest = Map.of("iccid", iccid);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Boolean> response = restTemplate.postForObject("http://localhost:8088/actuate", actuatorRequest, Map.class);

        // Save the activation record to the database
        boolean isActive = response != null && Boolean.TRUE.equals(response.get("success"));
        SimCardActivation activation = new SimCardActivation(iccid, customerEmail, isActive);
        repository.save(activation);
    }

    @GetMapping("/query")
    public Map<String, Object> querySimCard(@RequestParam Long simCardId) {
        Optional<SimCardActivation> activation = repository.findById(simCardId);
        if (activation.isPresent()) {
            SimCardActivation simCardActivation = activation.get();
            return Map.of(
                "iccid", simCardActivation.getIccid(),
                "customerEmail", simCardActivation.getCustomerEmail(),
                "active", simCardActivation.isActive()
            );
        } else {
            throw new RuntimeException("SIM Card Activation record not found");
        }
    }
}

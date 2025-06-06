Feature: SIM Card Activation

  Scenario: Successful SIM card activation
    Given the SIM card actuator service is running
    When I send an activation request with ICCID "1255789453849037777" and customer email "success@example.com"
    Then the activation should be successful
    And the database record with ID 1 should have ICCID "1255789453849037777", customer email "success@example.com", and active status true

  Scenario: Failed SIM card activation
    Given the SIM card actuator service is running
    When I send an activation request with ICCID "8944500102198304826" and customer email "fail@example.com"
    Then the activation should fail
    And the database record with ID 2 should have ICCID "8944500102198304826", customer email "fail@example.com", and active status false

package io.pivotal;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class OneSteps {

    @Autowired
    TestProperties testProperties;

    @Given("account balance is {double}")
    public void givenAccountBalance(Double initialBalance) {
        log.info("Props has {} and passed {}", testProperties, initialBalance);
    }

    @When("the account is credited with {double}")
    public void theAccountIsCreditedWith(Double accountCredit) {
        log.info("Props has {} and passed {}", testProperties, accountCredit);
    }

    @Then("account should have a balance of {double}")
    public void accountShouldHaveABalanceOf(Double expectedBalance) {
        log.info("Props has {} and passed {}", testProperties, expectedBalance);
    }
}

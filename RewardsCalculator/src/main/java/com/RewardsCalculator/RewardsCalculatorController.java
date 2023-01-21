package com.RewardsCalculator;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class RewardsCalculatorController {

    RewardsCalculatorService rewardsCalculatorService = new RewardsCalculatorService();

    @GetMapping("/calculate")
    public HashMap<String, String> calculateRewards(@RequestBody String transactionList) throws JsonProcessingException {

        return rewardsCalculatorService.calcRewards(transactionList);
    }
}

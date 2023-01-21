package com.RewardsCalculator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Service
public class RewardsCalculatorService {

    private double sportCheckAmount;
    private double timHortonsAmount;
    private double subwayAmount;
    private double otherTransactionsAmount;
    private int rewardPoints;

    public LinkedHashMap<String, String> calcRewards(String transactionList) throws JsonProcessingException {
        sportCheckAmount = 0;
        timHortonsAmount = 0;
        subwayAmount = 0;
        otherTransactionsAmount = 0;
        rewardPoints = 0;
        LinkedHashMap<String, String> result = new LinkedHashMap<>();

        // Parse through input Transaction list
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(transactionList);
        LinkedHashMap<String, Map<String, String>> transactionMap  = mapper.readValue(transactionList,
                new TypeReference<LinkedHashMap<String, Map<String, String>>>(){});
        Set<String> keys = transactionMap.keySet();
        // Calculate total spent at each merchant.
        for (String key : keys) {
            Map<String, String> currTransaction = transactionMap.get(key);

            if (currTransaction.get("merchant_code").contains("sportcheck")) {
                sportCheckAmount += Double.parseDouble(currTransaction.get("amount_cents"));
            }
            else if (currTransaction.get("merchant_code").contains("tim_hortons")) {
                timHortonsAmount += Double.parseDouble(currTransaction.get("amount_cents"));
            }
            else if (currTransaction.get("merchant_code").contains("subway")) {
                subwayAmount += Double.parseDouble(currTransaction.get("amount_cents"));
            }
            else {
                otherTransactionsAmount += Double.parseDouble(currTransaction.get("amount_cents"));
            }
        }
        // Calculate maximum rewards based on how much was spent at each merchant.
        while (sportCheckAmount > 7500 && timHortonsAmount > 2500 && subwayAmount > 2500) {
            rewardPoints += 500;
            sportCheckAmount -= 7500;
            timHortonsAmount -= 2500;
            subwayAmount -= 2500;
        }
        while (sportCheckAmount > 7500 && timHortonsAmount > 2500) {
            rewardPoints += 300;
            sportCheckAmount -= 7500;
            timHortonsAmount -= 2500;
        }
        while (sportCheckAmount > 2500 && timHortonsAmount > 1000 && subwayAmount > 1000) {
            rewardPoints += 150;
            sportCheckAmount -= 2500;
            timHortonsAmount -= 1000;
            subwayAmount -= 1000;
        }
        while (sportCheckAmount > 2000) {
            rewardPoints += 75;
            sportCheckAmount -= 2000;
        }
        // Add leftover to points
        int leftoverAmount = (int) ((otherTransactionsAmount + sportCheckAmount + timHortonsAmount + subwayAmount)/ 100.0);
        rewardPoints += leftoverAmount;
        result.put("Total Rewards Points", Integer.toString(rewardPoints));

        return result;
    }
}

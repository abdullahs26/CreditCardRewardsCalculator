package com.RewardsCalculator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    public HashMap<String, String> calcRewards(String transactionList) throws JsonProcessingException {
        sportCheckAmount = 0;
        timHortonsAmount = 0;
        subwayAmount = 0;
        otherTransactionsAmount = 0;
        rewardPoints = 0;

        HashMap<String, String> result = new HashMap<>();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(transactionList);
        LinkedHashMap<String, Map<String, String>> transactionMap  = mapper.readValue(transactionList,
                new TypeReference<LinkedHashMap<String, Map<String, String>>>(){});
        Set<String> keys = transactionMap.keySet();

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

        // Convert to dollars from cents
        sportCheckAmount /= 100.0;
        timHortonsAmount /= 100.0;
        subwayAmount /= 100.0;
        otherTransactionsAmount /= 100.0;


        while (sportCheckAmount > 75 && timHortonsAmount > 25 && subwayAmount > 25) {
            rewardPoints += 500;
            sportCheckAmount -= 75;
            timHortonsAmount -= 25;
            subwayAmount -= 25;
        }

        while (sportCheckAmount > 75 && timHortonsAmount > 25) {
            rewardPoints += 300;
            sportCheckAmount -= 75;
            timHortonsAmount -= 25;
        }

        while (sportCheckAmount > 25 && timHortonsAmount > 10 && subwayAmount > 10) {
            rewardPoints += 150;
            sportCheckAmount -= 25;
            timHortonsAmount -= 10;
            subwayAmount -= 10;
        }

        while (sportCheckAmount > 20) {
            rewardPoints += 75;
            sportCheckAmount -= 20;
        }

        int leftoverAmount = (int) (otherTransactionsAmount + sportCheckAmount + timHortonsAmount + subwayAmount);

        rewardPoints += leftoverAmount;

        result.put("Total Rewards Points", Integer.toString(rewardPoints));

        return result;
    }
}

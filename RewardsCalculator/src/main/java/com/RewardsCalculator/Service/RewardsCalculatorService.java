package com.RewardsCalculator.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Service
public class RewardsCalculatorService {

    // Variable to store the amount spent at sportCheck
    private double sportCheckAmount;
    // Variable to store the amount spent at Tim Hortons
    private double timHortonsAmount;
    // Variable to store the amount spent at Subway
    private double subwayAmount;
    // Variable to store the amount spent at any other merchants.
    private double otherTransactionsAmount;
    // Variable to store total reward points across the transactions over the month.
    private int rewardPoints;

    /**
     * This function takes the transaction list as input and returns the reward point calculation as output in a map.
     * Note, Rules 3 and 5 are ignored because there will never be a case where using these rules will maximize points.
     * Combination of other rules will always outperform Rules 3 and 5.
     * @param transactionList
     * @return Map containing maximum rewards per transaction and maximum total rewards for the month.
     */
    public LinkedHashMap<String, String> calcRewards(String transactionList) throws JsonProcessingException {
        sportCheckAmount = 0;
        timHortonsAmount = 0;
        subwayAmount = 0;
        otherTransactionsAmount = 0;
        rewardPoints = 0;
        LinkedHashMap<String, String> result = new LinkedHashMap<>();

        // Parse through input Transaction list
        ObjectMapper mapper = new ObjectMapper();
        LinkedHashMap<String, Map<String, String>> transactionMap  = mapper.readValue(transactionList,
                new TypeReference<LinkedHashMap<String, Map<String, String>>>(){});
        Set<String> keys = transactionMap.keySet();
        // Calculate total spent at each merchant.
        for (String key : keys) {
            int transactionRewardPoints = 0;
            Map<String, String> currTransaction = transactionMap.get(key);
            double currAmount = Double.parseDouble(currTransaction.get("amount_cents")) / 100.0;

            if (currTransaction.get("merchant_code").equals("sportcheck")) {
                sportCheckAmount += currAmount;
                transactionRewardPoints = (75 * (int) ((currAmount) / 20));
                // Add leftover amount (Use Rule 7)
                currAmount -= ((int) (currAmount / 20)) * 20;
                if (currAmount > 0) {
                    transactionRewardPoints += currAmount;
                }
                result.put(key, String.valueOf(transactionRewardPoints));
            }
            else if (currTransaction.get("merchant_code").equals("tim_hortons")) {
                timHortonsAmount += currAmount;
                result.put(key, String.valueOf((int) (currAmount)));
            }
            else if (currTransaction.get("merchant_code").equals("subway")) {
                subwayAmount += currAmount;
                result.put(key, String.valueOf((int) (currAmount)));
            }
            else {
                otherTransactionsAmount += currAmount;
                result.put(key, String.valueOf((int) (currAmount)));
            }
        }
        // Calculate maximum rewards based on how much was spent at each merchant.
        // Loop to calculate points for Rule 1
        while (sportCheckAmount > 75 && timHortonsAmount > 25 && subwayAmount > 25) {
            rewardPoints += 500;
            sportCheckAmount -= 75;
            timHortonsAmount -= 25;
            subwayAmount -= 25;
        }
        // Loop to calculate points for Rule 2
        while (sportCheckAmount > 75 && timHortonsAmount > 25) {
            rewardPoints += 300;
            sportCheckAmount -= 75;
            timHortonsAmount -= 25;
        }
        // Loop to calculate points for Rule 4
        while (sportCheckAmount > 25 && timHortonsAmount > 10 && subwayAmount > 10) {
            rewardPoints += 150;
            sportCheckAmount -= 25;
            timHortonsAmount -= 10;
            subwayAmount -= 10;
        }
        // Loop to calculate points for Rule 6
        while (sportCheckAmount > 20) {
            rewardPoints += 75;
            sportCheckAmount -= 20;
        }
        // Add leftover to points, Rule 7
        int leftoverAmount = (int) ((otherTransactionsAmount + sportCheckAmount + timHortonsAmount + subwayAmount));
        rewardPoints += leftoverAmount;
        result.put("Total Rewards Points", Integer.toString(rewardPoints));

        return result;
    }
}

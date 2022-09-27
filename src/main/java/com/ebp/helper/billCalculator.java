package com.ebp.helper;

import org.springframework.stereotype.Component;

/**
 * @Author rohit.parihar 9/6/2022
 * @Class billCalculator
 * @Project Electricity Bill Payment
 */

@Component
public class billCalculator {
    public Integer forNON_INDUSTRIAL(Integer unitsConsumed, Integer pricePerUnit){
        Integer bill = (unitsConsumed * pricePerUnit) + 500;
        return bill;
    }

    public Integer forINDUSTRIAL(Integer unitsConsumed, Integer pricePerUnit){
        Integer bill = (unitsConsumed * pricePerUnit) + 1500;
        return bill;
    }

    public Integer forAGRICULTURAL(Integer unitsConsumed, Integer pricePerUnit){
        Integer bill = (unitsConsumed * pricePerUnit) + 20;
        return bill;
    }
}

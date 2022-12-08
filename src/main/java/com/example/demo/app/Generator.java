package com.example.demo.app;

import java.util.concurrent.atomic.AtomicInteger;

import static com.example.demo.app.Constants.*;
import static java.lang.Math.random;

public class Generator {
    private static final AtomicInteger number = new AtomicInteger();

    public static Request generate(int sourceNumber) {
        double trackLen = random() * AREA_SIZE;
        double costTransportation = MIN_COST_TRANSPORTATION + (trackLen * COST_PER_KM);
        double prepayment = MIN_PREPAYMENT + trackLen * COST_PER_KM;
        int numberOfDaysBeforePayment = (int) (Math.random() *
                (GENERATOR_MAX_COUNT_DAY - GENERATOR_MIN_COUNT_DAY + 1)) + GENERATOR_MIN_COUNT_DAY;
        double freight = Math.random() * (GENERATOR_MAX_INTEREST_FREIGHT - GENERATOR_MIN_INTEREST_FREIGHT + 1)
                + GENERATOR_MIN_INTEREST_FREIGHT;
        boolean areProblemsWithFreight = ((int) (Math.random() * 2)) != 1;

        PaymentType paymentType = switch ((int) (random() * 3)) {
            case 0 -> PaymentType.CASH;
            case 1 -> PaymentType.CASHLESS;
            default -> PaymentType.CASHLESS_WITH_VAT;
        };

        return new Request(number.getAndIncrement(), sourceNumber, numberOfDaysBeforePayment, costTransportation,
                prepayment, freight, areProblemsWithFreight, paymentType, System.currentTimeMillis());
    }
}

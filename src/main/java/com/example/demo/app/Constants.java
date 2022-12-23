package com.example.demo.app;

public class Constants {
    public static final int MILLISECONDS_PER_SECOND = 1_000;
    public static final int GENERATOR_MAX_COUNT_DAY = 20;
    public static final int GENERATOR_MIN_COUNT_DAY = 0;
    public static final int GENERATOR_MAX_INTEREST_FREIGHT = 10;
    public static final int GENERATOR_MIN_INTEREST_FREIGHT = 100;

    public static final double LAMBDA = 1;
    public static final double MIN_COST_TRANSPORTATION = 5_000;
    public static final double MIN_PREPAYMENT = 100;
    public static final double COST_PER_KM = 55;
    public static final double AREA_SIZE = 10_000;

    public static final long BETA = (long) (3.2 * MILLISECONDS_PER_SECOND);
    public static final long ALPHA = (long) (1.8 * MILLISECONDS_PER_SECOND);
}

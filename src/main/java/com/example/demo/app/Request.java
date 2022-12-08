package com.example.demo.app;

public class Request {
    private int number;
    private int sourceNumber;
    private int numberOfDaysBeforePayment; // количество дней до оплаты
    private double costTransportation; // стоимость перевозки
    private double prepayment; //  предоплата
    private double freight; // проценты за фрахт
    private PaymentType paymentType; // // тип оплаты
    private boolean problemsWithFreight; // есть проблемы с фрахтом
    private long arrivalTime; // время создания заявки

    public Request(int number, int sourceNumber, int numberOfDaysBeforePayment,
                   double costTransportation, double prepayment, double freight,
                   boolean areProblemsWithFreight, PaymentType paymentType, long arrivalTime) {
        this.number = number;
        this.sourceNumber = sourceNumber;
        this.numberOfDaysBeforePayment = numberOfDaysBeforePayment;
        this.costTransportation = costTransportation;
        this.prepayment = prepayment;
        this.freight = freight;
        this.problemsWithFreight = areProblemsWithFreight;
        this.paymentType = paymentType;
        this.arrivalTime = arrivalTime;
    }

    public double getCostTransportation() {
        return costTransportation;
    }

    public double getPrepayment() {
        return prepayment;
    }

    public int getNumber() {
        return number;
    }

    public int getSourceNumber() {
        return sourceNumber;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public int getNumberOfDaysBeforePayment() {
        return numberOfDaysBeforePayment;
    }

    public double getFreight() {
        return freight;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public boolean isProblemsWithFreight() {
        return problemsWithFreight;
    }
}

package ru.neoflex.learning.creaditpipeline.conveyor.dto;

import lombok.Value;

import java.math.BigDecimal;

//ToDo schema
@Value
public class LoanOfferDto {

    Long applicationId;

    BigDecimal requestedAmount;

    BigDecimal totalAmount;

    Integer term;

    BigDecimal monthlyPayment;

    BigDecimal rate;

    Boolean isInsuranceEnabled;

    Boolean isSalaryClient;
}

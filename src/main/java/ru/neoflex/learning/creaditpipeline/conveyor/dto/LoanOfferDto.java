package ru.neoflex.learning.creaditpipeline.conveyor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

//ToDo schema
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanOfferDto {

    Long applicationId;

    BigDecimal requestedAmount;

    BigDecimal totalAmount;

    Integer term;

    BigDecimal monthlyPayment;

    //ToDo fraction part
    BigDecimal rate;

    Boolean isInsuranceEnabled;

    Boolean isSalaryClient;
}

package ru.neoflex.learning.creaditpipeline.conveyor.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

//ToDo schema
@Value
public class CreditDto {

    BigDecimal amount;

    Integer term;

    BigDecimal monthlyPayment;

    BigDecimal rate;

    BigDecimal psk;

    Boolean isInsuranceEnabled;

    Boolean isSalaryClient;

    List<PaymentScheduleElement> paymentSchedule;
}

package ru.neoflex.learning.creaditpipeline.conveyor.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentScheduleElement {

    Integer number;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate date;

    BigDecimal totalPayment;

    BigDecimal interestPayment;

    BigDecimal debtPayment;

    BigDecimal remainingDebt;
}

package ru.neoflex.learning.creaditpipeline.conveyor.test.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.model.CreditDto;
import org.openapitools.model.LoanApplicationRequestDto;
import org.openapitools.model.LoanOfferDto;
import org.openapitools.model.PaymentScheduleElement;
import org.openapitools.model.ScoringDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import ru.neoflex.learning.creaditpipeline.conveyor.service.ConveyorService;
import ru.neoflex.learning.creaditpipeline.conveyor.test.IT;
import ru.neoflex.learning.creaditpipeline.conveyor.test.ModelHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.DEFAULT_SCALE;

@IT
class ConveyorServiceTest implements ModelHelper {

    @Autowired
    ConveyorService conveyorService;

    @Test
    @DisplayName("Проверка offers()")
    void offersTest() {

        final LoanApplicationRequestDto loanApplicationRequestDTO = getLoanApplicationRequestDto();

        final List<LoanOfferDto> result = conveyorService.offers(loanApplicationRequestDTO);


        assertNotNull(result);
        assertEquals(4, result.size());

        result.forEach(loanOfferDTO -> assertAll(
            () -> assertEquals(loanApplicationRequestDTO.getAmount(), loanOfferDTO.getRequestedAmount()),
            () -> assertEquals(loanApplicationRequestDTO.getTerm(), loanOfferDTO.getTerm())
        ));

        assertEquals(BigDecimal.valueOf(10293.78), result.get(0).getTotalAmount());
        assertEquals(BigDecimal.valueOf(1715.63), result.get(0).getMonthlyPayment());
        assertEquals(BigDecimal.valueOf(10.00).setScale(DEFAULT_SCALE, RoundingMode.HALF_UP), result.get(0).getRate());
        assertFalse(result.get(0).getIsInsuranceEnabled());
        assertFalse(result.get(0).getIsSalaryClient());

        assertEquals(BigDecimal.valueOf(10279.02), result.get(1).getTotalAmount());
        assertEquals(BigDecimal.valueOf(1713.17), result.get(1).getMonthlyPayment());
        assertEquals(BigDecimal.valueOf(9.50).setScale(DEFAULT_SCALE, RoundingMode.HALF_UP), result.get(1).getRate());
        assertFalse(result.get(1).getIsInsuranceEnabled());
        assertTrue(result.get(1).getIsSalaryClient());

        assertEquals(BigDecimal.valueOf(10414.20).setScale(DEFAULT_SCALE, RoundingMode.HALF_UP), result.get(2).getTotalAmount());
        assertEquals(BigDecimal.valueOf(1710.70).setScale(DEFAULT_SCALE, RoundingMode.HALF_UP), result.get(2).getMonthlyPayment());
        assertEquals(BigDecimal.valueOf(9.00).setScale(DEFAULT_SCALE, RoundingMode.HALF_UP), result.get(2).getRate());
        assertTrue(result.get(2).getIsInsuranceEnabled());
        assertFalse(result.get(2).getIsSalaryClient());

        assertEquals(BigDecimal.valueOf(10250.70).setScale(DEFAULT_SCALE, RoundingMode.HALF_UP), result.get(3).getTotalAmount());
        assertEquals(BigDecimal.valueOf(1708.45), result.get(3).getMonthlyPayment());
        assertEquals(BigDecimal.valueOf(8.55), result.get(3).getRate());
        assertTrue(result.get(3).getIsInsuranceEnabled());
        assertTrue(result.get(3).getIsSalaryClient());
    }

    @Test
    @DisplayName("Проверка calculation()")
    void calculationTest() {

        final ScoringDataDto scoringDataDTO = getScoringDataDto(getEmploymentDto());
        final CreditDto result = conveyorService.calculation(scoringDataDTO);

        assertNotNull(result);
        assertEquals(scoringDataDTO.getAmount(), result.getAmount());
        assertEquals(scoringDataDTO.getTerm(), result.getTerm());
        assertEquals(BigDecimal.valueOf(17156.33), result.getMonthlyPayment());
        assertEquals(BigDecimal.valueOf(10.00).setScale(DEFAULT_SCALE, RoundingMode.HALF_UP), result.getRate());
        assertEquals(BigDecimal.valueOf(102937.98), result.getPsk());
        assertFalse(result.getIsInsuranceEnabled());
        assertFalse(result.getIsSalaryClient());
        assertEquals(scoringDataDTO.getTerm() + 1, result.getPaymentSchedule().size());

        result.getPaymentSchedule().forEach(paymentScheduleElement -> assertAll(
            () -> assertNotNull(paymentScheduleElement.getNumber()),
            () -> assertNotNull(paymentScheduleElement.getDate()),
            () -> assertNotNull(paymentScheduleElement.getTotalPayment()),
            () -> assertNotNull(paymentScheduleElement.getInterestPayment()),
            () -> assertNotNull(paymentScheduleElement.getDebtPayment()),
            () -> assertNotNull(paymentScheduleElement.getRemainingDebt())
        ));
        final PaymentScheduleElement last = result.getPaymentSchedule().get(result.getPaymentSchedule().size() - 1);
        assertEquals(0, BigDecimal.ZERO.compareTo(last.getRemainingDebt()));
    }
}

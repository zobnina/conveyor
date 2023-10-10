package ru.neoflex.learning.creaditpipeline.conveyor.test.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.neoflex.learning.creaditpipeline.conveyor.service.CalculationService;
import ru.neoflex.learning.creaditpipeline.conveyor.test.IT;
import ru.neoflex.learning.creaditpipeline.conveyor.test.ModelHelper;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.DEFAULT_SCALE;

@IT
class CalculationServiceTest implements ModelHelper {

    @Autowired
    CalculationService calculationService;

    @Test
    @DisplayName("Проверка calcRate()")
    void calcRateTest() {

        assertEquals(BigDecimal.valueOf(8.55).setScale(DEFAULT_SCALE, HALF_UP), calculationService.calcRate(true, true));
        assertEquals(BigDecimal.valueOf(10).setScale(DEFAULT_SCALE, HALF_UP), calculationService.calcRate(false, false));
        assertEquals(BigDecimal.valueOf(9).setScale(DEFAULT_SCALE, HALF_UP), calculationService.calcRate(true, false));
        assertEquals(BigDecimal.valueOf(9.5).setScale(DEFAULT_SCALE, HALF_UP), calculationService.calcRate(false, true));
    }

    @Test
    @DisplayName("Проверка calcMonthPayment()")
    void calcMonthPaymentTest() {

        assertEquals(BigDecimal.valueOf(879.16).setScale(DEFAULT_SCALE, HALF_UP), calculationService.calcMonthPayment(BigDecimal.valueOf(10000), BigDecimal.TEN, 12));
    }

    @Test
    @DisplayName("Проверка calcPsk()")
    void calcPskTest() {

        assertEquals(BigDecimal.valueOf(10000).setScale(DEFAULT_SCALE, HALF_UP), calculationService.calcPsk(BigDecimal.valueOf(10000), BigDecimal.valueOf(1000), 10, false, false));
        assertEquals(BigDecimal.valueOf(10250).setScale(DEFAULT_SCALE, HALF_UP), calculationService.calcPsk(BigDecimal.valueOf(10000), BigDecimal.valueOf(1000), 10, true, false));
        assertEquals(BigDecimal.valueOf(10000).setScale(DEFAULT_SCALE, HALF_UP), calculationService.calcPsk(BigDecimal.valueOf(10000), BigDecimal.valueOf(1000), 10, false, true));
        assertEquals(BigDecimal.valueOf(10000).setScale(DEFAULT_SCALE, HALF_UP), calculationService.calcPsk(BigDecimal.valueOf(10000), BigDecimal.valueOf(1000), 10, true, true));
    }
}

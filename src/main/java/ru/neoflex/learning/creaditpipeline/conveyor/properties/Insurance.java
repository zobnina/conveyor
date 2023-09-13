package ru.neoflex.learning.creaditpipeline.conveyor.properties;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Insurance {

    /**
     * Стоимость страховки, процент от суммы кредита
     */
    private BigDecimal percent;

    /**
     * Стоимость страховки для зарплатного клиента, процент от суммы кредита
     */
    private BigDecimal salaryClientPercent;
}


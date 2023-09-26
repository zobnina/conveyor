package ru.neoflex.learning.creaditpipeline.conveyor.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Insurance {

    /**
     * Стоимость страховки, процент от суммы кредита
     */
    BigDecimal percent;

    /**
     * Стоимость страховки для зарплатного клиента, процент от суммы кредита
     */
    BigDecimal salaryClientPercent;
}


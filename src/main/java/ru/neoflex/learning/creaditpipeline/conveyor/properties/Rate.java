package ru.neoflex.learning.creaditpipeline.conveyor.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Rate {

    /**
     * Базовая ставка
     */
    BigDecimal base;

    /**
     * Цена страховки, уменьшает ставку на
     */
    BigDecimal insurance;

    /**
     * Цена зарплатного клиента, уменьшает ставку на
     */
    BigDecimal salaryClient;
}
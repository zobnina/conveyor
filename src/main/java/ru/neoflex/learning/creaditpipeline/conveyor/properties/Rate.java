package ru.neoflex.learning.creaditpipeline.conveyor.properties;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Rate {

    /**
     * Базовая ставка
     */
    private BigDecimal base;

    /**
     * Цена страховки, уменьшает ставку на
     */
    private BigDecimal insurance;

    /**
     * Цена зарплатного клиента, уменьшает ставку на
     */
    private BigDecimal salaryClient;
}
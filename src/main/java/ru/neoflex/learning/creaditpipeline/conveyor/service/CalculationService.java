package ru.neoflex.learning.creaditpipeline.conveyor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.learning.creaditpipeline.conveyor.properties.ConveyorProperties;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;
import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.DEFAULT_SCALE;
import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.HUNDRED;
import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.MATH_CONTEXT_5;
import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.MONTHS_PER_YEAR;

@Service
@RequiredArgsConstructor
public class CalculationService {

    private final ConveyorProperties properties;

    public BigDecimal calcRate(boolean isInsuranceEnabled, boolean isSalaryClient) {

        BigDecimal rate = properties.getRate().getBase();
        rate = calcByInsurance(rate, isInsuranceEnabled);
        rate = calcBySalaryClient(rate, isSalaryClient);

        return rate.setScale(DEFAULT_SCALE, HALF_UP);
    }

    public BigDecimal calcMonthPayment(BigDecimal amount, BigDecimal rate, Integer term) {

        final BigDecimal pm = rateMonth(rate);
        final BigDecimal onePlusPm = BigDecimal.ONE.add(pm);
        final BigDecimal onePlusPmPow = onePlusPm.pow(term);
        final BigDecimal powMinusOne = onePlusPmPow.subtract(BigDecimal.ONE);
        final BigDecimal pmDiv = pm.divide(powMinusOne, MATH_CONTEXT_5);
        final BigDecimal pmPlusFraction = pm.add(pmDiv);

        return amount.multiply(pmPlusFraction).setScale(DEFAULT_SCALE, HALF_UP);
    }

    public BigDecimal calcPsk(BigDecimal amount, BigDecimal monthlyPayment, Integer term,
                              boolean isInsuranceEnabled, boolean isSalaryClient) {

        final BigDecimal psk = monthlyPayment.multiply(BigDecimal.valueOf(term));
        if (isInsuranceEnabled) {
            double yearCount = (double) term / MONTHS_PER_YEAR;
            BigDecimal insuranceCostTotal = insuranceCost(amount, isSalaryClient)
                .multiply(BigDecimal.valueOf(yearCount));

            return psk.add(insuranceCostTotal).setScale(DEFAULT_SCALE, HALF_UP);
        }

        return psk.setScale(DEFAULT_SCALE, HALF_UP);
    }

    public BigDecimal calcInterestPayment(BigDecimal remainingDebt, BigDecimal rate) {

        return remainingDebt.multiply(rateMonth(rate)).setScale(DEFAULT_SCALE, HALF_UP);
    }

    public BigDecimal calcTotalPayment(BigDecimal remainingDebt, BigDecimal monthlyPayment, BigDecimal interestPayment) {

        if (remainingDebt.compareTo(monthlyPayment) > 0) {
            return monthlyPayment;
        }
        return remainingDebt.add(interestPayment).setScale(DEFAULT_SCALE, HALF_UP);
    }

    private BigDecimal calcBySalaryClient(BigDecimal rate, boolean isSalaryClient) {

        if (isSalaryClient) {
            return rate.multiply(HUNDRED.subtract(properties.getRate().getSalaryClient()).divide(HUNDRED, MATH_CONTEXT_5));
        }
        return rate;
    }

    private BigDecimal calcByInsurance(BigDecimal rate, boolean isInsuranceEnabled) {

        if (isInsuranceEnabled) {
            return rate.multiply(HUNDRED.subtract(properties.getRate().getInsurance()).divide(HUNDRED, MATH_CONTEXT_5));
        }
        return rate;
    }

    private BigDecimal rateMonth(BigDecimal rate) {
        return rate.divide(HUNDRED, MATH_CONTEXT_5).divide(BigDecimal.valueOf(MONTHS_PER_YEAR), MATH_CONTEXT_5);
    }

    private BigDecimal insuranceCost(BigDecimal amount, boolean isSalaryClient) {

        if (isSalaryClient) {
            return amount.multiply(properties.getInsurance().getSalaryClientPercent()).divide(HUNDRED, MATH_CONTEXT_5);
        }
        return amount.multiply(properties.getInsurance().getPercent()).divide(HUNDRED, MATH_CONTEXT_5);
    }
}

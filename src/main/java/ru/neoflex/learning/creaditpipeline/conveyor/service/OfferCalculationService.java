package ru.neoflex.learning.creaditpipeline.conveyor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.learning.creaditpipeline.conveyor.properties.ConveyorProperties;

import java.math.BigDecimal;

import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.HUNDRED;
import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.MATH_CONTEXT_5;
import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.MONTHS_PER_YEAR;

@Service
@RequiredArgsConstructor
public class OfferCalculationService {

    private final ConveyorProperties properties;

    public BigDecimal calcRate(boolean isInsuranceEnabled, boolean isSalaryClient) {

        BigDecimal rate = properties.getRate().getBase();
        rate = calcByInsurance(rate, isInsuranceEnabled);
        rate = calcBySalaryClient(rate, isSalaryClient);

        return rate;
    }

    public BigDecimal calcMonthPayment(BigDecimal amount, BigDecimal rate, Integer term) {

        final BigDecimal pm = rateMonth(rate);
        final BigDecimal onePlusPm = BigDecimal.ONE.add(pm);
        final BigDecimal onePlusPmPow = onePlusPm.pow(term);
        final BigDecimal powMinusOne = onePlusPmPow.subtract(BigDecimal.ONE);
        final BigDecimal pmDiv = pm.divide(powMinusOne, MATH_CONTEXT_5);
        final BigDecimal pmPlusFraction = pm.add(pmDiv);

        return amount.multiply(pmPlusFraction);
    }

    public BigDecimal calcPsk(BigDecimal amount, BigDecimal monthlyPayment, Integer term,
                              boolean isInsuranceEnabled, boolean isSalaryClient) {

        final BigDecimal psk = monthlyPayment.multiply(BigDecimal.valueOf(term));
        if (Boolean.TRUE.equals(isInsuranceEnabled)) {
            double yearCount = (double) term / MONTHS_PER_YEAR;
            BigDecimal insuranceCostTotal = insuranceCost(amount, isSalaryClient)
                .multiply(BigDecimal.valueOf(yearCount));

            return psk.add(insuranceCostTotal);
        }

        return psk;
    }

    private BigDecimal calcBySalaryClient(BigDecimal rate, boolean isSalaryClient) {

        if (Boolean.TRUE.equals(isSalaryClient)) {
            return rate.multiply(HUNDRED.subtract(properties.getRate().getSalaryClient()).divide(HUNDRED, MATH_CONTEXT_5));
        }
        return rate;
    }

    private BigDecimal calcByInsurance(BigDecimal rate, boolean isInsuranceEnabled) {

        if (Boolean.TRUE.equals(isInsuranceEnabled)) {
            return rate.multiply(HUNDRED.subtract(properties.getRate().getInsurance()).divide(HUNDRED, MATH_CONTEXT_5));
        }
        return rate;
    }

    private BigDecimal rateMonth(BigDecimal rate) {
        return rate.divide(HUNDRED, MATH_CONTEXT_5).divide(BigDecimal.valueOf(MONTHS_PER_YEAR), MATH_CONTEXT_5);
    }

    private BigDecimal insuranceCost(BigDecimal amount, boolean isSalaryClient) {

        if (Boolean.TRUE.equals(isSalaryClient)) {
            return amount.multiply(properties.getInsurance().getSalaryClientPercent()).divide(HUNDRED, MATH_CONTEXT_5);
        }
        return amount.multiply(properties.getInsurance().getPercent()).divide(HUNDRED, MATH_CONTEXT_5);
    }
}

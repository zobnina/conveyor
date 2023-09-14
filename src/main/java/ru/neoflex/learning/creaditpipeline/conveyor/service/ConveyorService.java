package ru.neoflex.learning.creaditpipeline.conveyor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.CreditDto;
import org.openapitools.model.LoanApplicationRequestDto;
import org.openapitools.model.LoanOfferDto;
import org.openapitools.model.ScoringDataDto;
import org.springframework.stereotype.Service;
import ru.neoflex.learning.creaditpipeline.conveyor.exception.ExceptionCode;
import ru.neoflex.learning.creaditpipeline.conveyor.exception.PrescoringException;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.apache.commons.lang3.BooleanUtils.isNotTrue;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConveyorService {

    private final ScoringService scoringService;
    private final OfferCalculationService offerCalculationService;

    /**
     * Расчёт возможных условий кредита:
     * На основании LoanApplicationRequestDTO происходит прескоринг,
     * создаётся 4 кредитных предложения LoanOfferDTO на основании всех возможных комбинаций булевских полей
     * isInsuranceEnabled и isSalaryClient (false-false, false-true, true-false, true-true)
     *
     * @param request - {@link LoanApplicationRequestDto параметры запроса}
     * @return - список {@link LoanOfferDto кредитных предложений}
     */
    public List<LoanOfferDto> offers(LoanApplicationRequestDto request) {

        if (isNotTrue(scoringService.validAge(request.getBirthdate()))) {
            throw new PrescoringException(ExceptionCode.AGE_NOT_VALID.getMessage());
        }
        return Stream.of(
                createLoanOffer(request, false, false),
                createLoanOffer(request, false, true),
                createLoanOffer(request, true, false),
                createLoanOffer(request, true, true))
            .sorted(Comparator.comparing(LoanOfferDto::getRate).reversed())
            .toList();
    }

    /**
     * Происходит скоринг данных, высчитывание ставки(rate), полной стоимости кредита(psk),
     * размер ежемесячного платежа(monthlyPayment), график ежемесячных платежей
     *
     * @param request - {@link ScoringDataDto - параметры запроса}
     * @return - {@link CreditDto параметры кредита}
     */
    public CreditDto calculation(ScoringDataDto request) {
        //ToDo implementation
        return null;
    }

    private LoanOfferDto createLoanOffer(LoanApplicationRequestDto request,
                                         boolean isInsuranceEnabled,
                                         boolean isSalaryClient) {

        final BigDecimal rate = offerCalculationService.calcRate(isInsuranceEnabled, isSalaryClient);
        log.debug("createLoanOffer(): rate = {}", rate);
        final BigDecimal monthlyPayment = offerCalculationService.calcMonthPayment(request.getAmount(), rate, request.getTerm());
        log.debug("createLoanOffer(): monthlyPayment = {}", monthlyPayment);
        final BigDecimal totalAmount = offerCalculationService.calcPsk(
            request.getAmount(), monthlyPayment, request.getTerm(), isInsuranceEnabled, isSalaryClient);
        log.debug("createLoanOffer(): totalAmount = {}", totalAmount);

        return LoanOfferDto.builder()
            .isInsuranceEnabled(isInsuranceEnabled)
            .isSalaryClient(isSalaryClient)
            .requestedAmount(request.getAmount())
            .term(request.getTerm())
            .rate(rate)
            .monthlyPayment(monthlyPayment)
            .totalAmount(totalAmount)
            .build();
    }
}

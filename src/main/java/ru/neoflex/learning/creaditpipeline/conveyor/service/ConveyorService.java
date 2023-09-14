package ru.neoflex.learning.creaditpipeline.conveyor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.CreditDto;
import org.openapitools.model.LoanApplicationRequestDto;
import org.openapitools.model.LoanOfferDto;
import org.openapitools.model.PaymentScheduleElement;
import org.openapitools.model.ScoringDataDto;
import org.springframework.stereotype.Service;
import ru.neoflex.learning.creaditpipeline.conveyor.exception.ExceptionCode;
import ru.neoflex.learning.creaditpipeline.conveyor.exception.PrescoringException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.apache.commons.lang3.BooleanUtils.isNotTrue;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConveyorService {

    private final ScoringService scoringService;
    private final CalculationService calculationService;

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

        final BigDecimal rate = calculationService.calcRate(
            request.getIsInsuranceEnabled(), request.getIsSalaryClient());
        log.debug("calculation(): rate = {}", rate);
        final BigDecimal scoring = scoringService.scoring(rate, request);
        log.debug("calculation(): scoring = {}", scoring);
        final BigDecimal monthlyPayment = calculationService.calcMonthPayment(request.getAmount(), scoring, request.getTerm());
        log.debug("calculation(): monthlyPayment = {}", monthlyPayment);
        final BigDecimal psk = calculationService.calcPsk(request.getAmount(), monthlyPayment, request.getTerm(),
            request.getIsInsuranceEnabled(), request.getIsSalaryClient());
        log.debug("calculation(): psk = {}", psk);
        final List<PaymentScheduleElement> paymentSchedule = createPaymentSchedule(request.getAmount(),
            request.getTerm(), scoring, monthlyPayment);
        log.debug("calculation(): paymentSchedule = {}", paymentSchedule);

        return CreditDto.builder()
            .rate(scoring)
            .monthlyPayment(monthlyPayment)
            .psk(psk)
            .term(request.getTerm())
            .amount(request.getAmount())
            .isInsuranceEnabled(request.getIsInsuranceEnabled())
            .isSalaryClient(request.getIsSalaryClient())
            .paymentSchedule(paymentSchedule)
            .build();
    }

    private LoanOfferDto createLoanOffer(LoanApplicationRequestDto request,
                                         boolean isInsuranceEnabled,
                                         boolean isSalaryClient) {

        final BigDecimal rate = calculationService.calcRate(isInsuranceEnabled, isSalaryClient);
        log.debug("createLoanOffer(): rate = {}", rate);
        final BigDecimal monthlyPayment = calculationService.calcMonthPayment(request.getAmount(), rate, request.getTerm());
        log.debug("createLoanOffer(): monthlyPayment = {}", monthlyPayment);
        final BigDecimal totalAmount = calculationService.calcPsk(
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

    private List<PaymentScheduleElement> createPaymentSchedule(BigDecimal amount, Integer term, BigDecimal rate, BigDecimal monthlyPayment) {

        final List<PaymentScheduleElement> paymentScheduleElements = new ArrayList<>(term);
        paymentScheduleElements.add(zeroPayment(amount));

        IntStream.range(1, term + 1)
            .mapToObj(i -> Map.entry(i, paymentScheduleElements.get(i - 1)))
            .forEach(entry -> {
                PaymentScheduleElement previous = entry.getValue();
                final LocalDate date = previous.getDate().plusMonths(1);
                final BigDecimal interestPayment = calculationService.calcInterestPayment(previous.getRemainingDebt(), rate);
                final BigDecimal totalPayment = calculationService.calcTotalPayment(previous.getRemainingDebt(), monthlyPayment, interestPayment);
                final BigDecimal debtPayment = totalPayment.subtract(interestPayment);
                final BigDecimal remainingDebt = previous.getRemainingDebt().subtract(debtPayment);

                paymentScheduleElements.add(PaymentScheduleElement.builder()
                    .number(entry.getKey())
                    .date(date)
                    .interestPayment(interestPayment)
                    .totalPayment(totalPayment)
                    .debtPayment(debtPayment)
                    .remainingDebt(remainingDebt)
                    .build());
            });

        return paymentScheduleElements;
    }

    private PaymentScheduleElement zeroPayment(BigDecimal amount) {

        return PaymentScheduleElement.builder()
            .number(0)
            .date(LocalDate.now())
            .totalPayment(BigDecimal.ZERO)
            .debtPayment(BigDecimal.ZERO)
            .interestPayment(BigDecimal.ZERO)
            .remainingDebt(amount)
            .build();
    }
}

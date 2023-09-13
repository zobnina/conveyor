package ru.neoflex.learning.creaditpipeline.conveyor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.learning.creaditpipeline.conveyor.dto.CreditDto;
import ru.neoflex.learning.creaditpipeline.conveyor.dto.LoanApplicationRequestDto;
import ru.neoflex.learning.creaditpipeline.conveyor.dto.LoanOfferDto;
import ru.neoflex.learning.creaditpipeline.conveyor.dto.ScoringDataDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConveyorService {

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
        //ToDo implementation
        return null;
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
}

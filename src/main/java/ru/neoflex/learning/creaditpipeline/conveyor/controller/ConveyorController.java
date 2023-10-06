package ru.neoflex.learning.creaditpipeline.conveyor.controller;

import lombok.RequiredArgsConstructor;
import org.openapitools.api.ConveyorApi;
import org.openapitools.model.CreditDto;
import org.openapitools.model.LoanApplicationRequestDto;
import org.openapitools.model.LoanOfferDto;
import org.openapitools.model.ScoringDataDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.learning.creaditpipeline.conveyor.service.ConveyorService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class ConveyorController implements ConveyorApi {

    private final ConveyorService conveyorService;

    @Override
    public ResponseEntity<CreditDto> conveyorCalculation(ScoringDataDto scoringDataDto) {
        return ResponseEntity.ok(conveyorService.calculation(scoringDataDto));
    }

    @Override
    public ResponseEntity<List<LoanOfferDto>> conveyorOffers(LoanApplicationRequestDto loanApplicationRequestDto) {
        return ResponseEntity.ok(conveyorService.offers(loanApplicationRequestDto));
    }
}

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

    //ToDo doc
    public List<LoanOfferDto> offers(LoanApplicationRequestDto request) {
        //ToDo implementation
        return null;
    }

    //ToDo doc
    public CreditDto calculation(ScoringDataDto request) {
        //ToDo implementation
        return null;
    }
}

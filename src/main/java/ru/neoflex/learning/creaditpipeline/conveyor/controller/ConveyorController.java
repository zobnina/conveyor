package ru.neoflex.learning.creaditpipeline.conveyor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.learning.creaditpipeline.conveyor.dto.CreditDto;
import ru.neoflex.learning.creaditpipeline.conveyor.dto.LoanApplicationRequestDto;
import ru.neoflex.learning.creaditpipeline.conveyor.dto.LoanOfferDto;
import ru.neoflex.learning.creaditpipeline.conveyor.dto.ScoringDataDto;
import ru.neoflex.learning.creaditpipeline.conveyor.service.ConveyorService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/conveyor")
@Tag(name = "Контроллер для работы кредитного конвейера")
@RequiredArgsConstructor
public class ConveyorController {

    private final ConveyorService conveyorService;

    @Operation(
        summary = "Расчёт возможных условий кредита",
        description = "Расчёт возможных условий кредита",
        operationId = "conveyor_offers",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = LoanOfferDto.class))))
        }
    )
    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> offers(@RequestBody @Valid LoanApplicationRequestDto request) {

        return ResponseEntity.ok(conveyorService.offers(request));
    }

    @Operation(
        summary = "Скоринг данных + полный расчет параметров кредита",
        description = "Скоринг данных + полный расчет параметров кредита",
        operationId = "conveyor_calculation",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK",
                content = @Content(schema = @Schema(implementation = CreditDto.class)))
        }
    )
    @PostMapping("/calculation")
    public ResponseEntity<CreditDto> calculation(@RequestBody @Valid ScoringDataDto request) {
        return ResponseEntity.ok(conveyorService.calculation(request));
    }
}

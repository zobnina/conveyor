package ru.neoflex.learning.creaditpipeline.conveyor.test.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.model.EmploymentStatus;
import org.openapitools.model.Gender;
import org.openapitools.model.MaritalStatus;
import org.openapitools.model.Position;
import org.openapitools.model.ScoringDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import ru.neoflex.learning.creaditpipeline.conveyor.service.ScoringService;
import ru.neoflex.learning.creaditpipeline.conveyor.test.IT;
import ru.neoflex.learning.creaditpipeline.conveyor.test.ModelHelper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
class ScoringServiceTest implements ModelHelper {

    @Autowired
    ScoringService scoringService;

    @Test
    @DisplayName("Проверка validAge")
    void validAgeTest() {

        assertTrue(scoringService.validAge(LocalDate.of(2000, 1, 1)));
        assertFalse(scoringService.validAge(LocalDate.now()));
        assertFalse(scoringService.validAge(LocalDate.of(2100, 1, 1)));
    }

    @Test
    @DisplayName("Проверка scoring")
    void scoringTest() {

        final BigDecimal rate = BigDecimal.TEN;
        ScoringDataDto scoringDataDto = getScoringDataDto(getEmploymentDto().employmentStatus(EmploymentStatus.SELF_EMPLOYED));
        assertEquals(rate.add(BigDecimal.ONE), scoringService.scoring(rate, scoringDataDto));

        scoringDataDto = getScoringDataDto(getEmploymentDto().employmentStatus(EmploymentStatus.BUSINESS_OWNER));
        assertEquals(rate.add(BigDecimal.valueOf(3)), scoringService.scoring(rate, scoringDataDto));

        scoringDataDto = getScoringDataDto(getEmploymentDto().position(Position.MID_MANAGER));
        assertEquals(rate.subtract(BigDecimal.valueOf(2)), scoringService.scoring(rate, scoringDataDto));

        scoringDataDto = getScoringDataDto(getEmploymentDto().position(Position.TOP_MANAGER));
        assertEquals(rate.subtract(BigDecimal.valueOf(4)), scoringService.scoring(rate, scoringDataDto));

        scoringDataDto = getScoringDataDto(getEmploymentDto()).maritalStatus(MaritalStatus.MARRIED);
        assertEquals(rate.subtract(BigDecimal.valueOf(3)), scoringService.scoring(rate, scoringDataDto));

        scoringDataDto = getScoringDataDto(getEmploymentDto()).maritalStatus(MaritalStatus.DIVORCED);
        assertEquals(rate.add(BigDecimal.ONE), scoringService.scoring(rate, scoringDataDto));

        scoringDataDto = getScoringDataDto(getEmploymentDto()).dependentAmount(2);
        assertEquals(rate.add(BigDecimal.ONE), scoringService.scoring(rate, scoringDataDto));

        scoringDataDto = getScoringDataDto(getEmploymentDto()).gender(Gender.FEMALE).birthdate(LocalDate.now().minusYears(40));
        assertEquals(rate.subtract(BigDecimal.valueOf(3)), scoringService.scoring(rate, scoringDataDto));

        scoringDataDto = getScoringDataDto(getEmploymentDto()).gender(Gender.MALE).birthdate(LocalDate.now().minusYears(40));
        assertEquals(rate.subtract(BigDecimal.valueOf(3)), scoringService.scoring(rate, scoringDataDto));

        scoringDataDto = getScoringDataDto(getEmploymentDto()).gender(Gender.NON_BINARY);
        assertEquals(rate.add(BigDecimal.valueOf(3)), scoringService.scoring(rate, scoringDataDto));
    }
}

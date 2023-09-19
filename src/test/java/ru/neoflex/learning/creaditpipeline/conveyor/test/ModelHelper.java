package ru.neoflex.learning.creaditpipeline.conveyor.test;

import org.openapitools.model.EmploymentDto;
import org.openapitools.model.LoanApplicationRequestDto;
import org.openapitools.model.ScoringDataDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ModelHelper {

    default LoanApplicationRequestDto getLoanApplicationRequestDto() {

        return LoanApplicationRequestDto.builder()
            .amount(BigDecimal.valueOf(10000))
            .term(6)
            .firstName("Ivan")
            .lastName("Ivanov")
            .passportSeries("0000")
            .passportNumber("000000")
            .email("email@domain.com")
            .birthdate(LocalDate.of(2000, 1, 1))
            .build();
    }

    default ScoringDataDto getScoringDataDto(EmploymentDto employmentDto) {
        return ScoringDataDto.builder()
            .amount(BigDecimal.valueOf(100000))
            .term(6)
            .firstName("Ivan")
            .lastName("Ivanov")
            .gender(ScoringDataDto.GenderEnum.MALE)
            .birthdate(LocalDate.of(2000, 1, 1))
            .passportSeries("0000")
            .passportNumber("000000")
            .maritalStatus(ScoringDataDto.MaritalStatusEnum.SINGLE)
            .dependentAmount(0)
            .employment(employmentDto)
            .account("12345678900987654321")
            .isInsuranceEnabled(false)
            .isSalaryClient(false)
            .build();
    }

    default EmploymentDto getEmploymentDto() {
        return EmploymentDto.builder()
            .employmentStatus(EmploymentDto.EmploymentStatusEnum.EMPLOYED)
            .employerInn("1234567890")
            .salary(BigDecimal.valueOf(10000))
            .position(EmploymentDto.PositionEnum.WORKER)
            .workExperienceTotal(13)
            .workExperienceCurrent(4)
            .build();
    }
}

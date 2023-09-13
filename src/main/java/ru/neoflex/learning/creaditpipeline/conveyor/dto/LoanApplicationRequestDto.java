package ru.neoflex.learning.creaditpipeline.conveyor.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

//ToDo schema
//ToDo validation
@Value
public class LoanApplicationRequestDto {

    BigDecimal amount;

    Integer term;

    String firstName;

    String lastName;

    String middleName;

    @Email
    String email;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate birthdate;

    String passportSeries;

    String passportNumber;
}

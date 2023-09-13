package ru.neoflex.learning.creaditpipeline.conveyor.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;
import ru.neoflex.learning.creaditpipeline.conveyor.dictionary.Gender;
import ru.neoflex.learning.creaditpipeline.conveyor.dictionary.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

//ToDo schema
//ToDo validation
//ToDo add no args constructor
@Value
public class ScoringDataDto {

    BigDecimal amount;

    Integer term;

    String firstName;

    String lastName;

    String middleName;

    Gender gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate birthdate;

    String passportSeries;

    String passportNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate passportIssueDate;

    String passportIssueBranch;

    MaritalStatus maritalStatus;

    Integer dependentAmount;

    EmploymentDto employment;

    String account;

    Boolean isInsuranceEnabled;

    Boolean isSalaryClient;
}

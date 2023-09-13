package ru.neoflex.learning.creaditpipeline.conveyor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.neoflex.learning.creaditpipeline.conveyor.dictionary.EmploymentStatus;
import ru.neoflex.learning.creaditpipeline.conveyor.dictionary.Position;

import java.math.BigDecimal;

public class EmploymentDto {

    EmploymentStatus employmentStatus;

    @JsonProperty(value = "employerINN")
    String employerInn;

    BigDecimal salary;

    Position position;

    Integer workExperienceTotal;

    Integer workExperienceCurrent;
}

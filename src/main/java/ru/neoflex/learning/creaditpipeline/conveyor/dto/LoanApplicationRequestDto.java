package ru.neoflex.learning.creaditpipeline.conveyor.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;
import ru.neoflex.learning.creaditpipeline.conveyor.validation.AdultBirthDate;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Параметры запроса")
@Value
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class LoanApplicationRequestDto {

    @Schema(description = "Сумма кредита", minimum = "10000", defaultValue = "10000")
    @NotNull
    @DecimalMin(value = "10000")
    BigDecimal amount;

    @Schema(description = "Срок кредита", minimum = "6", defaultValue = "6")
    @NotNull
    @Min(value = 6)
    Integer term;

    @Schema(description = "Имя", minLength = 2, maxLength = 30, defaultValue = "Ivanov")
    @NotBlank
    @Pattern(regexp = "[A-Za-zЁёА-я]{2,30}")
    String firstName;

    @Schema(description = "Фамилия", minLength = 2, maxLength = 30, defaultValue = "Ivan")
    @NotBlank
    @Pattern(regexp = "[A-Za-zЁёА-я]{2,30}")
    String lastName;

    @Schema(description = "Отчество", minLength = 2, maxLength = 30, defaultValue = "Ivanovich", nullable = true)
    @Pattern(regexp = "[A-Za-zЁёА-я]{2,30}")
    String middleName;

    @Schema(description = "Email", defaultValue = "mail@domain.com")
    @NotBlank
    @Email
    String email;

    @Schema(description = "Дата рождения", defaultValue = "2000-01-01")
    @NotNull
    @AdultBirthDate
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate birthdate;

    @Schema(description = "Серия паспорта", pattern = "\\d{4}", defaultValue = "0000")
    @NotBlank
    @Pattern(regexp = "\\d{4}")
    String passportSeries;

    @Schema(description = "Номер паспорта", pattern = "\\d{4}", defaultValue = "000000")
    @NotBlank
    @Pattern(regexp = "\\d{6}")
    String passportNumber;
}

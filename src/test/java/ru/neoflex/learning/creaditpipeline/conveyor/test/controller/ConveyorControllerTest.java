package ru.neoflex.learning.creaditpipeline.conveyor.test.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.SneakyThrows;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.model.EmploymentDto;
import org.openapitools.model.LoanApplicationRequestDto;
import org.openapitools.model.ScoringDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.neoflex.learning.creaditpipeline.conveyor.exception.ExceptionCode;
import ru.neoflex.learning.creaditpipeline.conveyor.exception.PrescoringException;
import ru.neoflex.learning.creaditpipeline.conveyor.exception.ScoringException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ConveyorControllerTest {

    private static final String CONVEYOR_OFFERS_URL = "/conveyor/offers";
    private static final String CONVEYOR_CALCULATION_URL = "/conveyor/calculation";
    private static final LocalDate NOW = LocalDate.now();
    private static final JsonMapper JSON_MAPPER = JsonMapper.builder().findAndAddModules().build();

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("POST /conveyor/offers - 400 - error prescoring fio")
    @SneakyThrows
    void testPrescoringFio() {

        LoanApplicationRequestDto requestDto = getLoanApplicationRequestDto().firstName("a");
        byte[] content = JSON_MAPPER.writeValueAsBytes(requestDto);
        checkBadRequest(content);

        requestDto = getLoanApplicationRequestDto().firstName("1234567890");
        content = JSON_MAPPER.writeValueAsBytes(requestDto);
        checkBadRequest(content);

        requestDto = getLoanApplicationRequestDto().firstName(null);
        content = JSON_MAPPER.writeValueAsBytes(requestDto);
        checkBadRequest(content);

        requestDto = getLoanApplicationRequestDto().firstName("qwertyuioplkjhgfdsazxcvbnmqwertyuioplkjhgfdsazxcvbnm");
        content = JSON_MAPPER.writeValueAsBytes(requestDto);
        checkBadRequest(content);

        requestDto = getLoanApplicationRequestDto().lastName("a");
        content = JSON_MAPPER.writeValueAsBytes(requestDto);
        checkBadRequest(content);

        requestDto = getLoanApplicationRequestDto().lastName("1234567890");
        content = JSON_MAPPER.writeValueAsBytes(requestDto);
        checkBadRequest(content);

        requestDto = getLoanApplicationRequestDto().lastName(null);
        content = JSON_MAPPER.writeValueAsBytes(requestDto);
        checkBadRequest(content);

        requestDto = getLoanApplicationRequestDto().lastName("qwertyuioplkjhgfdsazxcvbnmqwertyuioplkjhgfdsazxcvbnm");
        content = JSON_MAPPER.writeValueAsBytes(requestDto);
        checkBadRequest(content);
    }

    @Test
    @DisplayName("POST /conveyor/offers - 400 - error prescoring amount")
    @SneakyThrows
    void testPrescoringAmount() {

        LoanApplicationRequestDto requestDto = getLoanApplicationRequestDto().amount(BigDecimal.TEN);
        byte[] content = JSON_MAPPER.writeValueAsBytes(requestDto);
        checkBadRequest(content);

        requestDto = getLoanApplicationRequestDto().amount(null);
        content = JSON_MAPPER.writeValueAsBytes(requestDto);
        checkBadRequest(content);
    }

    @Test
    @DisplayName("POST /conveyor/offers - 400 - error prescoring term")
    @SneakyThrows
    void testPrescoringTerm() {

        LoanApplicationRequestDto requestDto = getLoanApplicationRequestDto().term(5);
        byte[] content = JSON_MAPPER.writeValueAsBytes(requestDto);
        checkBadRequest(content);

        requestDto = getLoanApplicationRequestDto().term(null);
        content = JSON_MAPPER.writeValueAsBytes(requestDto);
        checkBadRequest(content);
    }

    @Test
    @DisplayName("POST /conveyor/offers - 400 - error prescoring email")
    @SneakyThrows
    void testPrescoringEmail() {

        LoanApplicationRequestDto requestDto = getLoanApplicationRequestDto().email("mail");
        byte[] content = JSON_MAPPER.writeValueAsBytes(requestDto);
        checkBadRequest(content);

        requestDto = getLoanApplicationRequestDto().email(null);
        content = JSON_MAPPER.writeValueAsBytes(requestDto);
        checkBadRequest(content);
    }

    @Test
    @DisplayName("POST /conveyor/offers - 400 - error prescoring passport")
    @SneakyThrows
    void testPrescoringPassport() {

        LoanApplicationRequestDto requestDto = getLoanApplicationRequestDto().passportNumber("abc");
        byte[] content = JSON_MAPPER.writeValueAsBytes(requestDto);
        checkBadRequest(content);

        requestDto = getLoanApplicationRequestDto().passportNumber("1234567");
        content = JSON_MAPPER.writeValueAsBytes(requestDto);
        checkBadRequest(content);

        requestDto = getLoanApplicationRequestDto().passportNumber(null);
        content = JSON_MAPPER.writeValueAsBytes(requestDto);
        checkBadRequest(content);

        requestDto = getLoanApplicationRequestDto().passportSeries("abc");
        content = JSON_MAPPER.writeValueAsBytes(requestDto);
        checkBadRequest(content);
        requestDto = getLoanApplicationRequestDto().passportSeries("1234567");
        content = JSON_MAPPER.writeValueAsBytes(requestDto);
        checkBadRequest(content);
        requestDto = getLoanApplicationRequestDto().passportSeries(null);
        content = JSON_MAPPER.writeValueAsBytes(requestDto);
        checkBadRequest(content);
    }

    @Test
    @DisplayName("POST /conveyor/offers - 422 - error prescoring age")
    @SneakyThrows
    void testPrescoringAge() {

        final LoanApplicationRequestDto content = getLoanApplicationRequestDto().birthdate(NOW);
        checkUnprocessableEntity(JSON_MAPPER.writeValueAsBytes(content), PrescoringException.class, CONVEYOR_OFFERS_URL, ExceptionCode.AGE_NOT_VALID);
    }

    @Test
    @DisplayName("POST /conveyor/offers - 200 - OK")
    @SneakyThrows
    void testOffers() {

        final LoanApplicationRequestDto content = getLoanApplicationRequestDto();
        mockMvc.perform(post(CONVEYOR_OFFERS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_MAPPER.writeValueAsBytes(content)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    @DisplayName("POST /conveyor/calculation")
    @SneakyThrows
    void testScoringUnemployed() {

        final ScoringDataDto request = getScoringDataDto(getEmploymentDto().employmentStatus(EmploymentDto.EmploymentStatusEnum.UNEMPLOYED));
        checkUnprocessableEntity(JSON_MAPPER.writeValueAsBytes(request), ScoringException.class, CONVEYOR_CALCULATION_URL, ExceptionCode.UNEMPLOYED_STATUS);
    }

    @Test
    @DisplayName("POST /conveyor/calculation")
    @SneakyThrows
    void testScoringAmount() {

        final ScoringDataDto request = getScoringDataDto(getEmploymentDto()).amount(BigDecimal.valueOf(1000000000));
        checkUnprocessableEntity(JSON_MAPPER.writeValueAsBytes(request), ScoringException.class, CONVEYOR_CALCULATION_URL, ExceptionCode.AMOUNT_TOO_BIG);
    }

    @Test
    @DisplayName("POST /conveyor/calculation")
    @SneakyThrows
    void testScoringAge() {

        ScoringDataDto request = getScoringDataDto(getEmploymentDto()).birthdate(NOW);
        checkUnprocessableEntity(JSON_MAPPER.writeValueAsBytes(request), ScoringException.class, CONVEYOR_CALCULATION_URL, ExceptionCode.INAPPROPRIATE_AGE);

        request = getScoringDataDto(getEmploymentDto()).birthdate(LocalDate.of(1900, 1, 1));
        checkUnprocessableEntity(JSON_MAPPER.writeValueAsBytes(request), ScoringException.class, CONVEYOR_CALCULATION_URL, ExceptionCode.INAPPROPRIATE_AGE);
    }

    @Test
    @DisplayName("POST /conveyor/calculation")
    @SneakyThrows
    void testScoringWorkExperience() {

        ScoringDataDto request = getScoringDataDto(getEmploymentDto().workExperienceTotal(10));
        checkUnprocessableEntity(JSON_MAPPER.writeValueAsBytes(request), ScoringException.class, CONVEYOR_CALCULATION_URL, ExceptionCode.INAPPROPRIATE_WORK_EXPERIENCE_TOTAL);

        request = getScoringDataDto(getEmploymentDto().workExperienceCurrent(2));
        checkUnprocessableEntity(JSON_MAPPER.writeValueAsBytes(request), ScoringException.class, CONVEYOR_CALCULATION_URL, ExceptionCode.INAPPROPRIATE_WORK_EXPERIENCE_CURRENT);
    }

    private LoanApplicationRequestDto getLoanApplicationRequestDto() {

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

    private ScoringDataDto getScoringDataDto(EmploymentDto employmentDto) {
        return ScoringDataDto.builder()
            .amount(BigDecimal.valueOf(10000))
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

    private EmploymentDto getEmploymentDto() {
        return EmploymentDto.builder()
            .employmentStatus(EmploymentDto.EmploymentStatusEnum.EMPLOYED)
            .employerInn("1234567890")
            .salary(BigDecimal.valueOf(10000))
            .position(EmploymentDto.PositionEnum.WORKER)
            .workExperienceTotal(13)
            .workExperienceCurrent(4)
            .build();
    }

    @SneakyThrows
    private void checkBadRequest(byte[] content) {

        mockMvc.perform(post(CONVEYOR_OFFERS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    private void checkUnprocessableEntity(byte[] request, Class<?> exception, String path, ExceptionCode message) {

        mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.timestamp").value(new StringContains(NOW.format(DateTimeFormatter.ISO_LOCAL_DATE))))
            .andExpect(jsonPath("$.exception").value(exception.getSimpleName()))
            .andExpect(jsonPath("$.status").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
            .andExpect(jsonPath("$.error").value(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase()))
            .andExpect(jsonPath("$.path").value(path))
            .andExpect(jsonPath("$.message").value(message.getMessage()));
    }
}

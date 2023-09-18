package ru.neoflex.learning.creaditpipeline.conveyor.test.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.SneakyThrows;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.model.LoanApplicationRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.neoflex.learning.creaditpipeline.conveyor.exception.ExceptionCode;
import ru.neoflex.learning.creaditpipeline.conveyor.exception.PrescoringException;

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

        final LocalDate now = LocalDate.now();
        final LoanApplicationRequestDto content = getLoanApplicationRequestDto().birthdate(now);
        mockMvc.perform(post(CONVEYOR_OFFERS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_MAPPER.writeValueAsBytes(content)))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.timestamp").value(new StringContains(now.format(DateTimeFormatter.ISO_LOCAL_DATE))))
            .andExpect(jsonPath("$.exception").value(PrescoringException.class.getSimpleName()))
            .andExpect(jsonPath("$.status").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
            .andExpect(jsonPath("$.error").value(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase()))
            .andExpect(jsonPath("$.path").value(CONVEYOR_OFFERS_URL))
            .andExpect(jsonPath("$.message").value(ExceptionCode.AGE_NOT_VALID.getMessage()));
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
    void testScoringUnemployed() {
        //ToDo
    }

    @Test
    @DisplayName("POST /conveyor/calculation")
    void testScoringAmount() {
        //ToDo
    }

    @Test
    @DisplayName("POST /conveyor/calculation")
    void testScoringAge() {
        //ToDo
    }

    @Test
    @DisplayName("POST /conveyor/calculation")
    void testScoringWorkExperience() {
        //ToDo
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

    private void checkBadRequest(byte[] content) throws Exception {

        mockMvc.perform(post(CONVEYOR_OFFERS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isBadRequest());
    }
}

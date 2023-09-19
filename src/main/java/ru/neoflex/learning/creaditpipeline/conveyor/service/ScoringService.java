package ru.neoflex.learning.creaditpipeline.conveyor.service;

import org.openapitools.model.EmploymentDto;
import org.openapitools.model.ScoringDataDto;
import org.springframework.stereotype.Service;
import ru.neoflex.learning.creaditpipeline.conveyor.exception.ExceptionCode;
import ru.neoflex.learning.creaditpipeline.conveyor.exception.ScoringException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.BUSINESS_OWNER_RATE_INCREASE;
import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.GENDER_RATE;
import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.MARRIED_RATE_DECREASE;
import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.MAX_AGE;
import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.MAX_MALE_AGE;
import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.MID_MANAGER_RATE_DECREASE;
import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.MIN_AGE;
import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.MIN_CURRENT_EXPERIENCE;
import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.MIN_DEPENDENT_AMOUNT;
import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.MIN_FEMALE_AGE;
import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.MIN_MALE_AGE;
import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.MIN_SALARIES_COUNT;
import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.MIN_TOTAL_EXPERIENCE;
import static ru.neoflex.learning.creaditpipeline.conveyor.util.Constant.TOP_MANAGER_RATE_DECREASE;

@Service
public class ScoringService {

    public boolean validAge(LocalDate birthdate) {

        final LocalDate now = LocalDate.now();
        if (birthdate.isBefore(now)) {
            return calcAge(birthdate) >= 18;
        }
        return false;
    }

    public BigDecimal scoring(BigDecimal rate, ScoringDataDto scoringDataDto) {

        scoringAge(scoringDataDto.getBirthdate());
        final EmploymentDto employment = scoringDataDto.getEmployment();
        rate = scoringEmploymentStatus(rate, employment.getEmploymentStatus());
        scoringWorkExperience(employment.getWorkExperienceTotal(), employment.getWorkExperienceCurrent());
        scoringAmount(scoringDataDto.getAmount(), employment.getSalary());
        rate = scoringPosition(rate, employment.getPosition());
        rate = scoringMaritalStatus(rate, scoringDataDto.getMaritalStatus());
        rate = scoringDependentAmount(rate, scoringDataDto.getDependentAmount());
        rate = scoringGenderAge(rate, scoringDataDto.getGender(), scoringDataDto.getBirthdate());

        return rate;
    }

    private void scoringWorkExperience(Integer workExperienceTotal, Integer workExperienceCurrent) {

        if (workExperienceTotal.compareTo(MIN_TOTAL_EXPERIENCE) < 0) {
            throw new ScoringException(ExceptionCode.INAPPROPRIATE_WORK_EXPERIENCE_TOTAL.getMessage());
        }
        if (workExperienceCurrent.compareTo(MIN_CURRENT_EXPERIENCE) < 0) {
            throw new ScoringException(ExceptionCode.INAPPROPRIATE_WORK_EXPERIENCE_CURRENT.getMessage());
        }
    }

    private void scoringAmount(BigDecimal amount, BigDecimal salary) {

        if (amount.compareTo(salary.multiply(BigDecimal.valueOf(MIN_SALARIES_COUNT))) > 0) {

            throw new ScoringException(ExceptionCode.AMOUNT_TOO_BIG.getMessage());
        }
    }

    private BigDecimal scoringGenderAge(BigDecimal rate, ScoringDataDto.GenderEnum gender, LocalDate birthdate) {

        final int age = calcAge(birthdate);
        if (ScoringDataDto.GenderEnum.NON_BINARY.equals(gender)) {

            return rate.add(BigDecimal.valueOf(GENDER_RATE));
        }
        if (ScoringDataDto.GenderEnum.FEMALE.equals(gender) && (age >= MIN_FEMALE_AGE && age <= MAX_AGE)) {

            return rate.subtract(BigDecimal.valueOf(GENDER_RATE));
        }
        if (ScoringDataDto.GenderEnum.MALE.equals(gender) && (age >= MIN_MALE_AGE && age <= MAX_MALE_AGE)) {

            return rate.subtract(BigDecimal.valueOf(GENDER_RATE));
        }

        return rate;
    }

    private BigDecimal scoringDependentAmount(BigDecimal rate, Integer dependentAmount) {

        return dependentAmount > MIN_DEPENDENT_AMOUNT ? rate.add(BigDecimal.ONE) : rate;
    }

    private BigDecimal scoringMaritalStatus(BigDecimal rate, ScoringDataDto.MaritalStatusEnum maritalStatus) {

        return switch (maritalStatus) {
            case MARRIED -> rate.subtract(BigDecimal.valueOf(MARRIED_RATE_DECREASE));
            case DIVORCED -> rate.add(BigDecimal.ONE);
            default -> rate;
        };
    }

    private BigDecimal scoringPosition(BigDecimal rate, EmploymentDto.PositionEnum position) {

        return switch (position) {
            case MID_MANAGER -> rate.subtract(BigDecimal.valueOf(MID_MANAGER_RATE_DECREASE));
            case TOP_MANAGER -> rate.subtract(BigDecimal.valueOf(TOP_MANAGER_RATE_DECREASE));
            default -> rate;
        };
    }

    private BigDecimal scoringEmploymentStatus(BigDecimal rate, EmploymentDto.EmploymentStatusEnum employmentStatus) {

        return switch (employmentStatus) {
            case UNEMPLOYED -> throw new ScoringException(ExceptionCode.UNEMPLOYED_STATUS.getMessage());
            case SELF_EMPLOYED -> rate.add(BigDecimal.ONE);
            case BUSINESS_OWNER -> rate.add(BigDecimal.valueOf(BUSINESS_OWNER_RATE_INCREASE));
            default -> rate;
        };
    }

    private void scoringAge(LocalDate birthdate) {

        final int age = calcAge(birthdate);
        if (age < MIN_AGE || age > MAX_AGE) {

            throw new ScoringException(ExceptionCode.INAPPROPRIATE_AGE.getMessage());
        }
    }

    private int calcAge(LocalDate birthdate) {

        return Period.between(birthdate, LocalDate.now()).getYears();
    }
}

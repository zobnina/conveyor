package ru.neoflex.learning.creaditpipeline.conveyor.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class ScoringService {

    public boolean validAge(LocalDate birthdate) {

        final LocalDate now = LocalDate.now();
        if (birthdate.isAfter(now)) {
            final long age = ChronoUnit.YEARS.between(now, birthdate);
            return age >= 18;
        }
        return false;
    }
}

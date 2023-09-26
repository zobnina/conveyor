package ru.neoflex.learning.creaditpipeline.conveyor.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
@ConfigurationProperties(prefix = "conveyor")
public class ConveyorProperties {

    /**
     * Параметры процентной ставки
     */
    Rate rate;

    /**
     * Параетры страховки
     */
    Insurance insurance;
}

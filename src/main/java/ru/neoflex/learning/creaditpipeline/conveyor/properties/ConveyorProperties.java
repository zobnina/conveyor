package ru.neoflex.learning.creaditpipeline.conveyor.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "conveyor")
public class ConveyorProperties {

    /**
     * Параметры процентной ставки
     */
    private Rate rate;

    /**
     * Параетры страховки
     */
    private Insurance insurance;
}

package ru.neoflex.learning.creaditpipeline.conveyor.test.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.neoflex.learning.creaditpipeline.conveyor.service.ConveyorService;

@SpringBootTest
class ConveyorServiceTest {

    @Autowired
    ConveyorService conveyorService;

    @Test
    @DisplayName("")
    void offersTest(){
//        conveyorService.offers();
    }
}

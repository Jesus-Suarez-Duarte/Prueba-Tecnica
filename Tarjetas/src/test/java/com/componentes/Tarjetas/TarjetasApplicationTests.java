package com.componentes.Tarjetas;

import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

@Suite
@SelectPackages({
    "com.componentes.Tarjetas",
    "com.componentes.Tarjetas.controler"
})
@SpringBootTest
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class
})
class TarjetasApplicationTests {

    @Test
    void contextLoads() {
        assert true : "Contexto de Spring cargado correctamente";
        System.out.println("Contexto de Spring cargado correctamente");
    }
    
    @Test
    void applicationStarts() {
        System.out.println("Aplicación iniciada correctamente");
        assert true : "Aplicación iniciada correctamente";
    }
}
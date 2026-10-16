package com.springboot.application;

import com.springboot.application.ScsmsApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ScsmsApplication.class) // Fix: link to actual main class
class ScsmsApplicationTests {

    @Test
    void contextLoads() {
    }

}
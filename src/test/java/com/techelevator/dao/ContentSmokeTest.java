package com.techelevator.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(classes = TestingDatabaseConfig.class)
public class ContentSmokeTest {
    @Test
    public void contextLoads() {
    }
}
package com.sokeila.woody.backend.rest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SmokeTest {

    @Autowired
    private FeedController feedController;

    @Test
    public void contextLoads() {
        assertThat(feedController).isNotNull();
    }
}

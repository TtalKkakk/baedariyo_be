package com.house.biet.support.config;

import jakarta.transaction.Transactional;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestRiderCallSenderConfig.class)
@Transactional
public class ServiceIntegrationTest {
}

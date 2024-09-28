package com.example.order_processing;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class OrderProcessingApplicationTests {

	@Test
	void contextLoads() {
	}

}

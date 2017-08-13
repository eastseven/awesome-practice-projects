package cn.eastseven.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class KafkaLearningApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaLearningApplication.class, args);
	}
}
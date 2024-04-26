package com.hallym.project.RingRingRing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ForwardedHeaderFilter;

@SpringBootApplication
public class RingRingRingApplication {

	public static void main(String[] args) {
		SpringApplication.run(RingRingRingApplication.class, args);
	}
	@Bean
	ForwardedHeaderFilter forwardedHeaderFilter() {
	   return new ForwardedHeaderFilter();
	}

}

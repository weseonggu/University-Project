package com.hallym.project.RingRingRing.AI;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AICallService {
	private final static String externalServerUrl = "http://127.0.0.1:8000/kogpt2-test";
	
	public String fastAPIRequest(Conversation sendData) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Conversation> requestEntity = new HttpEntity<>(sendData, headers);
        ResponseEntity<String> responseEntity = new RestTemplate().exchange(
                externalServerUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
            );
        return responseEntity.getBody();
	}
}

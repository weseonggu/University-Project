package com.hallym.project.RingRingRing.AI;


import java.net.SocketTimeoutException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
public class AICallService {
	
	private RestTemplate restTemplate;
	
	private final static String externalServerUrl = "http://125.240.0.97:7000";
	
	private static final int TIMEOUT = 5000;
	
    public AICallService() {
        this.restTemplate = createRestTemplate();
    }
    
	/**
	 * 타임 아웃 설정
	 * @return
	 */
    private RestTemplate createRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(TIMEOUT);
        factory.setReadTimeout(TIMEOUT);
        return new RestTemplate(factory);
    }
	/**
	 * 배달모델 문장생서 외부 서버에 요청
	 * @param sendData
	 * @return
	 */
	public String fastAPIRequest(Conversation sendData) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Conversation> requestEntity = new HttpEntity<>(sendData, headers);
		
		
		try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    externalServerUrl + "/delivery-chat",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            return responseEntity.getBody();
        } catch (RestClientException e) {
            // 타임아웃 예외 처리
            System.err.println("Request timed out: " + e.getMessage());
            return "Request timed out";
        } catch (Exception e) {
            // 기타 예외 처리
            System.err.println("Request failed: " + e.getMessage());
            return "Request failed";
        }
	}
	/**
	 * ai서버 연결 확인
	 * @param sendData
	 * @return
	 */
	public String fastAPIIsConnected() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Conversation> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = new RestTemplate().exchange(
                externalServerUrl+"/isConnected",
                HttpMethod.GET,
                requestEntity,
                String.class
            );
        return responseEntity.getBody();
	}

	public String fastAPIReservation(Conversation sendData) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Conversation> requestEntity = new HttpEntity<>(sendData, headers);
		
		
		try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    externalServerUrl + "/reservation-chat",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            return responseEntity.getBody();
        } catch (RestClientException e) {
            // 타임아웃 예외 처리
            System.err.println("Request timed out: " + e.getMessage());
            return "Request timed out";
        } catch (Exception e) {
            // 기타 예외 처리
            System.err.println("Request failed: " + e.getMessage());
            return "Request failed";
        }
	}
}

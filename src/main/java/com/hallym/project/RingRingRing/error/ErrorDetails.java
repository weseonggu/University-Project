package com.hallym.project.RingRingRing.error;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorDetails {
	
	private LocalDate timeStamp;
	private String message;
	private String details;

}

package com.hallym.project.RingRingRing.message;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FailMessage {
	private String timeStamp;
	private String endPoint;
	private List<String> errorDetails;
}

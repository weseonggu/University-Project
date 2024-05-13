package com.hallym.project.RingRingRing.message;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Message {
	private LocalDate date;
	private String Massage;
}

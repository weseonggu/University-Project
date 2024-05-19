package com.hallym.project.RingRingRing.Mail.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailCodeDTO {
	private String email;
	private int code;
}

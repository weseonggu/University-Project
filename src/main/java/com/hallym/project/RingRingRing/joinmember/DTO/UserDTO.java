package com.hallym.project.RingRingRing.joinmember.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	
	private Long id;
	
	@NotNull(message = "이름을 입력해주세요")
	private String name;
	
	@NotNull(message = "이메일을 입력해주세요")
	@Email(message = "이메일 형식으로 입력해 주세요")
	private String email;
	
	@NotNull(message = "비번을 입력하세요")
	@Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\\\":{}|<>])(?=.*[a-zA-Z])(?=.*[0-9]).{8,}$", 
	message = "8자 이상글자에 적어도 하나의 특수문자, 영문자, 숫자를 포함해야 합니다.")
	private String pwd;
	
}

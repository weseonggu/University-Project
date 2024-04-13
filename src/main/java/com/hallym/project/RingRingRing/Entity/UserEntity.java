package com.hallym.project.RingRingRing.Entity;

import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name="user_id")
	private Long id;
	
	@Email(message = "이메일 형식으로 입력해 주세요")
	private String email;
	
	@Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\\\":{}|<>])(?=.*[a-zA-Z])(?=.*[0-9]).{8,}$", 
			message = "8자 이상글자에 적어도 하나의 특수문자, 영문자, 숫자를 포함해야 합니다.")
	private String pwd;
	
    @JsonIgnore
    @OneToMany(mappedBy="user",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<AuthorityEntity> authorities;

	@JsonIgnore
	@OneToMany(mappedBy="user",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<ScenarioEntity> scenarios;
    
}

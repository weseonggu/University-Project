package com.hallym.project.RingRingRing.Entity;

import java.util.Set;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * 유저 정보를 저장하는데 사용되는 엔티티 유형성 검사를 설정되어 있음
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class UserEntity {
	
	/**
	 * userEntity의 기본키 자동 생성
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name="user_id")
	private Long id;
	
	/**
	 * 사용자이름 속성 null값입력시 MethodArgumentNotValidException
	 */
	@NotNull(message = "이름을 입력해주세요")
	private String name;
	
	/**
	 * 사용자이름 속성 null값입력또는 이메일 형식이 아니면 MethodArgumentNotValidException
	 */
	@NotNull(message = "이메일을 입력해주세요")
	@Email(message = "이메일 형식으로 입력해 주세요")
	private String email;
	
	/**
	 * 사용자이름 속성 null값입력 또는 조건식에 틀리면 MethodArgumentNotValidException
	 */
	@NotNull
	@Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\\\":{}|<>])(?=.*[a-zA-Z])(?=.*[0-9]).{8,}$", 
			message = "8자 이상글자에 적어도 하나의 특수문자, 영문자, 숫자를 포함해야 합니다.")
	private String pwd;
	
    @OneToMany(mappedBy="user",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<AuthorityEntity> authorities;

	@JsonIgnore
	@OneToMany(mappedBy="user",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<ScenarioEntity> scenarios;
    
}

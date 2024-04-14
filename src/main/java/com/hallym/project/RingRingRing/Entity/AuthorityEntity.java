package com.hallym.project.RingRingRing.Entity;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 *  DB에 저장되는 데이터로 권한 관련 정보가 저장 되어있다. 제야조건으로UserEntity랑 연결 되어 있음
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authority")
public class AuthorityEntity {
	/**
	 * 기본키 로 자동 생성
	 */
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name="authority_id")
    private Long id;
    /**
     * 권한 저장 형식 ROLE_...
     */
    private String role;
    
    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;
    

}

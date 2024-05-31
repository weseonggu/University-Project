package com.hallym.project.RingRingRing.weeklyUsageAnalysis.DTO;

import java.time.LocalDateTime;

import com.hallym.project.RingRingRing.joinmember.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyUsageDTO {


    private int timestamp;

    private Long duration;

    private LocalDateTime week;


}

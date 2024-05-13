package com.hallym.project.RingRingRing.DTO;

import java.time.LocalDateTime;

import com.hallym.project.RingRingRing.Entity.UserEntity;
import com.hallym.project.RingRingRing.Entity.WeeklyUsageAnalysisEntity;

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

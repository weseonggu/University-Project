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

//    private int weekNumber;
    private Long id;

    private int timestamp;

    private Long duration;

    private LocalDateTime week;

    public WeeklyUsageAnalysisEntity toEntity(UserEntity user){
        return WeeklyUsageAnalysisEntity.builder()
                .timestamp(timestamp)
                .duration(duration)
                .week(LocalDateTime.now())
                .build();
    }

}

package com.hallym.project.RingRingRing.DTO;

import com.hallym.project.RingRingRing.Entity.UserEntity;
import com.hallym.project.RingRingRing.Entity.WeeklyUsageAnalysisEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

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

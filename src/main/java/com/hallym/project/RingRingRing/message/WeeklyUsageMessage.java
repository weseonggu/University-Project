package com.hallym.project.RingRingRing.message;

import com.hallym.project.RingRingRing.Entity.WeeklyUsageAnalysisEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class WeeklyUsageMessage {
    private Long duration;
    private Long average;
    private String message;
}

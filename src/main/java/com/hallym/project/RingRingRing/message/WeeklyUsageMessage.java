package com.hallym.project.RingRingRing.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WeeklyUsageMessage {
    private Long duration;
    private String message;
}

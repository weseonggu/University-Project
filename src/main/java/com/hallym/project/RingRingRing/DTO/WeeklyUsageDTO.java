package com.hallym.project.RingRingRing.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WeeklyUsageDTO {

    private int weekNumber;
    private int usageTime;

    public WeeklyUsageDTO(int weekNumber, int usageTime) {
        this.weekNumber = weekNumber;
        this.usageTime = usageTime;
    }
}

package com.hallym.project.RingRingRing.DTO;

import java.time.LocalDateTime;

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

    private Double average;

    private LocalDateTime week;


}

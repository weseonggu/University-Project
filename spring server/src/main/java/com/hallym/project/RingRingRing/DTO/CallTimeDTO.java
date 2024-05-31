package com.hallym.project.RingRingRing.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CallTimeDTO {
    private Long userId;
    private int callTime;
}

package com.hallym.project.RingRingRing.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CallTimeMessage {
    private int callTime;
    private String message;
}

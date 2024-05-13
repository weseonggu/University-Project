package com.hallym.project.RingRingRing.weeklyUsageAnalysis;

import com.hallym.project.RingRingRing.message.WeeklyUsageMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WeeklyUsageController {
    private final WeeklyUsageService weeklyUsageService;

    /**
     * 요청 방식: GET<br>
     * EndPoint: /usage/만들었던 이메일<br>
     * @param email 주소에 입력한 이메일<br>
     * @return CODE: 200 BODY: {"duration": "[사용 시간]", "message": "평균 연습 시간"} <br>
     */
    @GetMapping("/usage/{email}")
    public ResponseEntity<WeeklyUsageMessage> getWeeklyUsage(@PathVariable("email") String email){
        return weeklyUsageService.getWeeklyUsageByEmail(email);
    }

}

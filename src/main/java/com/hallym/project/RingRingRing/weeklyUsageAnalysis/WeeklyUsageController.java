package com.hallym.project.RingRingRing.weeklyUsageAnalysis;

import com.hallym.project.RingRingRing.message.WeeklyUsageMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name="주간 사용 통계 컨트롤러", description = "이메일로 주간 사용 통계 확인")
public class WeeklyUsageController {
    private final WeeklyUsageService weeklyUsageService;

    /**
     * 요청 방식: GET<br>
     * EndPoint: /usage/만들었던 이메일<br>
     * @param email 주소에 입력한 이메일<br>
     * @return CODE: 200 BODY: {"duration": "[사용 시간]", "message": "평균 연습 시간"} <br>
     */
    @GetMapping("/usage/{email}")
    @Operation(summary = "주간 사용 통계 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "duration: (Long)누적시간,<br>average: (Long)평균시간,<br>message: 주간 연습 시간"),
            @ApiResponse(responseCode = "500", description = "통계 조회 실패")
    })
    public ResponseEntity<WeeklyUsageMessage> getWeeklyUsage(@PathVariable("email") String email){
        log.info("이메일에 따른 주간 사용시간 통계: " + email);
        return weeklyUsageService.getWeeklyUsageByEmail(email);
    }

}

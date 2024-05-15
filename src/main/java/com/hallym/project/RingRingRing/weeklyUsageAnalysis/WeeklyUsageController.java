package com.hallym.project.RingRingRing.weeklyUsageAnalysis;

import com.hallym.project.RingRingRing.DTO.CallTimeDTO;
import com.hallym.project.RingRingRing.message.CallTimeMessage;
import com.hallym.project.RingRingRing.message.WeeklyUsageMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    /**
     * 요청 방식: PUT<br>
     * EndPoint: /save <br>
     * @param callTimeDTO 사용자 정보로 {"userID":유저Id(ex 1), "callTime":통화시간(ex 200)}를 body에 넣어서 요청<br>
     * @return CODE: 200 BODY: {"callTime": "[통화 시간]", "message": "통화 시간 저장"} <br>
     */
    @PutMapping("/save")
    @Operation(summary = "통화 시간 저장 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "callTime: (Long)통화시간,<br>message: 통화 시간 저장"),
            @ApiResponse(responseCode = "400", description = "Failed to read request")
    })
    public ResponseEntity<CallTimeMessage> putSavedCallTime(@RequestBody CallTimeDTO callTimeDTO){
        log.info("통화시간 저장 요청");
        return weeklyUsageService.saveCallTime(callTimeDTO);
    }
}


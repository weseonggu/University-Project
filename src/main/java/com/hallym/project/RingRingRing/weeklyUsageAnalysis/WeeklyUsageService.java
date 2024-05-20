package com.hallym.project.RingRingRing.weeklyUsageAnalysis;

import com.hallym.project.RingRingRing.DTO.CallTimeDTO;
import com.hallym.project.RingRingRing.DTO.WeeklyUsageDTO;
import com.hallym.project.RingRingRing.Entity.UserEntity;
import com.hallym.project.RingRingRing.Entity.WeeklyUsageAnalysisEntity;
import com.hallym.project.RingRingRing.customexception.UsageFailException;
import com.hallym.project.RingRingRing.message.CallTimeMessage;
import com.hallym.project.RingRingRing.message.WeeklyUsageMessage;
import com.hallym.project.RingRingRing.repository.UserRepository;
import com.hallym.project.RingRingRing.repository.WeeklyUsageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class WeeklyUsageService {
    private final WeeklyUsageRepository weeklyUsageRepository;
    private final UserRepository userRepository;

        public ResponseEntity<WeeklyUsageMessage> getWeeklyUsageById(Long id) {

            LocalDate now = LocalDate.now();
            LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

            try{
                Map<String, Object> usageStatistics = weeklyUsageRepository.findWeeklyUsageByIdAndTimestampBetween(id, startOfWeek.atStartOfDay(), endOfWeek.atTime(LocalTime.MAX));

                Long duration = (Long) usageStatistics.get("duration");
                Double average = (Double) usageStatistics.get("average");
                Long averageLong = (long) Math.floor(average);

                log.info("user id에 따른 주간 사용 통계 전송: " + id);

                return new ResponseEntity<WeeklyUsageMessage>(new WeeklyUsageMessage(duration, averageLong, "주간 연습 시간"), HttpStatus.OK);

            } catch (Exception e){
                throw new UsageFailException("사용통계 조회 실패(사용 이력이 없습니다.)");
            }

//            Map<String, Object> usageStatistics = weeklyUsageRepository.findWeeklyUsageByIdAndTimestampBetween(id, startOfWeek.atStartOfDay(), endOfWeek.atTime(LocalTime.MAX));
//
//            Long duration = (Long) usageStatistics.get("duration");
//            Double average = (Double) usageStatistics.get("average");
//            Long averageLong = (long) Math.floor(average);
//
//            log.info("user id에 따른 주간 사용 통계 전송: " + id);
//
//            return new ResponseEntity<WeeklyUsageMessage>(new WeeklyUsageMessage(duration, averageLong, "주간 연습 시간"), HttpStatus.OK);

        }

        public ResponseEntity<CallTimeMessage> saveCallTime(CallTimeDTO callTimeDTO){
            int callTimes = callTimeDTO.getCallTime();
            UserEntity user = userRepository.findById(callTimeDTO.getUserId()).orElse(null);

            WeeklyUsageAnalysisEntity callTime = WeeklyUsageAnalysisEntity.builder()
                    .user(user)
                    .timestamp(callTimes)
                    .week(LocalDateTime.now())
                    .build();
            weeklyUsageRepository.save(callTime);

            log.info("통화 시간 저장 중");
            return new ResponseEntity<CallTimeMessage>(new CallTimeMessage(callTimes, "통화 시간 저장"), HttpStatus.OK);
        }


}

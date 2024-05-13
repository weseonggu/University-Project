package com.hallym.project.RingRingRing.weeklyUsageAnalysis;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import com.hallym.project.RingRingRing.DTO.WeeklyUsageDTO;
import com.hallym.project.RingRingRing.Entity.UserEntity;
import com.hallym.project.RingRingRing.message.WeeklyUsageMessage;
import com.hallym.project.RingRingRing.repository.UserRepository;
import com.hallym.project.RingRingRing.repository.WeeklyUsageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class WeeklyUsageService {
    private final WeeklyUsageRepository weeklyUsageRepository;
    private final UserRepository userRepository;

    public ResponseEntity<WeeklyUsageMessage> getWeeklyUsageByEmail(String email) {

        UserEntity user = userRepository.findByEmail(email).get(0);

        Long duration = 0L;
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

//        WeeklyUsageAnalysisEntity testUsage = WeeklyUsageAnalysisEntity.builder()
//                        .user(user)
//                        .timestamp(15)
//                                .duration(duration)
//                                        .week(LocalDateTime.now()).build();
//        weeklyUsageRepository.save(testUsage);

        List<WeeklyUsageDTO> weeklyUsageDTO = weeklyUsageRepository.findWeeklyUsageByEmailAndTimestampBetween(email, startOfWeek.atStartOfDay(), endOfWeek.atTime(LocalTime.MAX));
        for(WeeklyUsageDTO wud : weeklyUsageDTO){
            duration += wud.getTimestamp();
        }

        return new ResponseEntity<WeeklyUsageMessage>(new WeeklyUsageMessage(duration, "평균 연습 시간"), HttpStatus.OK);

    }
}

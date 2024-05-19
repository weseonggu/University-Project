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

import com.hallym.project.RingRingRing.joinmember.entity.UserEntity;
import com.hallym.project.RingRingRing.joinmember.repository.UserRepository;
import com.hallym.project.RingRingRing.message.WeeklyUsageMessage;

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

        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        Long duration = weeklyUsageRepository.findWeeklyUsageByEmailAndTimestampBetween(email, startOfWeek.atStartOfDay(), endOfWeek.atTime(LocalTime.MAX));
        log.info("이메일에 따른 주간 사용 통계 전송: " + email);

        return new ResponseEntity<WeeklyUsageMessage>(new WeeklyUsageMessage(duration, "평균 연습 시간"), HttpStatus.OK);

    }

//        WeeklyUsageAnalysisEntity testUsage = WeeklyUsageAnalysisEntity.builder()
//                        .user(user)
//                        .timestamp(15)
//                        .week(LocalDateTime.now()).build();
//        weeklyUsageRepository.save(testUsage);

//        }
}

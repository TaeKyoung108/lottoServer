package com.example.lotto.service;


import com.example.lotto.domain.lotto.LottoStatus;
import com.example.lotto.domain.lotto.LottoStatusResponseDto;
import com.example.lotto.repository.LottoRepository;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class LottoService {

    private final LottoRepository lottoRepository;

    /**
     * 일단은 회차 있으면 저장 안되게 함
     * @param lottoStatus
     */
    @Transactional
    public void save(LottoStatus lottoStatus) {
        LottoStatus target = lottoRepository.findOne(lottoStatus.getRound());
        if (target == null){
            lottoRepository.save(lottoStatus);
        }
        else {
            ;
        }
    }

    /**
     * Optional 안썼으므로 null 가능성 있음
     * @param round
     * @return
     */
    public LottoStatus findOne(Long round){
        return lottoRepository.findOne(round);
    }

    public List<LottoStatus> findAll(){
        return lottoRepository.findAll();
    }

    /**
     * -1이면 잘못 입력된 값
     * @param targetNumber
     * @return
     */
    public int countOccurrencesOfNumber(int targetNumber, boolean includeBonus) {
        if (targetNumber>=0 && targetNumber<=45){
            return lottoRepository.countOccurrencesOfNumber(targetNumber, includeBonus);
        }
        return -1;
    }

    /**
     * 다가올 추첨일이 몇회차 추첨인지 알려줌
     *
     * 현재 회차가 몇회차인지 알아내는 부분
     * 예를들면 1115 회까지 추첨하였고 1116회차를 구매하는 중이면 1116회차를 결과로 줌
     */
    public Long CurrentRound(){
        LocalDate startDate = LocalDate.of(2002, 12, 7); // 로또 1회 추첨 날짜
        LocalDate lastSaturday;
        LocalDate today = LocalDate.now();

        /**
         * today.with(DayOfWeek.SATURDAY); 월~일 기준 이번주 포함 토요일을 가져옴
         * 일요일까지는 이번주꺼를 가져오므로 금요일까지는 전주를 가져와야함
         */
        // 오늘이 토요일이고 오후 9시가 지났는지 확인
        if (today.getDayOfWeek() == DayOfWeek.SATURDAY && LocalTime.now().isAfter(LocalTime.of(21, 0))) {
            // 현재 시간이 토요일이고 22시 이전이라면, 이전 주의 토요일을 찾음
            lastSaturday = today.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
        }
        //오늘이 일요일이면 다음주 토요일
        else if (today.getDayOfWeek() == DayOfWeek.SUNDAY){
            lastSaturday = today.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
        }
        else{
            // 그 외의 경우에는 오늘을 포함하여 가장 최근의 토요일을 찾음
            lastSaturday = today.with(DayOfWeek.SATURDAY);
        }

        // 시작 날짜부터 마지막 토요일까지의 주 수를 계산
        Long weeksBetween = ChronoUnit.WEEKS.between(startDate, lastSaturday);

        // 로또 회차 계산 (1회부터 시작하므로 1을 더함)
        Long drawNumber = weeksBetween + 1;
        log.info("8월 31일 토요일 기준 가장 최근 토요일" + LocalDate.of(2024, 8, 31).with(DayOfWeek.SATURDAY));
        log.info("9월 1일 일요일 기준 가장 최근 토요일" + LocalDate.of(2024, 9, 1).with(DayOfWeek.SATURDAY));
        log.info("다가올 추첨일"+lastSaturday);

        return drawNumber;
    }

    @Transactional
    public void fetchAndInsertData(LottoStatusResponseDto lottoStatusResponseDto){
        LottoStatus lottoStatus = new LottoStatus(lottoStatusResponseDto);
        lottoRepository.save(lottoStatus);
    }

    /**
     * startNum 과 endNum 을 포함한 그 사이값중 db에 없는 값을 return
     * @param startNum
     * @param endNum
     * @return
     */
    public List<Integer> findMissedRound(Long startNum, Long endNum) {
        List<Long> allRounds = lottoRepository.findMissedRound(); // 데이터베이스에서 모든 라운드 가져오기
        List<Integer> missedRounds = new ArrayList<>();

        // startNum과 endNum 사이의 값 중에서 allRounds에 없는 숫자 찾기
        for (int i = startNum.intValue(); i <= endNum.intValue(); i++) {
            if (!allRounds.contains((long) i)) {
                missedRounds.add(i);
            }
        }

        return missedRounds;
    }

    @Transactional
    public LottoStatus saveSecondThird(Long round,Long second, Long third){
        LottoStatus lottoStatus = lottoRepository.saveSecondThird(round, second, third);

        return lottoStatus;
    }

    public List<Long> findMissedSecondThird(){
        List<Long> missedSecondThird = lottoRepository.findMissedSecondThird();

        return missedSecondThird;
    }

}

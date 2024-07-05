package com.example.lotto.domain.lotto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class LottoStatus {
    @Id
    private Long round;  // 회차 drwNo
    private LocalDate drawingDate;  // 추첨 날짜
    private long totalSellAmount;  // 총상금액 totSellamnt
    private long firstPrize;  // 1등 상금액 firstWinamnt
    private long secondPrize;   //2등 당첨금
    private long thirdPrize;    //3등 당첨금
    private int firstWinnerCount;  // 1등 당첨인원 firstPrzwnerCo
    private long totalFirstPrize;  // 1등 누적금액 firstAccumamnt
    private int winningNumber1;  // 로또번호 1 drwtNo1
    private int winningNumber2;  // 로또번호 2 drwtNo2
    private int winningNumber3;  // 로또번호 3 drwtNo3
    private int winningNumber4;  // 로또번호 4 drwtNo4
    private int winningNumber5;  // 로또번호 5 drwtNo5
    private int winningNumber6;  // 로또번호 6 drwtNo6
    private int bonusNumber;  // 보너스 번호 bnusNo

    public LottoStatus(LottoStatusResponseDto lottoStatusResponseDto) {
        this.round = lottoStatusResponseDto.getRound();
        this.drawingDate = lottoStatusResponseDto.getDrawingDate();
        this.totalSellAmount = lottoStatusResponseDto.getTotalSellAmount();
        this.firstPrize = lottoStatusResponseDto.getFirstPrize();
        this.secondPrize = 0;
        this.thirdPrize = 0;
        this.firstWinnerCount = lottoStatusResponseDto.getFirstWinnerCount();
        this.totalFirstPrize = lottoStatusResponseDto.getTotalFirstPrize();
        this.winningNumber1 = lottoStatusResponseDto.getWinningNumber1();
        this.winningNumber2 = lottoStatusResponseDto.getWinningNumber2();
        this.winningNumber3 = lottoStatusResponseDto.getWinningNumber3();
        this.winningNumber4 = lottoStatusResponseDto.getWinningNumber4();
        this.winningNumber5 = lottoStatusResponseDto.getWinningNumber5();
        this.winningNumber6 = lottoStatusResponseDto.getWinningNumber6();
        this.bonusNumber = lottoStatusResponseDto.getBonusNumber();


    }
}

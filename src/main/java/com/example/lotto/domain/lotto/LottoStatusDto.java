package com.example.lotto.domain.lotto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class LottoStatusDto {
    @JsonProperty("drwNo")
    private Long round;

    @JsonProperty("drwNoDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate drawingDate;

    @JsonProperty("totSellamnt")
    private long totalSellAmount;

    @JsonProperty("firstWinamnt")
    private long firstPrize;

    @JsonProperty("firstPrzwnerCo")
    private int firstWinnerCount;

    @JsonProperty("firstAccumamnt")
    private long totalFirstPrize;

    @JsonProperty("drwtNo1")
    private int winningNumber1;

    @JsonProperty("drwtNo2")
    private int winningNumber2;

    @JsonProperty("drwtNo3")
    private int winningNumber3;

    @JsonProperty("drwtNo4")
    private int winningNumber4;

    @JsonProperty("drwtNo5")
    private int winningNumber5;

    @JsonProperty("drwtNo6")
    private int winningNumber6;

    @JsonProperty("bnusNo")
    private int bonusNumber;

    private Boolean valid;

    public LottoStatusDto(Long round) {
        this.round = round;
        this.drawingDate = null;
        this.totalSellAmount = 0;
        this.firstPrize = 0;
        this.firstWinnerCount = 0;
        this.totalFirstPrize = 0;
        this.winningNumber1 = 0;
        this.winningNumber2 = 0;
        this.winningNumber3 = 0;
        this.winningNumber4 = 0;
        this.winningNumber5 = 0;
        this.winningNumber6 = 0;
        this.bonusNumber = 0;
        this.valid = false;
    }

    public LottoStatusDto(LottoStatus lottoStatus) {
        this.round = lottoStatus.getRound();
        this.drawingDate = lottoStatus.getDrawingDate();
        this.totalSellAmount = lottoStatus.getTotalSellAmount();
        this.firstPrize = lottoStatus.getFirstPrize();
        this.firstWinnerCount = lottoStatus.getFirstWinnerCount();
        this.totalFirstPrize = lottoStatus.getTotalFirstPrize();
        this.winningNumber1 = lottoStatus.getWinningNumber1();
        this.winningNumber2 = lottoStatus.getWinningNumber2();
        this.winningNumber3 = lottoStatus.getWinningNumber3();
        this.winningNumber4 = lottoStatus.getWinningNumber4();
        this.winningNumber5 = lottoStatus.getWinningNumber5();
        this.winningNumber6 = lottoStatus.getWinningNumber6();
        this.bonusNumber = lottoStatus.getBonusNumber();
        this.valid = true;

    }
}

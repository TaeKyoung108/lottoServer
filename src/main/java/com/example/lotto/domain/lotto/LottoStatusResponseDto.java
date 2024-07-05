package com.example.lotto.domain.lotto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class LottoStatusResponseDto {
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

}

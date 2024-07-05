package com.example.lotto.controller.lotto;

import com.example.lotto.domain.lotto.LottoStatus;
import com.example.lotto.domain.lotto.LottoStatusDto;
import com.example.lotto.domain.lotto.LottoStatusResponseDto;
import com.example.lotto.service.LottoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


@RestController
@RequestMapping("/lotto")
@RequiredArgsConstructor
@Slf4j
public class LottoController {

    private final LottoService lottoService;

    /**
     * 1부터 45까지의 당첨 횟수를 return
     * 각 LottoNumber 는 number :x value: x의 당첨 횟수
     * 이때 includeBonus 가 참이면 보너스 숫자도 포함
     * @param includeBonus
     * @return
     */
    @GetMapping("/getNumbers")
    public List<LottoNumber> getLottoNumbers(@RequestParam(defaultValue = "false") Boolean includeBonus) {
        List<LottoNumber> lottoNumbers = new ArrayList<>();
        for (int i = 1; i <= 45; i++) {
            LottoNumber lottoNumber = new LottoNumber();
            lottoNumber.setNumber(i);
            lottoNumber.setValue(lottoService.countOccurrencesOfNumber(i, includeBonus)); // 임의의 값 설정
            lottoNumbers.add(lottoNumber);
        }
        return lottoNumbers;
    }

    /**
     * 특정 값만 가져옴
     * @param id
     * @return
     */
    @GetMapping("/getRound/{id}")
    public LottoStatusDto getTargetRound(@PathVariable Long id){
        Long lastRound = lottoService.CurrentRound()-1;
        if (lastRound>=id && id>0){
            LottoStatus target = lottoService.findOne(id);
            LottoStatusDto result = new LottoStatusDto(target);
            return result;
        }
        else {
            // 일단 현재는 없는 값 접근시 값만 비어있는 형태로 반환됨
            LottoStatusDto result = new LottoStatusDto(id);
            return result;
        }
    }

    /**
     * 모든 결과 리턴
     * @return
     */
    @GetMapping("/getRounds")
    public List<LottoStatusDto> getRounds(){
        List<LottoStatus> findAll = lottoService.findAll();
        List<LottoStatusDto> result = findAll.stream().map(one -> new LottoStatusDto(one)).toList();

        return result;
    }


    /**
     * 현재 미사용
     * api로 호출해서 가져오는거라 많은 요청 발생시 문제 요지 있음.
     * @param round
     * @param numbers
     * @return
     */
    @ResponseBody
//    @GetMapping("/getApiScore{round}q{numbers}")
    public String getApiScore(@PathVariable String round,@PathVariable String numbers){

        //입력된 숫자가 제대로 들어왔는지
        if (isValidNumber(round, 4)&&isValidNumber(numbers, 12)){
            String url = "https://m.dhlottery.co.kr/qr.do?method=winQr&v="+round+"q"+numbers;

            try {
                // HTML 페이지 가져오기
                Document doc = Jsoup.connect(url).get();

                // 특정 클래스 이름을 가진 요소 선택
                Elements elementsWithClass = doc.getElementsByClass("key_clr1");

                if (elementsWithClass.size() >= 2) {
                    // 두 번째 요소 선택
                    String secondElement = elementsWithClass.get(1).text();
                    String numberOnly = extractNumber(secondElement);

                    return numberOnly;
                } else {
                    return "0";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return "유효하지 않은 입력입니다.";
        }
        return "try문 실패";

    }

    @ResponseBody
    @GetMapping("/getApiScore{round}q{numbers}")
    public String getApiScoreV2(@PathVariable String round,@PathVariable String numbers){

        //입력된 숫자가 제대로 들어왔는지
        if (isValidNumber(round, 4)&&isValidNumber(numbers, 12)){
            String LOTTO_RESULT_URL = "https://dhlottery.co.kr/gameResult.do?method=byWin&drwNo=";

            try {
                // HTML 페이지 가져오기
                Document doc = Jsoup.connect(LOTTO_RESULT_URL).get();

                // tbody 요소 선택
                Elements tbodyElements = doc.select("tbody");

                for (Element tbody : tbodyElements) {
                    System.out.println(tbody);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return "유효하지 않은 입력입니다.";
        }
        return "try문 완료";

    }



    /**
     * 가장 최근 당첨 회차를 return
     * @return
     */
    @GetMapping("/getLastRound")
    public Long getLastRound(){
        return lottoService.CurrentRound()-1;
    }

//    @GetMapping("/setLottoStatus")
    @PostConstruct
    public List<LottoStatus> setLottoStatus(){
        String LOTTO_API_URL = "https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=";
        String LOTTO_RESULT_URL = "https://dhlottery.co.kr/gameResult.do?method=byWin&drwNo=";
        RestTemplate restTemplate = new RestTemplate();

        /**
         * 회차별 정보 저장하는 부분
         */
        // StringHttpMessageConverter를 추가하여 text/html 응답을 처리할 수 있도록 설정
        restTemplate.setMessageConverters(Collections.singletonList(new StringHttpMessageConverter(StandardCharsets.UTF_8)));

        ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper
        objectMapper.registerModule(new JavaTimeModule());

        Long lastRoundNumber = lottoService.CurrentRound()-1;

        List<Integer> missedRounds = lottoService.findMissedRound(1l, lastRoundNumber);

        log.info("가장 최근 추첨 회차 "+lastRoundNumber);
        for (int missedRound : missedRounds) {
            log.info("missed : "+missedRound);
            String url = LOTTO_API_URL + missedRound;
            String response = restTemplate.getForObject(url, String.class); // 응답을 String으로 받음

            try {
                // String 응답을 JSON으로 파싱하여 LottoStatusDto 객체로 변환
                LottoStatusResponseDto lottoStatusResponseDto = objectMapper.readValue(response, LottoStatusResponseDto.class);
                lottoService.fetchAndInsertData(lottoStatusResponseDto);
//                log.info("lotto "+lottoStatusDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<Long> missedSecondThird = lottoService.findMissedSecondThird();
        for (long missedRound : missedSecondThird) {
            String url = LOTTO_RESULT_URL + missedRound;
            try {
                // HTML 페이지 가져오기
                Document doc = Jsoup.connect(url).get();

                // tbody 요소 선택
                Element tbody = doc.select("tbody").first();

                if (tbody != null) {
                    // 모든 tr 요소 선택
                    Elements rows = tbody.select("tr");

                    // 2등과 3등의 당첨금을 저장할 변수
                    String secondPrize = "";
                    String thirdPrize = "";

                    // 각 행(tr)을 순회하면서 데이터 추출
                    for (Element row : rows) {
                        Elements cells = row.select("td");
                        if (cells.size() > 3) {
                            String rank = cells.get(0).text();
                            String prize = cells.get(3).text();

                            if (rank.equals("2등")) {
                                secondPrize = prize;
                            } else if (rank.equals("3등")) {
                                thirdPrize = prize;
                            }
                        }
                    }

                    LottoStatus lottoStatus = lottoService.saveSecondThird( missedRound, Long.parseLong(extractNumber(secondPrize)), Long.parseLong(extractNumber(thirdPrize)));
//                    System.out.println(lottoStatus);

                } else {
                    log.info("회차 " + missedRound + " - tbody 요소를 찾을 수 없습니다.");
                }

            } catch (IOException e) {
                log.info("회차 " + missedRound + " - 페이지를 가져오는 중 오류가 발생했습니다.");
                e.printStackTrace();
            }
        }


        return lottoService.findAll();
    }



    /**
     * 숫자만 걸러내기 위한 메서드
     * @param text
     * @return
     */
    private static String extractNumber(String text) {
        // 숫자를 제외한 문자 모두 제거
        String numberOnly = text.replaceAll("[^0-9]", "");
        return numberOnly;
    }

    //숫자의 자리가 n 자리인지 검증하는것
    public boolean isValidNumber(String round, int number) {
        // 숫자인지 확인
        if (!round.matches("\\d+")) {
            System.out.println("안맞음 "+round+" 1번");
            return false;
        }

        // 4자리 숫자인지 확인
        if (round.length() != number) {
            System.out.println("안맞음 "+round+" 2번");
            return false;
        }

        return true;
    }


}

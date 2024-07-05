package com.example.lotto.repository;

import com.example.lotto.domain.lotto.LottoStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LottoRepository {

    private final EntityManager em;

    public void save(LottoStatus lottoStatus) {
        em.persist(lottoStatus);
    }

    public LottoStatus findOne(Long round) {
        return em.find(LottoStatus.class, round);
    }
    public List<LottoStatus> findAll(){
        return em.createQuery("select ls from LottoStatus ls").getResultList();
    }

    /**
     * 각 숫자의 당첨 횟수 알수 있음. includeBonus 참이면 보너스 숫자도 포함
     * @param targetNumber
     * @param includeBonus
     * @return
     */
    public int countOccurrencesOfNumber(int targetNumber, boolean includeBonus) {
        String bonusNumberCondition = includeBonus ? " + SUM(CASE WHEN ls.bonusNumber = :targetNumber THEN 1 ELSE 0 END)" : "";
        Query query = em.createQuery(
                "SELECT " +
                        "SUM(CASE WHEN ls.winningNumber1 = :targetNumber THEN 1 ELSE 0 END) + " +
                        "SUM(CASE WHEN ls.winningNumber2 = :targetNumber THEN 1 ELSE 0 END) + " +
                        "SUM(CASE WHEN ls.winningNumber3 = :targetNumber THEN 1 ELSE 0 END) + " +
                        "SUM(CASE WHEN ls.winningNumber4 = :targetNumber THEN 1 ELSE 0 END) + " +
                        "SUM(CASE WHEN ls.winningNumber5 = :targetNumber THEN 1 ELSE 0 END) + " +
                        "SUM(CASE WHEN ls.winningNumber6 = :targetNumber THEN 1 ELSE 0 END) " +
                        bonusNumberCondition +
                        "FROM LottoStatus ls"
        );
        query.setParameter("targetNumber", targetNumber);
        List<Long> resultList = query.getResultList();
        return resultList.get(0).intValue();
    }

    /**
     * 없는 숫자 확인하기 위해 db에서 모든 리스트 가져옴
     * @return
     */
    public List<Long> findMissedRound(){
        Query query = em.createQuery("select ls.id from LottoStatus ls");
        List<Long> resultList = query.getResultList();

        return resultList;
    }

    /**
     * 2등 3등 금액 추가
     * @param round
     * @param second
     * @param third
     * @return
     */
    public LottoStatus saveSecondThird(Long round,Long second, Long third){
        LottoStatus lottoStatus = em.find(LottoStatus.class, round);
        lottoStatus.setSecondPrize(second);
        lottoStatus.setThirdPrize(third);

        return lottoStatus;
    }
    /**
     * repository 내에 2등, 3등 금액이 없는 애들 조회
     */
    public List<Long> findMissedSecondThird(){
        Query query = em.createQuery("SELECT ls.id FROM LottoStatus ls WHERE ls.secondPrize = 0 OR ls.thirdPrize = 0");
        List<Long> resultList = query.getResultList();

        return resultList;
    }

}

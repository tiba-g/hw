package tbag.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tbag.homework.model.History;
import tbag.homework.model.enums.HistoryType;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {

    @Query(value = "select h " +
            "from History h " +
            "inner join fetch h.issuerAccount ia " +
            "left join fetch h.beneficiaryAccount ba " +
            "inner join fetch ia.owner " +
            "left join fetch ba.owner " +
            "where ia.id = ?1 and h.historyType in ?2")
    List<History> selectByIssuerAccountIdAndHistoryType(int accountId, List<HistoryType> historyTypes);

}

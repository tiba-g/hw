package tbag.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tbag.homework.model.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {

}

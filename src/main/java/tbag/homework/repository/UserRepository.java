package tbag.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tbag.homework.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}

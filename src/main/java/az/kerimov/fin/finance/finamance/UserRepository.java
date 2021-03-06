package az.kerimov.fin.finance.finamance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByLoginAndPassword(String login, String password);
}

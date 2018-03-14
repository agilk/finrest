package az.kerimov.fin.finance.finamance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByLogin(String login);
    User findById(Integer id);
    User findByLoginAndPassword(String login, String password);
}

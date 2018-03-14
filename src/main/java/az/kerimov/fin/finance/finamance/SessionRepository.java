package az.kerimov.fin.finance.finamance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    Session findBySessionKeyAndActiveIsTrue(String sessionKey);
    List<Session> findAllByUserAndActiveIsTrue(User user);
}

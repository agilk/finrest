package az.kerimov.fin.finance.finamance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCurrencyRepository extends JpaRepository<UserCurrency, Integer>{
    List<UserCurrency> findAllByUserAndActiveIsTrue(User user);
    List<UserCurrency> findAllByUserAndActiveIsTrueAndDefaultElementIsTrue(User user);
    UserCurrency findByUserAndCurrency(User user, Currency currency);
}

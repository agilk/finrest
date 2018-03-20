package az.kerimov.fin.finance.finamance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RateRepository extends JpaRepository<Rate, Integer>{
    Rate findByCurrencyAndCtime(UserCurrency currency, String ctime);
    List<Rate> findByCurrencyAndCtimeIsLessThanEqual(UserCurrency currency, String ctime);


}

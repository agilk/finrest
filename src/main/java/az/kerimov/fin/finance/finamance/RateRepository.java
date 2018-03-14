package az.kerimov.fin.finance.finamance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RateRepository extends JpaRepository<Rate, Integer>{
    Rate findByCurrencyAndCtime(Currency currency, String ctime);
    List<Rate> findByCurrencyAndCtimeIsLessThanEqual(Currency currency, String ctime);
    List<Rate> findByCtime(String ctime);


}

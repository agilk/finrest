package az.kerimov.fin.finance.finamance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
    List<Currency> findAllByIdGreaterThan(Integer id);
    Currency findByCode(String code);
    Currency findByShortDescription(String shortDescription);
}

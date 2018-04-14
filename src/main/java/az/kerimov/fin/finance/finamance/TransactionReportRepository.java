package az.kerimov.fin.finance.finamance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface TransactionReportRepository extends JpaRepository<TransactionReport, Integer> {
    List<TransactionReport> findAllByUserOrderByIdDesc(User user);
}

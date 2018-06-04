package az.kerimov.fin.finance.finamance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryVRepository extends JpaRepository<SubCategoryV, Integer>{
    List<SubCategoryV> findByUserAndCategoryAndActiveIsTrueOrderByTransactionsDesc(User user, Category category);

}

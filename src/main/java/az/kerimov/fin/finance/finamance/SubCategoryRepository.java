package az.kerimov.fin.finance.finamance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Integer>{
    List<SubCategory> findByUserAndCategoryAndActiveIsTrue(User user, Category category);
    SubCategory findByIdAndUser(Integer id, User user);
}

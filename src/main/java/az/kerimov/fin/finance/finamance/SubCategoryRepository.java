package az.kerimov.fin.finance.finamance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Integer>{
    public List<SubCategory> findByUserAndCategory(User user, Category category);

    public SubCategory findById(Integer id);
}

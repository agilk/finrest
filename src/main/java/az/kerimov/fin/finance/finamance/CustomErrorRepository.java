package az.kerimov.fin.finance.finamance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomErrorRepository extends JpaRepository<CustomError, Integer>{
    CustomError findByExceptionAndLang(String exception, String lang);
    CustomError findByExceptionAndParamsAndLang(String exception, String params, String lang);
}

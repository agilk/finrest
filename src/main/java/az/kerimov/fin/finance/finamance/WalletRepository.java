package az.kerimov.fin.finance.finamance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer>{
    List<Wallet> findByUser(User user);
    List<Wallet> findAllByUserAndDefaultElementIsTrue(User user);
    Wallet findByIdAndUser(Integer id, User user);
    Wallet findById(Integer id);
}

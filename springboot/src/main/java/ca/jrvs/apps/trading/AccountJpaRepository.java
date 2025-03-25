package ca.jrvs.apps.trading;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountJpaRepository extends JpaRepository<Account, Integer> {
    Account getAccountByTraderId(Integer traderId);
}

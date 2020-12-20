package team16.bankpaymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team16.bankpaymentservice.model.Bank;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {

    @Query(value = "select * from bank b where b.code = ?1", nativeQuery = true)
    Bank findByBankCode(String bankCode);
}

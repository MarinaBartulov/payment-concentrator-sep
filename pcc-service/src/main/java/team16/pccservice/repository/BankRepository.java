package team16.pccservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team16.pccservice.model.Bank;

public interface BankRepository extends JpaRepository<Bank, Long> {

    @Query(value = "select * from bank b where b.code = ?1", nativeQuery = true)
    Bank findByBankCode(String bankCode);
}

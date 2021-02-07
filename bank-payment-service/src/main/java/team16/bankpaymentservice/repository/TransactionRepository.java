package team16.bankpaymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team16.bankpaymentservice.model.Transaction;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Transaction findTransactionByAcquirerOrderId(Long acquirerOrderId);
    Transaction findTransactionByMerchantOrderId(Long merchnetOrderId);

    @Query(value = "select * from transaction t where t.status in (0,1)", nativeQuery = true)
    List<Transaction> findUnfinishedTransactions();
}

package team16.bitcoinservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team16.bitcoinservice.model.Transaction;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    @Query(value="FROM Transaction t where t.status = 'NEW' OR t.status = 'PENDING' OR t.status = 'CONFIRMING'")
    List<Transaction> findUnfinishedTransactions();

    Transaction findByOrderId(Long orderId);
}

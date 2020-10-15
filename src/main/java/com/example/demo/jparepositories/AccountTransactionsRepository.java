package com.example.demo.jparepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.AccountTransactions;

public interface AccountTransactionsRepository extends JpaRepository<AccountTransactions, Long>{

}

package com.example.demo.jparepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.ATMAccountDetails;

public interface ATMAccountDetailsRepository extends JpaRepository<ATMAccountDetails, Long>{
	@Query("select count(*) from ATMAccountDetails a where a.debitCardNumber=:debitCardNumber and a.code=:code and a.bank=:bank")
	int getAccountDetails(@Param("debitCardNumber") String debitCardNumber, @Param("code") String code, @Param("bank") String bank);
}

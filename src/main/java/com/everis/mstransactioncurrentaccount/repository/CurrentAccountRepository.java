package com.everis.mstransactioncurrentaccount.repository;


import com.everis.mstransactioncurrentaccount.entity.CurrentAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CurrentAccountRepository extends ReactiveMongoRepository<CurrentAccount, String> {

}

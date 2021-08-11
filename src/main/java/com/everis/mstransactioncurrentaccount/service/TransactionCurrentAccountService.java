package com.everis.mstransactioncurrentaccount.service;

import com.everis.mstransactioncurrentaccount.entity.TransactionCurrentAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionCurrentAccountService {
    Mono<TransactionCurrentAccount> create(TransactionCurrentAccount t);

    Flux<TransactionCurrentAccount> findAll();

    Mono<TransactionCurrentAccount> findById(String id);

    Mono<TransactionCurrentAccount> update(TransactionCurrentAccount t);

    Mono<Boolean> delete(String t);

    Flux<TransactionCurrentAccount> findByCurrentAccountIdCustomer(String id);
}

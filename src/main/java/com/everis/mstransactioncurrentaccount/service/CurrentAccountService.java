package com.everis.mstransactioncurrentaccount.service;

import com.everis.mstransactioncurrentaccount.entity.CurrentAccount;
import com.everis.mstransactioncurrentaccount.entity.TransactionCurrentAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrentAccountService {
    Mono<CurrentAccount> create(CurrentAccount t);

    Mono<CurrentAccount> findById(String id);

    Flux<CurrentAccount> findAll();

    Mono<CurrentAccount> update(CurrentAccount t);

}

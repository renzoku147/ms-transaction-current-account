package com.everis.mstransactioncurrentaccount.service.impl;

import com.everis.mstransactioncurrentaccount.entity.TransactionCurrentAccount;
import com.everis.mstransactioncurrentaccount.repository.TransactionCurrentAccountRepository;
import com.everis.mstransactioncurrentaccount.service.TransactionCurrentAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionCurrentAccountServiceImpl implements TransactionCurrentAccountService {
    @Autowired
    TransactionCurrentAccountRepository transactionCurrentAccountRepository;

    @Override
    public Mono<TransactionCurrentAccount> create(TransactionCurrentAccount t) {
        return transactionCurrentAccountRepository.save(t);
    }

    @Override
    public Flux<TransactionCurrentAccount> findAll() {
        return transactionCurrentAccountRepository.findAll();
    }

    @Override
    public Mono<TransactionCurrentAccount> findById(String id) {
        return transactionCurrentAccountRepository.findById(id);
    }

    @Override
    public Mono<TransactionCurrentAccount> update(TransactionCurrentAccount t) {
        return transactionCurrentAccountRepository.save(t);
    }

    @Override
    public Mono<Boolean> delete(String t) {
        return transactionCurrentAccountRepository.findById(t)
                .flatMap(ca -> transactionCurrentAccountRepository.delete(ca).then(Mono.just(Boolean.TRUE)))
                .defaultIfEmpty(Boolean.FALSE);
    }

    @Override
    public Flux<TransactionCurrentAccount> findByCurrentAccountIdCustomer(String id){
        return transactionCurrentAccountRepository.findByCurrentAccount_Id(id);
    };
}

package com.everis.mstransactioncurrentaccount.service.impl;

import com.everis.mstransactioncurrentaccount.entity.CurrentAccount;
import com.everis.mstransactioncurrentaccount.entity.TransactionCurrentAccount;
import com.everis.mstransactioncurrentaccount.repository.CurrentAccountRepository;
import com.everis.mstransactioncurrentaccount.service.CurrentAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CurrentAccountServiceImpl implements CurrentAccountService {
    @Autowired
    CurrentAccountRepository currentAccountRepository;

    @Override
    public Mono<CurrentAccount> create(CurrentAccount t) {
        return currentAccountRepository.save(t);
    }

    @Override
    public Mono<CurrentAccount> findById(String id) {
        return currentAccountRepository.findById(id);
    }

    @Override
    public Flux<CurrentAccount> findAll() {
        return currentAccountRepository.findAll();
    }

    @Override
    public Mono<CurrentAccount> update(CurrentAccount t) {
        if(t.getBalance() < 0) throw new RuntimeException("Operacion Invalida");
        log.info("REPOSITORY Nuevo balance >> " + t.getBalance());
        return currentAccountRepository.save(t);
    }

}

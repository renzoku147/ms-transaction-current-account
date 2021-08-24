package com.everis.mstransactioncurrentaccount.service.impl;

import com.everis.mstransactioncurrentaccount.entity.CurrentAccount;
import com.everis.mstransactioncurrentaccount.entity.TransactionCurrentAccount;
import com.everis.mstransactioncurrentaccount.repository.TransactionCurrentAccountRepository;
import com.everis.mstransactioncurrentaccount.service.TransactionCurrentAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionCurrentAccountServiceImpl implements TransactionCurrentAccountService {

    WebClient webClient = WebClient.create("http://localhost:8887/ms-current-account/current/currentAccount");

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
    public Mono<Long> countMovements(String t) {
        return transactionCurrentAccountRepository.findByCurrentAccountId(t).count();
    }

    @Override
    public Mono<CurrentAccount> findSavingAccountById(String id) {
        return webClient.get().uri("/find/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CurrentAccount.class);
    }

    @Override
    public Mono<CurrentAccount> updateSavingAccount(CurrentAccount sa) {
        return webClient.put().uri("/update", sa.getId())
                .accept(MediaType.APPLICATION_JSON)
                .syncBody(sa)
                .retrieve()
                .bodyToMono(CurrentAccount.class);
    }

    @Override
    public Flux<TransactionCurrentAccount> findByCurrentAccountCardNumber(String cardNumber) {
        return transactionCurrentAccountRepository.findByCurrentAccountCardNumber(cardNumber);
    }

}

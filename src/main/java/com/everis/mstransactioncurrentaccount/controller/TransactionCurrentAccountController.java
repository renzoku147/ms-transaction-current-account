package com.everis.mstransactioncurrentaccount.controller;

import com.everis.mstransactioncurrentaccount.entity.CurrentAccount;
import com.everis.mstransactioncurrentaccount.entity.Customer;
import com.everis.mstransactioncurrentaccount.entity.TransactionCurrentAccount;
import com.everis.mstransactioncurrentaccount.service.CurrentAccountService;
import com.everis.mstransactioncurrentaccount.service.TransactionCurrentAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RefreshScope
@RestController
@RequestMapping("/transaction")
@Slf4j
public class TransactionCurrentAccountController {

    WebClient webClient = WebClient.create("http://localhost:8009/currentAccount");

    @Autowired
    TransactionCurrentAccountService transactionCurrentAccountService;

    @Autowired
    CurrentAccountService currentAccountService;

    @GetMapping("list")
    public Flux<TransactionCurrentAccount> findAll(){
        return transactionCurrentAccountService.findAll();
    }

    @PutMapping("/prueba")
    public Mono<CurrentAccount> prueba(@RequestBody CurrentAccount ca){
        return  webClient.put().uri("/update", ca.getId())
                .accept(MediaType.APPLICATION_JSON)
                .syncBody(ca)
                .retrieve()
                .bodyToMono(CurrentAccount.class);
    }

    @GetMapping("/find/{id}")
    public Mono<TransactionCurrentAccount> findById(@PathVariable String id){
        return transactionCurrentAccountService.findById(id);
    }

    @GetMapping("/findAllCustomer/{id}")
    public Flux<TransactionCurrentAccount> findAllTransactionByCustomer(@PathVariable String id){
        return transactionCurrentAccountService.findByCurrentAccountIdCustomer(id);
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<TransactionCurrentAccount>> create(@RequestBody TransactionCurrentAccount transaction){
        // BUSCO LA CUENTA CORRIENTE PARA VERIFICAR EL BALANCE ACTUAL ANTES DE LA TRANSACCION
        Mono<CurrentAccount> currentAccount = webClient.get().uri("/find/{id}", transaction.getCurrentAccount().getId())
                                    .accept(MediaType.APPLICATION_JSON)
                                    .retrieve()
                                    .bodyToMono(CurrentAccount.class);

        return currentAccount.flatMap(ca -> {
                                            switch (transaction.getTypeTransaction()){
                                                case DEPOSIT: ca.setBalance(ca.getBalance() + transaction.getTransactionAmount());
                                                    return webClient.put().uri("/update")
                                                            .accept(MediaType.APPLICATION_JSON)
                                                            .syncBody(ca)
                                                            .retrieve()
                                                            .bodyToMono(CurrentAccount.class); // Mono<CurrentAccount>
                                                case DRAFT: ca.setBalance(ca.getBalance() - transaction.getTransactionAmount());
                                                    return webClient.put().uri("/update")
                                                            .accept(MediaType.APPLICATION_JSON)
                                                            .syncBody(ca)
                                                            .retrieve()
                                                            .bodyToMono(CurrentAccount.class); // Mono<CurrentAccount>
                                                default: return Mono.empty();
                                            }
                 }).flatMap(ca -> {
                    transaction.setCurrentAccount(ca);
                    transaction.setTransactionDateTime(LocalDateTime.now());
                    return transactionCurrentAccountService.create(transaction); // Mono<TransactionCurrentAccount>
                })
                .map(tca -> new ResponseEntity<>(tca, HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }

    @PutMapping("/update")
    public Mono<ResponseEntity<TransactionCurrentAccount>> update(@RequestBody TransactionCurrentAccount transaction) {
        return transactionCurrentAccountService.findById(transaction.getId())
                .flatMap(tcaDB -> webClient.get().uri("/find/{id}", transaction.getCurrentAccount().getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .retrieve()
                                .bodyToMono(CurrentAccount.class)
                                .flatMap(ca -> {
                                    switch (transaction.getTypeTransaction()){
                                        case DEPOSIT: ca.setBalance(ca.getBalance() - tcaDB.getTransactionAmount() + transaction.getTransactionAmount());
                                            return webClient.put().uri("/update")
                                                    .accept(MediaType.APPLICATION_JSON)
                                                    .syncBody(ca)
                                                    .retrieve()
                                                    .bodyToMono(CurrentAccount.class).flatMap(caUpdate -> {
                                                                                                transaction.setCurrentAccount(caUpdate);
                                                                                                transaction.setTransactionDateTime(LocalDateTime.now());
                                                                                                return transactionCurrentAccountService.create(transaction); // Mono<TransactionCurrentAccount>
                                                                                            }); // Mono<CurrentAccount>
                                        case DRAFT: ca.setBalance(ca.getBalance() + tcaDB.getTransactionAmount() - transaction.getTransactionAmount());
                                            return webClient.put().uri("/update")
                                                    .accept(MediaType.APPLICATION_JSON)
                                                    .syncBody(ca)
                                                    .retrieve()
                                                    .bodyToMono(CurrentAccount.class).flatMap(caUpdate -> {
                                                                                                transaction.setCurrentAccount(caUpdate);
                                                                                                transaction.setTransactionDateTime(LocalDateTime.now());
                                                                                                return transactionCurrentAccountService.create(transaction); // Mono<TransactionCurrentAccount>
                                                                                            }); // Mono<CurrentAccount>
                                        default: return Mono.empty();
                                    }
                                })
                )
                .map(tca -> new ResponseEntity<>(tca, HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        // BUSCO LA TRANSACCION ORIGINAL
//        TransactionCurrentAccount tca = transactionCurrentAccountService.findById(c.getId()).block();
//
//        // BUSCO LA CUENTA CORRIENTE PARA VERIFICAR EL BALANCE ACTUAL ANTES DE LA TRANSACCION
//        CurrentAccount ca = currentAccountService.findById(c.getCurrentAccount().getId()).block();
//
//        switch (c.getTypeTransaction()){
//                        // LE QUITO LA CANTIDAD ORIGINAL Y SUMAMOS EL NUEVO MONTO DEL DEPOSITO
//            case DEPOSIT:   ca.setBalance(ca.getBalance() - tca.getTransactionAmount() + c.getTransactionAmount());
//                currentAccountService.update(ca).subscribe(); // ACTUALIZAMOS EL BALANCE DE LA CUENTA
//                break;
//                        // DESHACEMOS EL RETIRO ORIGINAL Y LO ACTUALIZAMOS POR MONTO DEL RETIRO NUEVO
//            case DRAFT:     ca.setBalance(ca.getBalance() + tca.getTransactionAmount() - c.getTransactionAmount());
//                currentAccountService.update(ca).subscribe(); // ACTUALIZAMOS EL BALANCE DE LA CUENTA
//                break;
//
//        }

//        return transactionCurrentAccountService.update(c)
//                .map(savedCustomer -> new ResponseEntity<>(savedCustomer, HttpStatus.CREATED))
//                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
        return transactionCurrentAccountService.delete(id)
                .filter(deleteCustomer -> deleteCustomer)
                .map(deleteCustomer -> new ResponseEntity<>("Transaction Deleted", HttpStatus.ACCEPTED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}

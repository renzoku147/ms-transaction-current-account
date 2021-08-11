//package com.everis.mstransactioncurrentaccount.controller;
//
//import com.everis.mstransactioncurrentaccount.entity.CurrentAccount;
//import com.everis.mstransactioncurrentaccount.entity.TransactionCurrentAccount;
//import com.everis.mstransactioncurrentaccount.service.CurrentAccountService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@RestController
//@RequestMapping("/currentAccount")
//@Slf4j
//public class CurrentAccountController {
//
//    WebClient webClient = WebClient.create("http://localhost:8009/currentAccount");
//
//    @Autowired
//    CurrentAccountService currentAccountService;
//
//    @GetMapping("/prueba")
//    public Flux<CurrentAccount> prueba(){
//
//        return webClient.get().uri("/list")
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .bodyToFlux(CurrentAccount.class)
//                .log("retrieve emps::");
//    }
//
//    @PostMapping("/create")
//    public Mono<ResponseEntity<CurrentAccount>> create(@RequestBody CurrentAccount c){
//        return currentAccountService.create(c)
//                .map(savedCustomer -> new ResponseEntity<>(savedCustomer , HttpStatus.CREATED))
//                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    @GetMapping("/list")
//    public Flux<CurrentAccount> findAll(){
//        return currentAccountService.findAll();
//    }
//
//    @GetMapping("/balanceAccount/{id}")
//    public Mono<CurrentAccount> findById(@PathVariable String id){
//        return currentAccountService.findById(id);
//    }
//
//}

package com.everis.mstransactioncurrentaccount.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CurrentAccount {
    private String id;

    private Customer customer;

    private String cardNumber;

    private List<Person> holders;

    private List<Person> signers;

    private Integer freeTransactions;

    private Double commissionTransactions;

    private Double commissionMaintenance;

    private Double balance;

    private LocalDateTime date;
}

package com.everis.mstransactioncurrentaccount.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CurrentAccount {
    @Id
    private String id;

    @NotNull
    private Customer customer;

    @NotNull
    private List<Person> holders;

    private List<Person> signers;

    @NotNull
    private Double maintenanceCommission;

    @NotNull
    private Double balance;
}

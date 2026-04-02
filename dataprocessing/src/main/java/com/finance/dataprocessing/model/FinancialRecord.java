package com.finance.dataprocessing.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class FinancialRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Double amount;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RecordType type;

    @NotBlank
    private String category;
    @NotNull
    private LocalDate date;
    private String notes;
    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

}

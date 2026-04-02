package com.finance.dataprocessing.dto;

import com.finance.dataprocessing.model.RecordType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrendDTO {
    private String period;
    @Enumerated(EnumType.STRING)  // ← add this
    @NotNull
    private RecordType type;
    private Double amount;
}

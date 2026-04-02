package com.finance.dataprocessing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryTotalDTO {
    private String category;
    private Double total;
}

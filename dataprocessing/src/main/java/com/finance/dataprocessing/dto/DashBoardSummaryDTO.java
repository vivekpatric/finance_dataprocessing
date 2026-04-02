package com.finance.dataprocessing.dto;

import com.finance.dataprocessing.model.FinancialRecord;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DashBoardSummaryDTO {
    private Double totalIncome;
    private Double totalExpense;
    private Double netBalance;
    private List<CategoryTotalDTO> categoryTotals;
    private List<FinancialRecord> recentActivity;
    private List<TrendDTO> monthlyTrends;
    private List<TrendDTO> weeklyTrends;
}

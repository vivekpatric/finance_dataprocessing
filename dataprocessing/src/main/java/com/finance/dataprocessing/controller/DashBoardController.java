package com.finance.dataprocessing.controller;


import com.finance.dataprocessing.dto.CategoryTotalDTO;
import com.finance.dataprocessing.dto.DashBoardSummaryDTO;
import com.finance.dataprocessing.dto.TrendDTO;
import com.finance.dataprocessing.model.FinancialRecord;
import com.finance.dataprocessing.model.RecordType;
import com.finance.dataprocessing.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dashboard")
public class DashBoardController {

    @Autowired
    private RecordRepository repo;

    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    @GetMapping("/summary")
    public DashBoardSummaryDTO summary(){

        Double income =repo.totalIncome();
        Double expense = repo.totalExpense();

        double totalIncome = income == null ? 0.0: income;
        double totalExpense = expense == null ? 0.0: expense;

        List<CategoryTotalDTO> categoryTotals = repo.totalByCategory()
                .stream()
                .map(row -> new CategoryTotalDTO(
                        (String) row[0],
                        (Double) row[1]
                ))
                .collect(Collectors.toList());
        List<FinancialRecord> recentActivity = repo.findTop10ByOrderByDateDesc();

        List<TrendDTO> monthlyTrends = repo.monthlyTrends()
                .stream()
                .map(row -> {
                    int year = ((Number) row[1]).intValue();
                    int month =((Number) row[0]).intValue();

                    String period  = String.format("%d-%02d",year,month);
                    RecordType type = (RecordType) row[2];
                    Double amount = ((Number) row[3]).doubleValue();
                    return new TrendDTO(period,type,amount);

                })
                .collect(Collectors.toList());

        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        List<TrendDTO> weeklyTrends = repo.weeklyTrends(sevenDaysAgo)
                .stream()
                .map(row ->{
                    String period = row[0].toString();
                    RecordType type = (RecordType) row[1];
                    Double amount =((Number) row[2]).doubleValue();
                    return new TrendDTO(period,type,amount);
                })
                .collect(Collectors.toList());

        return new DashBoardSummaryDTO(
                totalIncome,
                totalExpense,
                totalIncome - totalExpense,
                categoryTotals,
                recentActivity,
                monthlyTrends,
                weeklyTrends
        );

    }
}

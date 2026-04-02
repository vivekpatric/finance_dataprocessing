package com.finance.dataprocessing.repository;

import com.finance.dataprocessing.model.FinancialRecord;
import org.apache.catalina.LifecycleState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface  RecordRepository extends JpaRepository<FinancialRecord,Long> {

    List<FinancialRecord> findByType(String type);
    @Query("SELECT SUM(r.amount) FROM FinancialRecord r WHERE r.type='INCOME'")
    Double totalIncome();
    @Query("SELECT SUM(r.amount) FROM FinancialRecord r WHERE r.type='EXPENSE'")
    Double totalExpense();
    @Query("SELECT r.category,SUM(r.amount) FROM FinancialRecord r GROUP BY r.category")
    List<Object[]> totalByCategory();

    List<FinancialRecord> findTop10ByOrderByDateDesc();

    @Query("SELECT EXTRACT(YEAR FROM r.date),EXTRACT(MONTH FROM r.date), r.type, SUM(r.amount) " +
           "FROM FinancialRecord r "+
           "GROUP BY EXTRACT(YEAR FROM r.date), EXTRACT(MONTH FROM r.date), r.type "+
           "ORDER BY EXTRACT(YEAR FROM r.date), EXTRACT(MONTH FROM r.date)"
          )
    List<Object[]> monthlyTrends();
    @Query("SELECT r.date, r.type, SUM(r.amount) " +
         "FROM FinancialRecord r " +
         "WHERE r.date >= :startDate "+
         "GROUP BY r.date, r.type "+
         "ORDER BY r.date"
    )
    List<Object[]> weeklyTrends(@Param("startDate") LocalDate startDate);

}

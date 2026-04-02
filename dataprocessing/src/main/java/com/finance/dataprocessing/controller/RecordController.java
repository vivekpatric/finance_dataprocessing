package com.finance.dataprocessing.controller;

import com.finance.dataprocessing.model.FinancialRecord;
import com.finance.dataprocessing.model.User;
import com.finance.dataprocessing.repository.RecordRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/records")
public class RecordController {

    @Autowired
    private RecordRepository repo;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public FinancialRecord create(@Valid @RequestBody FinancialRecord record){
        return repo.save(record);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    @GetMapping
    public List<FinancialRecord> getAll(){
        return repo.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        repo.deleteById(id);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public FinancialRecord update(@PathVariable Long id, @RequestBody FinancialRecord record){
        FinancialRecord existing = repo.findById(id).orElseThrow();
        existing.setAmount(record.getAmount());
        existing.setType(record.getType());
        existing.setCategory(record.getCategory());
        existing.setDate(record.getDate());
        existing.setNotes(record.getNotes());
        return repo.save(existing);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    @GetMapping("/type/{type}")
    public List<FinancialRecord> getByType(@PathVariable String type){
        return repo.findByType(type);
    }


}

package com.expensia.repository.core;

import com.expensia.entity.core.Expense;
import com.expensia.entity.core.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    //get all transaction by user_id
    List<Expense> findAllByUserId(Long user_id);

    Page<Expense> findByUserId(Long userId, Pageable pageable);

    Long countByUserId(Long userId);
}

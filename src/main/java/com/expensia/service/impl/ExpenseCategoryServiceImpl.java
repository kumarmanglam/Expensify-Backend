package com.expensia.service.impl;

import com.expensia.entity.helper.ExpenseCategory;
import com.expensia.repository.helper.ExpenseCategoryRepository;
import com.expensia.service.ExpenseCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {

    private final ExpenseCategoryRepository expenseCategoryRepository;

    @Override
    public ExpenseCategory getExpenseCategoryById(Long expenseCategoryId) {
        return expenseCategoryRepository.findById(expenseCategoryId)
                .orElseThrow(() -> new NoSuchElementException("No expense category with matching id"));
    }

    // Implement other methods if needed
}

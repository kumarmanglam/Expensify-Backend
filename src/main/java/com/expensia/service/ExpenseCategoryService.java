package com.expensia.service;

import com.expensia.entity.helper.ExpenseCategory;

public interface ExpenseCategoryService {
    ExpenseCategory getExpenseCategoryById(Long expenseCategoryId);
}
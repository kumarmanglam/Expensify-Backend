package com.expensia.service;

import com.expensia.dto.core.ExpenseDto;
import com.expensia.dto.core.IncomeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ExpenseService {

    ExpenseDto addExpense(ExpenseDto expenseDto);
    ExpenseDto getExpenseById(Long expenseId);

    List<ExpenseDto> getAllExpense();

    ExpenseDto updateExpense(Long expenseId, ExpenseDto expenseDto);

    void deleteExpense(Long expenseId);

    List<ExpenseDto> getAllExpenseBySorting(String field, Sort.Direction sortOrder);

    Page<ExpenseDto> findAllWithPagination(int offSet, int pageSize);

    Page<ExpenseDto> findAllWithPaginationAndSorting(int offSet, int pageSize, String field, Sort.Direction sortOrder);
}

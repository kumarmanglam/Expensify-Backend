package com.expensia.service;

import com.expensia.dto.core.IncomeDto;
import com.expensia.entity.core.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface IncomeService {

    IncomeDto addIncome(IncomeDto incomeDto);

    IncomeDto getIncomeById(Long incomeId);

    List<IncomeDto> getAllIncome();

    List<IncomeDto> getAllIncomeBySorting(String field, Sort.Direction sortOrder);

    IncomeDto updateIncome(Long incomeId, IncomeDto incomeDto);

    void deleteIncome(Long incomeId);

    // Custom query method for pagination
    Page<IncomeDto> findAllWithPagination(int offSet, int pageSize);

    Page<IncomeDto> findAllWithPaginationAndSorting(int offSet, int pageSize, String field, Sort.Direction sortOrder);
}

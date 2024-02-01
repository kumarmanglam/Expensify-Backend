package com.expensia.repository.helper;

import com.expensia.dto.core.ExpenseDto;
import com.expensia.dto.helper.ExpenseCategoryDto;
import com.expensia.entity.helper.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {
    ExpenseCategory findByName(String name);
    Boolean existsByName(String name);
}

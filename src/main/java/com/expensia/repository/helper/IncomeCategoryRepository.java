package com.expensia.repository.helper;

import com.expensia.entity.helper.IncomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeCategoryRepository extends JpaRepository<IncomeCategory, Long> {
    Boolean existsByName(String name);
    IncomeCategory findByName(String name);
}
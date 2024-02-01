package com.expensia.repository.helper;

import com.expensia.entity.helper.InvestmentCategory;
import com.expensia.repository.core.InvestmentRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestmentCategoryRepository extends JpaRepository<InvestmentCategory, Long> {
    InvestmentCategory findByName(String name);
    Boolean existsByName(String name);
}

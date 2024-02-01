package com.expensia.service.impl;

import com.expensia.entity.helper.InvestmentCategory;
import com.expensia.repository.helper.InvestmentCategoryRepository;
import com.expensia.service.InvestmentCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class InvestmentCategoryServiceImpl implements InvestmentCategoryService {

    private final InvestmentCategoryRepository investmentCategoryRepository;

    @Override
    public InvestmentCategory getInvestmentCategoryById(Long investmentCategoryId) {
        return investmentCategoryRepository.findById(investmentCategoryId)
                .orElseThrow(() -> new NoSuchElementException("No investment category with matching id"));
    }
}
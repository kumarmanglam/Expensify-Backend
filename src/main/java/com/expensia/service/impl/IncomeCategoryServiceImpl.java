package com.expensia.service.impl;

import com.expensia.entity.helper.IncomeCategory;
import com.expensia.repository.helper.IncomeCategoryRepository;
import com.expensia.service.IncomeCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class IncomeCategoryServiceImpl implements IncomeCategoryService {

    private final IncomeCategoryRepository incomeCategoryRepository;

    @Override
    public IncomeCategory getIncomeCategoryById(Long incomeCategoryId) {
        return incomeCategoryRepository.findById(incomeCategoryId)
                .orElseThrow(() -> new NoSuchElementException("No income category with matching id"));
    }
}
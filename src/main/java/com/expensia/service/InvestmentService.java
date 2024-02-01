package com.expensia.service;

import com.expensia.dto.core.ExpenseDto;
import com.expensia.dto.core.InvestmentDto;
import com.expensia.entity.core.Investment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;


public interface InvestmentService {

    InvestmentDto addInvestment(InvestmentDto investmentDto);

    InvestmentDto getInvestmentById(Long investmentId);

    List<InvestmentDto> getAllInvestments();

    InvestmentDto updateInvestment(Long investmentId, InvestmentDto investmentDto);

    void deleteInvestment(Long investmentId);

    List<InvestmentDto> getAllInvesmentBySorting(String field, Sort.Direction sortOrder);

    Page<InvestmentDto> findAllWithPagination(int offSet, int pageSize);

    Page<InvestmentDto> findAllWithPaginationAndSorting(int offSet, int pageSize, String field, Sort.Direction sortOrder);
}
package com.expensia.service.impl;

import com.expensia.dto.core.ExpenseDto;
import com.expensia.dto.core.IncomeDto;
import com.expensia.dto.core.InvestmentDto;
import com.expensia.dto.core.TransactionDto;
import com.expensia.entity.core.Income;
import com.expensia.entity.helper.ExpenseCategory;
import com.expensia.entity.helper.IncomeCategory;
import com.expensia.entity.helper.InvestmentCategory;
import com.expensia.repository.helper.ExpenseCategoryRepository;
import com.expensia.repository.helper.IncomeCategoryRepository;
import com.expensia.repository.helper.InvestmentCategoryRepository;
import com.expensia.service.ExpenseService;
import com.expensia.service.IncomeService;
import com.expensia.service.InvestmentService;
import com.expensia.service.TransactionService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final IncomeService incomeService;
    private final IncomeCategoryRepository incomeCategoryRepository;
    private final ExpenseService expenseService;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final InvestmentService investmentService;
    private final InvestmentCategoryRepository investmentCategoryRepository;
    private final ModelMapper modelMapper;
    @Override
    public TransactionDto addTransaction(TransactionDto transactionDto) {
        TransactionDto dto = null;
        switch (transactionDto.getType().toLowerCase()){
            case("income"):
                IncomeDto incomeDto = modelMapper.map(transactionDto, IncomeDto.class);
                incomeService.addIncome(incomeDto);
                dto = modelMapper.map(incomeDto, TransactionDto.class);
                // how to handle transactionCategory(which is string type) to IncomeCategory(which is like a Object type)
                // find the category as from incomeCategoryService and add it to incomeDto
//                IncomeCategory incomeCat = incomeCategoryRepository.findByName(transactionDto.getCategory());
//
//                if (incomeCat != null) {
//                    incomeDto.setCategory(incomeCat);
//                    incomeService.addIncome(incomeDto);
//                } else {
//                    // Handle the case where the category is not found
//                    // You can throw an exception, log a message, or take any appropriate action
//                    throw new RuntimeException("Income category not found for name: " + transactionDto.getCategory());
//                }
                break;
            case "expense":
                ExpenseDto expenseDto = modelMapper.map(transactionDto, ExpenseDto.class);
//                ExpenseCategory expenseCat = expenseCategoryRepository.findByName(transactionDto.getCategory());

                expenseService.addExpense(expenseDto);
                dto = modelMapper.map(expenseDto, TransactionDto.class);
//                if (expenseCat != null) {
//                    expenseDto.setCategory(expenseCat);
//                    expenseService.addExpense(expenseDto);
//                } else {
//                    throw new RuntimeException("Expense category not found for name: " + transactionDto.getCategory());
//                }
                break;
            case "investment":
                InvestmentDto investmentDto = modelMapper.map(transactionDto, InvestmentDto.class);

                investmentService.addInvestment(investmentDto);

                dto = modelMapper.map(investmentDto, TransactionDto.class);
//                InvestmentCategory investmentCat = investmentCategoryRepository.findByName(transactionDto.getCategory());
//
//                if (investmentCat != null) {
//                    investmentDto.setCategory(investmentCat);
//                    investmentService.addInvestment(investmentDto);
//                } else {
//                    throw new RuntimeException("Investment category not found for name: " + transactionDto.getCategory());
//                }
                break;
        }
        return dto;
    }

    @Override
    public TransactionDto getTransaction(Long id, String transactionType) {
        TransactionDto transactionDto = null;
        // here I want to check whether the id requested matches the current logged-in user.
        // Fetch the transaction from the repository based on its type
        switch (transactionType.toLowerCase()) {
            case "income":
                IncomeDto incomeDto = incomeService.getIncomeById(id);
                if (incomeDto != null) {
                    transactionDto = modelMapper.map(incomeDto, TransactionDto.class);
                    transactionDto.setCategory(incomeDto.getCategory());
                }
                break;
            case "expense":
                ExpenseDto expenseDto = expenseService.getExpenseById(id);
                if (expenseDto != null) {
                    transactionDto = modelMapper.map(expenseDto, TransactionDto.class);
                    transactionDto.setCategory(expenseDto.getCategory());
                }
                break;
            case "investment":
                InvestmentDto investmentDto = investmentService.getInvestmentById(id);
                if (investmentDto != null) {
                    transactionDto = modelMapper.map(investmentDto, TransactionDto.class);
//                    transactionDto.setCategory(investmentDto.getCategory().getName());
                }
                break;
            // Add additional cases for other transaction types if needed
        }
        return transactionDto;
    }

    @Override
    public List<TransactionDto> getAllTransactions() {
        // Here I want to only select transactions of current user
        List<TransactionDto> transactionDtoList = new ArrayList<>();

        List<IncomeDto> allIncome = incomeService.getAllIncome();
        transactionDtoList.addAll(allIncome.stream()
                .map(item -> {
                    TransactionDto tranObj = modelMapper.map(item, TransactionDto.class);
                    tranObj.setType("income");
                    return tranObj;
                })
                .collect(Collectors.toList()));


        List<InvestmentDto> allInvestments = investmentService.getAllInvestments();
        transactionDtoList.addAll(allInvestments.stream()
                .map(item -> {
                    TransactionDto tranObj = modelMapper.map(item, TransactionDto.class);
                    tranObj.setType("investment");
                    tranObj.setCategory(item.getCategory());
                    return tranObj;
                })
                .collect(Collectors.toList()));

        List<ExpenseDto> allExpense = expenseService.getAllExpense();
        transactionDtoList.addAll(allExpense.stream()
                .map(item -> {
                    TransactionDto tranObj = modelMapper.map(item, TransactionDto.class);
                    tranObj.setType("expense");
                    return tranObj;
                })
                .collect(Collectors.toList()));

        return transactionDtoList;
    }

    @Override
    public TransactionDto updateTransaction(TransactionDto transactionDto) {
        TransactionDto dto = null;
        if(transactionDto.getType().toLowerCase() != transactionDto.get__destroyInfo().getType().toLowerCase()){
            System.out.println("change detected");
            //delete the record from __destroyInfo and add new record according to new type
            deleteTransaction(transactionDto.get__destroyInfo().getId(), transactionDto.get__destroyInfo().getType().toLowerCase());
            TransactionDto transactionDto1 = addTransaction(transactionDto);
            return transactionDto1;
        }
        switch (transactionDto.getType().toLowerCase()){
            case("income"):
                IncomeDto incomeObj = incomeService.getIncomeById(transactionDto.getId());
                incomeObj.setAmount(transactionDto.getAmount());
                incomeObj.setDescription(transactionDto.getDescription());
                incomeObj.setDate(transactionDto.getDate());
                incomeObj.setCategory(transactionDto.getCategory());
                IncomeDto incomeDto = incomeService.updateIncome(transactionDto.getId(), incomeObj);
                dto = modelMapper.map(incomeDto, TransactionDto.class);
                dto.setCategory(incomeObj.getCategory());
                break;
            case "investment":
                InvestmentDto investmentObj = investmentService.getInvestmentById(transactionDto.getId());
                investmentObj.setAmount(transactionDto.getAmount());
                investmentObj.setDescription(transactionDto.getDescription());
                investmentObj.setDate(transactionDto.getDate());
                investmentObj.setCategory(transactionDto.getCategory());
                InvestmentDto updatedInvestmentDto = investmentService.updateInvestment(transactionDto.getId(), investmentObj);
                dto = modelMapper.map(updatedInvestmentDto, TransactionDto.class);
                dto.setCategory(investmentObj.getCategory());
                break;
            case "expense":
                ExpenseDto expenseObj = expenseService.getExpenseById(transactionDto.getId());
                expenseObj.setAmount(transactionDto.getAmount());
                expenseObj.setDescription(transactionDto.getDescription());
                expenseObj.setDate(transactionDto.getDate());
                expenseObj.setCategory(transactionDto.getCategory());
                ExpenseDto updatedExpenseDto = expenseService.updateExpense(transactionDto.getId(), expenseObj);
                dto = modelMapper.map(updatedExpenseDto, TransactionDto.class);
                dto.setCategory(expenseObj.getCategory());
                break;
        }
        if(dto == null){
            throw new RuntimeException("dto is null, could not update");
        }
        return dto;
    }

    @Override
    public void deleteTransaction(Long id, String type) {
        switch (type.toLowerCase()){
            case("income"):
                incomeService.deleteIncome(id);
                break;
            case("investment"):
                investmentService.deleteInvestment(id);
                break;
            case("expense"):
                expenseService.deleteExpense(id);
                break;
        }
    }
}

package com.expensia.service.impl;

import com.expensia.dto.core.ExpenseDto;
import com.expensia.dto.core.IncomeDto;
import com.expensia.entity.User;
import com.expensia.entity.core.Expense;
import com.expensia.entity.core.Income;
import com.expensia.entity.helper.ExpenseCategory;
import com.expensia.exception.ResourceNotFoundException;
import com.expensia.exception.ValidationException;
import com.expensia.repository.UserRepository;
import com.expensia.repository.core.ExpenseRepository;
import com.expensia.repository.helper.ExpenseCategoryRepository;
import com.expensia.service.ExpenseService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
    private ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final UserRepository userRepository;
    private ModelMapper modelMapper;

    @Override
    public ExpenseDto addExpense(ExpenseDto expenseDto) {
        if (expenseDto == null || expenseDto.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            // Handle the null case, throw an exception, or return an appropriate response
            throw new IllegalArgumentException("Expense object cannot be null");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsernameOrEmail(username,username)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with the given username"));

        // task to add the currect user is left which I will do later or the id will be saved from controller class
        Expense currentExpense = modelMapper.map(expenseDto, Expense.class);
        //here we have to find the user and add it to current object
        currentExpense.setUser(user);
        currentExpense.setCategory(expenseCategoryRepository.findByName(expenseDto.getCategory()));
        Expense saved = expenseRepository.save(currentExpense);
        ExpenseDto expenseDto1 = modelMapper.map(saved, ExpenseDto.class);
        expenseDto1.setCategory(currentExpense.getCategory().getName());
        return expenseDto1;
    }

    @Override
    public ExpenseDto getExpenseById(Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new NoSuchElementException("No expense obj with matching id"));
        ExpenseDto expenseDto = modelMapper.map(expense, ExpenseDto.class);
        expenseDto.setCategory(expense.getCategory().getName());
        return expenseDto;
    }

    @Override
    public List<ExpenseDto> getAllExpense() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsernameOrEmail(username,username)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with the given username"));

        List<Expense> expenses = expenseRepository.findAllByUserId(user.getId());
        return expenses.stream()
                .map((item) ->{
                    ExpenseDto expenseDto = modelMapper.map(item, ExpenseDto.class);
                    expenseDto.setCategory(item.getCategory().getName());
                    return expenseDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseDto updateExpense(Long expenseId, ExpenseDto expenseDto) {
        if (expenseDto == null ||
                expenseDto.getAmount() == null || expenseDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            // Handle the validation error, throw an exception, or return an appropriate response
            throw new ValidationException("Expense name cannot be null or empty or amount zero or less than 0");
        }
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new NoSuchElementException("No expense obj with matching id"));
        expense.setAmount(expenseDto.getAmount());
        expense.setDate(expenseDto.getDate());
        expense.setDescription(expenseDto.getDescription());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsernameOrEmail(username,username)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with the given username"));


        expense.setCategory(expenseCategoryRepository.findByName(expenseDto.getCategory()));

        Expense save = expenseRepository.save(expense);
        ExpenseDto expenseDto1 = modelMapper.map(save, ExpenseDto.class);
        expenseDto1.setCategory(expense.getCategory().getName());
        return expenseDto1;
    }

    @Override
    public void deleteExpense(Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new NoSuchElementException("No expense obj with matching id"));
        expenseRepository.delete(expense);
    }

    @Override
    public List<ExpenseDto> getAllExpenseBySorting(String field, Sort.Direction sortOrder) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
//        System.out.println("the username is: " + username);
        User user = userRepository.findByUsernameOrEmail(username,username)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with the given username"));

        List<Expense> expenses = expenseRepository.findAllByUserId(user.getId());// Apply sorting based on the provided parameters
        expenses.sort((income1, income2) -> {
            switch (field){
                case("amount"):
                    return income1.getAmount().compareTo(income2.getAmount());
                case("date"):
                    return income1.getDate().compareTo(income2.getDate());
                case("description"):
                    return income1.getDescription().toLowerCase().compareTo(income2.getDescription().toLowerCase());
                default:
                    return 0;
            }
        });

        return expenses.stream()
                .map(item ->{
                    ExpenseDto expenseDto = modelMapper.map(item, ExpenseDto.class);
                    expenseDto.setCategory(item.getCategory().getName());
                    return expenseDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<ExpenseDto> findAllWithPagination(int offSet, int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
//        System.out.println("the username is: " + username);
        User user = userRepository.findByUsernameOrEmail(username,username)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with the given username"));


        long totalRecords = expenseRepository.countByUserId(user.getId());

        if (offSet * pageSize > totalRecords) {
            throw new IllegalArgumentException("Invalid combination of offset and pageSize, exceeds total records");
        }


        Pageable pageable = PageRequest.of(offSet, pageSize);
        Page<Expense> expensePage = expenseRepository.findByUserId(user.getId(), pageable);

        return expensePage.map(expense -> {
            ExpenseDto expenseDto = modelMapper.map(expense, ExpenseDto.class);
            expenseDto.setCategory(expense.getCategory().getName());
            return expenseDto;
        });
    }

    @Override
    public Page<ExpenseDto> findAllWithPaginationAndSorting(int offSet, int pageSize, String field, Sort.Direction sortOrder) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
//        System.out.println("the username is: " + username);
        User user = userRepository.findByUsernameOrEmail(username,username)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with the given username"));

        Sort sort = Sort.by(sortOrder, field);
        Pageable pageable = PageRequest.of(offSet, pageSize, sort);
        Page<Expense> expensePage = expenseRepository.findByUserId(user.getId(), pageable);

        return expensePage.map(expense -> {
            ExpenseDto expenseDto = modelMapper.map(expense, ExpenseDto.class);
            expenseDto.setCategory(expense.getCategory().getName());
            return expenseDto;
        });
    }
}


//The ExpenseService validates and handles null checks for input parameters, such as expense and expenseDto, ensuring proper amount and name conditions.
//The service utilizes ModelMapper for entity-DTO conversion and performs CRUD operations using the ExpenseRepository.

//In controller how to handle these exception while using service methods.
//In the controller, use try-catch blocks to handle specific exceptions, returning appropriate HTTP responses with error messages.
//Employ @ExceptionHandler methods for targeted exception handling, customizing responses based on the exception type.
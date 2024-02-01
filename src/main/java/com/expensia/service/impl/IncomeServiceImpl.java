package com.expensia.service.impl;

import com.expensia.dto.core.IncomeDto;
import com.expensia.entity.User;
import com.expensia.entity.core.Income;
import com.expensia.entity.helper.IncomeCategory;
import com.expensia.exception.ResourceNotFoundException;
import com.expensia.repository.UserRepository;
import com.expensia.repository.core.IncomeRepository;
import com.expensia.repository.helper.IncomeCategoryRepository;
import com.expensia.service.IncomeService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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
public class IncomeServiceImpl implements IncomeService {
    private final IncomeRepository incomeRepository;
    private final IncomeCategoryRepository incomeCategoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public IncomeDto addIncome(IncomeDto incomeDto) {
        if (incomeDto == null || incomeDto.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Income amount must be non-negative");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
//        System.out.println("the username is: " + username);
        User user = userRepository.findByUsernameOrEmail(username,username)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with the given username"));

        Income currentIncome = modelMapper.map(incomeDto, Income.class);
        currentIncome.setUser(user);
        currentIncome.setCategory(incomeCategoryRepository.findByName(incomeDto.getCategory()));

        //here we have to find the user and add it to current object
        Income save = incomeRepository.save(currentIncome);
        IncomeDto incomeDto1 = modelMapper.map(save, IncomeDto.class);
        incomeDto1.setCategory(currentIncome.getCategory().getName());

        return incomeDto1;
    }

    @Override
    public IncomeDto getIncomeById(Long incomeId) {
        Income income = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new NoSuchElementException("No income object with matching id"));
        IncomeDto incomeDto = modelMapper.map(income, IncomeDto.class);
        incomeDto.setCategory(income.getCategory().getName());
        return incomeDto;
    }

    @Override
    public List<IncomeDto> getAllIncome() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
//        System.out.println("the username is: " + username);
        User user = userRepository.findByUsernameOrEmail(username,username)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with the given username"));

        List<Income> incomes = incomeRepository.findAllByUserId(user.getId());
        return incomes.stream()
                .map(item ->{
                    IncomeDto incomeDto = modelMapper.map(item, IncomeDto.class);
                    incomeDto.setCategory(item.getCategory().getName());
                    return incomeDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<IncomeDto> getAllIncomeBySorting(String sortField, Sort.Direction sortOrder) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
//        System.out.println("the username is: " + username);
        User user = userRepository.findByUsernameOrEmail(username,username)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with the given username"));

        List<Income> incomes = incomeRepository.findAllByUserId(user.getId()); // Apply sorting based on the provided parameters
        incomes.sort((income1, income2) -> {
            switch (sortField){
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

        return incomes.stream()
                .map(item ->{
                    IncomeDto incomeDto = modelMapper.map(item, IncomeDto.class);
                    incomeDto.setCategory(item.getCategory().getName());
                    return incomeDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<IncomeDto> findAllWithPagination(int offSet, int pageSize){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
//        System.out.println("the username is: " + username);
        User user = userRepository.findByUsernameOrEmail(username,username)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with the given username"));

        long totalRecords = incomeRepository.countByUserId(user.getId());

        if (offSet * pageSize > totalRecords) {
            throw new IllegalArgumentException("Invalid combination of offset and pageSize, exceeds total records");
        }

        Pageable pageable = PageRequest.of(offSet, pageSize);
        Page<Income> incomePage = incomeRepository.findByUserId(user.getId(), pageable);

//        return incomePage.getContent().stream().map((item) => modelMapper.map(item, IncomeDto.class));
        return incomePage.map(income -> {
            IncomeDto incomeDto = modelMapper.map(income, IncomeDto.class);
            incomeDto.setCategory(income.getCategory().getName());
            return incomeDto;
        });
    }

    @Override
    public Page<IncomeDto> findAllWithPaginationAndSorting(int offSet, int pageSize, String field, Sort.Direction sortOrder) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
//        System.out.println("the username is: " + username);
        User user = userRepository.findByUsernameOrEmail(username,username)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with the given username"));

        Sort sort = Sort.by(sortOrder, field);
        Pageable pageable = PageRequest.of(offSet, pageSize, sort);
        Page<Income> incomePage = incomeRepository.findByUserId(user.getId(), pageable);

        return incomePage.map(income -> {
            IncomeDto incomeDto = modelMapper.map(income, IncomeDto.class);
            incomeDto.setCategory(income.getCategory().getName());
            return incomeDto;
        });
    }

    @Override
    public IncomeDto updateIncome(Long incomeId, IncomeDto incomeDto) {
        if (incomeDto == null ||
                incomeDto.getAmount() == null || incomeDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("IncomeDto object cannot be null or name cannot be null");
        }
        Income income = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new NoSuchElementException("No income object with matching id"));
        income.setAmount(incomeDto.getAmount());
        income.setDate(incomeDto.getDate());
        IncomeCategory byName = incomeCategoryRepository.findByName(incomeDto.getCategory());
        income.setCategory(byName);
        income.setDescription(incomeDto.getDescription());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsernameOrEmail(username,username)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with the given username"));
        income.setUser(user);

        Income save = incomeRepository.save(income);
        System.out.println(save.getDescription());
        IncomeDto incomeDto1 = modelMapper.map(save, IncomeDto.class);
        incomeDto1.setCategory(income.getCategory().getName());

        return incomeDto1;
    }

    @Override
    public void deleteIncome(Long incomeId) {
        Income income = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new NoSuchElementException("No income object with matching id"));
        incomeRepository.delete(income);
    }
}

package com.expensia.service.impl;

import com.expensia.dto.core.ExpenseDto;
import com.expensia.dto.core.InvestmentDto;
import com.expensia.entity.User;
import com.expensia.entity.core.Expense;
import com.expensia.entity.core.Income;
import com.expensia.entity.core.Investment;
import com.expensia.entity.helper.InvestmentCategory;
import com.expensia.exception.ResourceNotFoundException;
import com.expensia.repository.UserRepository;
import com.expensia.repository.core.InvestmentRepository;
import com.expensia.repository.helper.InvestmentCategoryRepository;
import com.expensia.service.InvestmentService;
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
public class InvestmentServiceImpl implements InvestmentService {
    private final InvestmentRepository investmentRepository;
    private final InvestmentCategoryRepository investmentCategoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public InvestmentDto addInvestment(InvestmentDto investmentDto) {
        validateInvestment(investmentDto);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        User user = userRepository.findByUsernameOrEmail(name, name)
                .orElseThrow(() -> new ResourceNotFoundException("no user found with given username"));

        Investment currentInvestment = modelMapper.map(investmentDto, Investment.class);

        //here we have to find the user and add it to current object
        currentInvestment.setUser(user);
        currentInvestment.setCategory(investmentCategoryRepository.findByName(investmentDto.getCategory()));
        Investment save = investmentRepository.save(currentInvestment);

        InvestmentDto investmentDto1 = modelMapper.map(save, InvestmentDto.class);
        investmentDto1.setCategory(currentInvestment.getCategory().getName());
        return investmentDto1;
    }

    @Override
    public InvestmentDto getInvestmentById(Long investmentId) {
        Investment investment = investmentRepository.findById(investmentId)
                .orElseThrow(() -> new NoSuchElementException("No investment object with matching id"));
        InvestmentDto investmentDto = modelMapper.map(investment, InvestmentDto.class);
        investmentDto.setCategory(investment.getCategory().getName());
        return investmentDto;
    }

    @Override
    public List<InvestmentDto> getAllInvestments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("the username is: " + username);
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with the given username"));

        List<Investment> investments = investmentRepository.findAllByUserId(user.getId());

        return investments.stream()
                .map(item -> {
                    InvestmentDto investmentDto = modelMapper.map(item, InvestmentDto.class);
                    investmentDto.setCategory(item.getCategory().getName());
                    return investmentDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public InvestmentDto updateInvestment(Long investmentId, InvestmentDto investmentDto) {
        validateInvestmentDto(investmentDto);
        Investment investment = investmentRepository.findById(investmentId)
                .orElseThrow(() -> new NoSuchElementException("No investment object with matching id"));
        investment.setAmount(investmentDto.getAmount());
        investment.setDate(investmentDto.getDate());
        investment.setDescription(investmentDto.getDescription());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        User user = userRepository.findByUsernameOrEmail(name, name)
                .orElseThrow(() -> new ResourceNotFoundException("no user found with given username"));

        InvestmentCategory categoryObj = investmentCategoryRepository.findByName(investmentDto.getCategory());
        investment.setCategory(categoryObj);
        investment.setUser(user);
        Investment save = investmentRepository.save(investment);
        InvestmentDto investmentDto1 = modelMapper.map(save, InvestmentDto.class);
        investmentDto1.setCategory(investment.getCategory().getName());

        return investmentDto;
    }

    @Override
    public void deleteInvestment(Long investmentId) {
        Investment investment = investmentRepository.findById(investmentId)
                .orElseThrow(() -> new NoSuchElementException("No investment object with matching id"));
        try {
            investmentRepository.delete(investment);
        } catch (Exception e) {
            // Handle the exception, log it, or take appropriate action
            e.printStackTrace();
            throw new RuntimeException("Failed to delete investment object");
        }
    }

    @Override
    public List<InvestmentDto> getAllInvesmentBySorting(String field, Sort.Direction sortOrder) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
//        System.out.println("the username is: " + username);
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with the given username"));

        List<Investment> investments = investmentRepository.findAllByUserId(user.getId());// Apply sorting based on the provided parameters
        investments.sort((income1, income2) -> {
            switch (field) {
                case ("amount"):
                    return income1.getAmount().compareTo(income2.getAmount());
                case ("date"):
                    return income1.getDate().compareTo(income2.getDate());
                case ("description"):
                    return income1.getDescription().toLowerCase().compareTo(income2.getDescription().toLowerCase());
                default:
                    return 0;
            }
        });
        return investments.stream()
                .map(item -> {
                    InvestmentDto investmentDto = modelMapper.map(item, InvestmentDto.class);
                    investmentDto.setCategory(item.getCategory().getName());
                    return investmentDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<InvestmentDto> findAllWithPagination(int offSet, int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        //System.out.println("the username is: " + username);
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with the given username"));

        long totalRecords = investmentRepository.countByUserId(user.getId());

        if (offSet * pageSize > totalRecords) {
            throw new IllegalArgumentException("Invalid combination of offset and pageSize, exceeds total records");
        }

        Pageable pageable = PageRequest.of(offSet, pageSize);
        Page<Investment> investmentPage = investmentRepository.findByUserId(user.getId(), pageable);

        return investmentPage.map(item -> {
            InvestmentDto investmentDto = modelMapper.map(item, InvestmentDto.class);
            investmentDto.setCategory(item.getCategory().getName());
            return investmentDto;
        });
    }

    @Override
    public Page<InvestmentDto> findAllWithPaginationAndSorting(int offSet, int pageSize, String field, Sort.Direction sortOrder) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
//        System.out.println("the username is: " + username);
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with the given username"));


        Sort sort = Sort.by(sortOrder, field);
        Pageable pageable = PageRequest.of(offSet, pageSize, sort);
        Page<Investment> investmentPage = investmentRepository.findByUserId(user.getId(), pageable);

        return investmentPage.map(item -> {
            InvestmentDto investmentDto = modelMapper.map(item, InvestmentDto.class);
            investmentDto.setCategory(item.getCategory().getName());
            return investmentDto;
        });
    }

    private void validateInvestment(Investment investment) {
        if (investment == null || investment.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Investment amount must be non-negative");
        }
    }

    private void validateInvestment(InvestmentDto investmentDto) {
        if (investmentDto == null || investmentDto.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Investment amount must be non-negative");
        }
    }

    private void validateInvestmentDto(InvestmentDto investmentDto) {
        if (investmentDto == null ||
                investmentDto.getAmount() == null || investmentDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("InvestmentDto object cannot be null, and name and amount must be valid");
        }
    }
}
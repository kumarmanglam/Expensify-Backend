package com.expensia.controller;

import com.expensia.dto.core.ExpenseDto;
import com.expensia.dto.core.IncomeDto;
import com.expensia.dto.core.InvestmentDto;
import com.expensia.repository.core.InvestmentRepository;
import com.expensia.repository.helper.InvestmentCategoryRepository;
import com.expensia.service.InvestmentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin("*")
@RestController
@RequestMapping("/expensia/investments")
@AllArgsConstructor
public class InvestmentController {
    private final InvestmentService investmentService;
    private final Logger logger = LoggerFactory.getLogger(InvestmentController.class);

    @PostMapping
    public ResponseEntity<Object> addInvestment(@RequestBody InvestmentDto investmentDto) {
        try {
            InvestmentDto createdInvestment = investmentService.addInvestment(investmentDto);
            logger.info("Investment added successfully: {}", createdInvestment);
            return new ResponseEntity<>(createdInvestment, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            logger.error("Invalid request: {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            logger.error("Internal server error", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getInvestment(@PathVariable("id") Long investmentId) {
        try {
            InvestmentDto investmentById = investmentService.getInvestmentById(investmentId);
            logger.info("Retrieved investment successfully: {}", investmentById);
            return new ResponseEntity<>(investmentById, HttpStatus.OK);
        } catch (NoSuchElementException noSuchElementException) {
            logger.warn("Resource not found: {}", investmentId);
            return new ResponseEntity<>("Resource not found", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.error("Internal server error", ex);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping
    public ResponseEntity<List<InvestmentDto>> getAllInvestments() {
        try {
            List<InvestmentDto> allInvestments = investmentService.getAllInvestments();
            return new ResponseEntity<>(allInvestments, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Internal server error while retrieving all investments", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sorted")
    public ResponseEntity<Object> getAllInvestmentBySort(@RequestParam("sortByField") String field, @RequestParam("orderBy") String orderBy) {
        try {
            Sort.Direction sortOrder = (orderBy.toLowerCase() == "asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            List<InvestmentDto> allInvesmentBySorting = investmentService.getAllInvesmentBySorting(field, sortOrder);
            return new ResponseEntity<>(allInvesmentBySorting, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error while getting data in Sorted order ", ex);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/paginate")
    public ResponseEntity<Object> getAllInvestmentPaginated(@RequestParam("offset") int offset, @RequestParam("pageSize") int pageSize) {
        try {
            Page<InvestmentDto> allWithPagination = investmentService.findAllWithPagination(offset, pageSize);
            return new ResponseEntity<>(allWithPagination, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            logger.error("Incorrect offset value ", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            logger.error("Error with paginating ", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/paginateAndSort")
    public ResponseEntity<Object> getAllInvestmentPaginateAndSort(@RequestParam("offset") int offset, @RequestParam("pageSize") int pageSize, @RequestParam("sortByField") String field, @RequestParam("orderBy") String orderBy) {
        try {
            Sort.Direction sortOrder = (orderBy.toLowerCase().equals("asc")) ? Sort.Direction.ASC : Sort.Direction.DESC;
            Page<InvestmentDto> allWithPaginationAndSorting = investmentService.findAllWithPaginationAndSorting(offset, pageSize, field, sortOrder);
            return new ResponseEntity<>(allWithPaginationAndSorting, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error with paginating and sorting ", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateInvestment(@PathVariable("id") Long id, @RequestBody InvestmentDto investmentDto) {
        try {
            InvestmentDto updatedInvestment = investmentService.updateInvestment(id, investmentDto);
            logger.info("Investment updated successfully: {}", updatedInvestment);
            return new ResponseEntity<>(updatedInvestment, HttpStatus.OK);
        } catch (IllegalArgumentException illegalArgumentException) {
            logger.error("Invalid request: {}", illegalArgumentException.getMessage());
            return new ResponseEntity<>(illegalArgumentException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException noSuchElementException) {
            logger.warn("Investment not found: {}", id);
            return new ResponseEntity<>("Investment not found", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.error("Internal server error", ex);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteInvestment(@PathVariable Long id) {
        try {
            investmentService.deleteInvestment(id);
            logger.info("Investment deleted successfully: {}", id);
            return new ResponseEntity<>("Investment deleted successfully", HttpStatus.OK);
        } catch (NoSuchElementException noSuchElementException) {
            logger.warn("Investment not found: {}", id);
            return new ResponseEntity<>("Investment not found", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.error("Internal server error", ex);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

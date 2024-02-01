package com.expensia.controller;

import com.expensia.dto.core.ExpenseDto;
import com.expensia.dto.core.IncomeDto;
import com.expensia.service.ExpenseService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin("*")
@RestController
@RequestMapping("/expensia/expenses")
@AllArgsConstructor
public class ExpenseController {
    private static final Logger logger = LoggerFactory.getLogger(IncomeController.class);

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDto> addExpense(@RequestBody ExpenseDto expenseDto) {
        try {
            ExpenseDto createdExpense = expenseService.addExpense(expenseDto);
            logger.info("Expense added successfully with id: {}", createdExpense.getId());
            return new ResponseEntity<>(createdExpense, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            logger.error("Failed to add expense. Invalid argument.", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            logger.error("Failed to add expense. Internal server error.", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ExpenseDto> getExpense(@PathVariable("id") Long expenseId) {
        try {
            ExpenseDto expenseById = expenseService.getExpenseById(expenseId);
            logger.info("Retrieved expense with id: {}", expenseId);
            return new ResponseEntity<>(expenseById, HttpStatus.OK);
        } catch (NoSuchElementException noSuchElementException) {
            logger.warn("Expense with id {} not found.", expenseId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.error("Failed to retrieve expense. Internal server error.", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping
    public ResponseEntity<List<ExpenseDto>> getAllExpense() {
        List<ExpenseDto> allExpense = expenseService.getAllExpense();
        logger.info("Retrieved all expenses. Count: {}", allExpense.size());
        return new ResponseEntity<>(allExpense, HttpStatus.OK);
    }

    @GetMapping("/sorted")
    public ResponseEntity<Object> getAllIncomeBySort(@RequestParam("sortByField") String field, @RequestParam("orderBy") String orderBy) {
        try {
            Sort.Direction sortOrder = (orderBy.toLowerCase() == "asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            List<ExpenseDto> allExpenseBySorting = expenseService.getAllExpenseBySorting(field, sortOrder);
            return new ResponseEntity<>(allExpenseBySorting, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error while getting data in Sorted order ", ex);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/paginate")
    public ResponseEntity<Object> getAllIncomePaginated(@RequestParam("offset") int offset, @RequestParam("pageSize") int pageSize) {
        try {
            Page<ExpenseDto> allWithPagination = expenseService.findAllWithPagination(offset, pageSize);
            return new ResponseEntity<>(allWithPagination, HttpStatus.OK);
        }catch (IllegalArgumentException ex){
            logger.error("Incorrect offset value ", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (Exception ex) {
            logger.error("Error with paginating ", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/paginateAndSort")
    public ResponseEntity<Object> getAllIncomePaginateAndSort(@RequestParam("offset") int offset, @RequestParam("pageSize") int pageSize, @RequestParam("sortByField") String field, @RequestParam("orderBy") String orderBy) {
        try {
            Sort.Direction sortOrder = (orderBy.toLowerCase().equals("asc")) ? Sort.Direction.ASC : Sort.Direction.DESC;
            Page<ExpenseDto> allWithPaginationAndSorting = expenseService.findAllWithPaginationAndSorting(offset, pageSize, field, sortOrder);
            return new ResponseEntity<>(allWithPaginationAndSorting, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error with paginating and sorting ", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<ExpenseDto> updateExpense(@PathVariable("id") Long id, @RequestBody ExpenseDto expenseDto) {
        try {
            ExpenseDto updatedExpense = expenseService.updateExpense(id, expenseDto);
            logger.info("Updated expense with id: {}", id);
            return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
        } catch (IllegalArgumentException illegalArgumentException) {
            logger.error("Failed to update expense. Invalid argument.", illegalArgumentException);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException noSuchElementException) {
            logger.warn("Expense with id {} not found for update.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.error("Failed to update expense. Internal server error.", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id) {
        try {
            expenseService.deleteExpense(id);
            logger.info("Deleted expense with id: {}", id);
            return new ResponseEntity<>("Expense deleted successfully", HttpStatus.OK);
        } catch (NoSuchElementException noSuchElementException) {
            logger.warn("Expense with id {} not found for deletion.", id);
            return new ResponseEntity<>("No such expense found", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.error("Failed to delete expense. Internal server error.", ex);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

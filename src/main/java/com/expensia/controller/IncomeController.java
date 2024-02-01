package com.expensia.controller;

import com.expensia.dto.core.IncomeDto;
import com.expensia.service.IncomeService;
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
@RequestMapping("/expensia/incomes")
@AllArgsConstructor
public class IncomeController {

    private static final Logger logger = LoggerFactory.getLogger(IncomeController.class);

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeDto> addIncome(@RequestBody IncomeDto incomeDto) {
        try {
            IncomeDto createdIncome = incomeService.addIncome(incomeDto);
            return new ResponseEntity<>(createdIncome, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            logger.error("Bad request while adding income: {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            logger.error("Internal server error while adding income", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<IncomeDto> getIncome(@PathVariable("id") Long incomeId) {
        try {
            IncomeDto incomeById = incomeService.getIncomeById(incomeId);
            return new ResponseEntity<>(incomeById, HttpStatus.OK);
        } catch (NoSuchElementException noSuchElementException) {
            logger.error("Income not found with id: {}", incomeId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.error("Internal server error while retrieving income", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<IncomeDto>> getAllIncome() {
        try {
            List<IncomeDto> allIncome = incomeService.getAllIncome();
            return new ResponseEntity<>(allIncome, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Internal server error while retrieving all incomes ", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sorted")
    public ResponseEntity<Object> getAllIncomeBySort(@RequestParam("sortByField") String field, @RequestParam("orderBy") String orderBy) {
        try {
            Sort.Direction sortOrder = (orderBy.toLowerCase() == "asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            List<IncomeDto> allIncomeBySorting = incomeService.getAllIncomeBySorting(field, sortOrder);
            return new ResponseEntity<>(allIncomeBySorting, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error while getting data in Sorted order ", ex);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/paginate")
    public ResponseEntity<Object> getAllIncomePaginated(@RequestParam("offset") int offset, @RequestParam("pageSize") int pageSize) {
        try {
            Page<IncomeDto> allWithPagination = incomeService.findAllWithPagination(offset, pageSize);
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
            System.out.println("the sort order figured out is  " + sortOrder);
            Page<IncomeDto> allWithPagination = incomeService.findAllWithPaginationAndSorting(offset, pageSize,field, sortOrder);
            return new ResponseEntity<>(allWithPagination, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error with paginating and sorting ", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("{id}")
    public ResponseEntity<IncomeDto> updateIncome(@PathVariable("id") Long id, @RequestBody IncomeDto incomeDto) {
        try {
            IncomeDto updatedIncome = incomeService.updateIncome(id, incomeDto);
            return new ResponseEntity<>(updatedIncome, HttpStatus.OK);
        } catch (IllegalArgumentException illegalArgumentException) {
            logger.error("Bad request while updating income: {}", illegalArgumentException.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException noSuchElementException) {
            logger.error("Income not found with id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.error("Internal server error while updating income", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteIncome(@PathVariable Long id) {
        try {
            incomeService.deleteIncome(id);
            return new ResponseEntity<>("Income deleted successfully", HttpStatus.OK);
        } catch (NoSuchElementException noSuchElementException) {
            logger.error("Income not found with id: {}", id);
            return new ResponseEntity<>("No such income found", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.error("Internal server error while deleting income", ex);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

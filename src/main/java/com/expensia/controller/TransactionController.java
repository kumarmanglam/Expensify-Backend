package com.expensia.controller;

import com.expensia.dto.core.TransactionDto;
import com.expensia.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/expensia/transactions")
@AllArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    @PostMapping
    public ResponseEntity<Object> addTransaction(@RequestBody TransactionDto transactionDto){
        try{
            TransactionDto transactionDto1 = transactionService.addTransaction(transactionDto);
            if(transactionDto1 == null){
                throw new RuntimeException("Resource could not be updated");
            }
            return new ResponseEntity<>(transactionDto1, HttpStatus.OK);
        }
        catch (RuntimeException re){
            re.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (Exception ex){
            System.out.println(ex);
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //http://localhost:8080/expensia/transactions/1?type=expense
    @GetMapping("{id}")
    public ResponseEntity<Object> getTransaction(@PathVariable("id") Long id, @RequestParam("type") String type){
        System.out.println("the id requested is " + id);
        System.out.println("the type requested is " + type);
        try {
            TransactionDto transactionDTO = transactionService.getTransaction(id, type);
            if (transactionDTO != null) {
                return new ResponseEntity<>(transactionDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Transaction not found", HttpStatus.NOT_FOUND);
            }
        }catch (Exception ex) {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //http://localhost:8080/expensia/transactions
    @GetMapping
    public ResponseEntity<Object> getAllTransaction(){
        try{
            List<TransactionDto> allTransactions = transactionService.getAllTransactions();
            return new ResponseEntity<>(allTransactions, HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //http://localhost:8080/expensia/transactions
    @PutMapping
    public ResponseEntity<Object> updateTransaction(@RequestBody TransactionDto transactionDto){
        try{
            TransactionDto transactionDto1 = transactionService.updateTransaction(transactionDto);
            if(transactionDto1 == null){
                throw new RuntimeException("return object is null");
            }
            return new ResponseEntity<>(transactionDto1, HttpStatus.OK);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //http://localhost:808/expensia/transactions/id?type=income
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteTransaction(@PathVariable("id") Long id, @RequestParam("type") String type){
        transactionService.deleteTransaction(id, type);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

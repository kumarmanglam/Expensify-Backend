package com.expensia.service;

import com.expensia.dto.core.TransactionDto;

import java.util.List;

public interface TransactionService {
    TransactionDto addTransaction(TransactionDto transactionDto);
    TransactionDto getTransaction(Long id, String transactionType);
    List<TransactionDto> getAllTransactions();
    TransactionDto updateTransaction(TransactionDto transactionDto);
    void deleteTransaction(Long id, String type);
}

package com.expensia.repository.core;

import com.expensia.entity.core.Expense;
import com.expensia.entity.core.Investment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    //find all by given user_id
    List<Investment> findAllByUserId(Long user_id);

    Page<Investment> findByUserId(Long userId, Pageable pageable);

    Long countByUserId(Long userId);
}
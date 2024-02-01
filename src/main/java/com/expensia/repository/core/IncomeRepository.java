package com.expensia.repository.core;

import com.expensia.entity.User;
import com.expensia.entity.core.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    // find all incomes of user_id
    List<Income> findAllByUserId(Long user_id);

    Page<Income> findByUserId(Long userId, Pageable pageable);

    Long countByUserId(Long userId);
}
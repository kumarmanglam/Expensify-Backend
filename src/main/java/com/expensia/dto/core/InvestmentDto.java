package com.expensia.dto.core;


import com.expensia.entity.helper.ExpenseCategory;
import com.expensia.entity.helper.IncomeCategory;
import com.expensia.entity.helper.InvestmentCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InvestmentDto {
    private Long id;

    private BigDecimal amount;
    private LocalDate date;
    private String category;

    private String description;
    private String type = "Investment";
}
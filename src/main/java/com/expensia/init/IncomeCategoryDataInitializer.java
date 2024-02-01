package com.expensia.init;

import com.expensia.entity.helper.IncomeCategory;
import com.expensia.repository.helper.IncomeCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class IncomeCategoryDataInitializer implements CommandLineRunner {
    private final IncomeCategoryRepository incomeCategoryRepository;

    @Override
    public void run(String... args) throws Exception {
        initiateIncomeCategories();
    }
    public void initiateIncomeCategories() {
        createCategoryIfNotExists("Salary");
        createCategoryIfNotExists("Investments");
        createCategoryIfNotExists("Dividends");
        createCategoryIfNotExists("Rental");
        createCategoryIfNotExists("Other");
        // Add more income categories as needed
    }

    public void createCategoryIfNotExists(String categoryName) {
        if (!incomeCategoryRepository.existsByName(categoryName)) {
            IncomeCategory category = new IncomeCategory();
            category.setName(categoryName);
            incomeCategoryRepository.save(category);
        }
    }
}

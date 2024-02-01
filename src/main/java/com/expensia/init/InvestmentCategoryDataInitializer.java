package com.expensia.init;

import com.expensia.entity.helper.InvestmentCategory;
import com.expensia.repository.helper.InvestmentCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InvestmentCategoryDataInitializer implements CommandLineRunner {
    private final InvestmentCategoryRepository investmentCategoryRepository;

    @Override
    public void run(String... args) throws Exception {
        initiateInvestmentCategories();
    }

    public void initiateInvestmentCategories() {
        createCategoryIfNotExists("Stocks");
        createCategoryIfNotExists("Mutual Funds");
        createCategoryIfNotExists("Bonds");
        createCategoryIfNotExists("Gold");
        createCategoryIfNotExists("Real estate");
        createCategoryIfNotExists("Other");
    }

    public void createCategoryIfNotExists(String categoryName) {
        if (!investmentCategoryRepository.existsByName(categoryName)) {
            InvestmentCategory category = new InvestmentCategory();
            category.setName(categoryName);
            investmentCategoryRepository.save(category);
        }
    }
}

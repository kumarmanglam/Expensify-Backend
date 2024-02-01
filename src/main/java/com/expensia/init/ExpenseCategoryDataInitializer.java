package com.expensia.init;


import com.expensia.entity.helper.ExpenseCategory;
import com.expensia.repository.helper.ExpenseCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ExpenseCategoryDataInitializer implements CommandLineRunner {
    private final ExpenseCategoryRepository expenseCategoryRepository;

    @Override
    public void run(String... args) throws Exception {
        initiateExpenseCategories();
    }

    public void initiateExpenseCategories() {
        createCategoryIfNotExists("Housing");
        createCategoryIfNotExists("Transportation");
        createCategoryIfNotExists("Food");
        createCategoryIfNotExists("Healthcare");
        createCategoryIfNotExists("Clothing");
        createCategoryIfNotExists("Utility");
        createCategoryIfNotExists("Other");
        // Add more expense categories as needed
    }

    public void createCategoryIfNotExists(String categoryName) {
        if (!expenseCategoryRepository.existsByName(categoryName)) {
            ExpenseCategory category = new ExpenseCategory();
            category.setName(categoryName);
            expenseCategoryRepository.save(category);
        }
    }
}

package model;

import error.ProductNotFoundException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductService {
    private final List<Product> products;

    public ProductService(List<Product> products) {
        this.products = products;
    }

    public List<Product> getExpensiveBooks() {
        return products.stream()
                .filter(p -> "Book".equals(p.getType()) && p.getPrice() > 250)
                .collect(Collectors.toList());
    }

    public List<Product> getDiscountedBooks() {
        return products.stream()
                .filter(p -> "Book".equals(p.getType()) && p.isDiscountAvailable())
                .map(p -> new Product(p.getType(), p.getPrice() * 0.9, true, p.getCreateDate()))
                .collect(Collectors.toList());
    }

    public Product getCheapestBook() throws ProductNotFoundException {
        return products.stream()
                .filter(p -> "Book".equals(p.getType()))
                .min(Comparator.comparing(Product::getPrice))
                .orElseThrow(() -> new ProductNotFoundException("Продукт з категорії Book не знайдено"));
    }

    public double calculateTotalCostOfRecentBooks() {
        LocalDate currentYear = LocalDate.now();
        return products.stream()
                .filter(p -> p.getCreateDate().getYear() == currentYear.getYear())
                .filter(p -> "Book".equals(p.getType()) && p.getPrice() <= 75)
                .mapToDouble(Product::getPrice)
                .sum();
    }

    public Map<String, List<Product>> groupProductsByType() {
        return products.stream()
                .collect(Collectors.groupingBy(Product::getType));
    }
}

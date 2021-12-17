package com.unipi.reactor.p08Batching;

import com.github.javafaker.Book;
import com.unipi.reactor.util.Util;
import lombok.Getter;
import lombok.ToString;
import reactor.core.publisher.Flux;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Lec03AssignmentBookOrder {

    public static void main(String[] args) {

        Set<String> allowedCategories = Set.of(
                "Science fiction",
                "Fantasy",
                "Suspense/Thriller"
        );

        bookStream()
                // Items should be in the allowedCategories List
                .filter(book -> allowedCategories.contains(book.getCategory()))
                // When 5 items have been collected
                .buffer(5) // Collect data every 5 items
                // Calculate the revenue per category using Stream Api
                .map(list -> revenueCalculator(list))
                .subscribe(Util.subscriber());

        Util.sleepSeconds(60);
    }

    // Publish a book sale every 200ms
    private static Flux<BookOrder> bookStream() {
        return Flux.interval(Duration.ofMillis(200))
                .map(i -> new BookOrder());
    }

    private static RevenueReport revenueCalculator(List<BookOrder> books) {
        Map<String, Double> map = books.stream()
                .collect(Collectors.groupingBy(
                        BookOrder::getCategory,
                        Collectors.summingDouble(BookOrder::getPrice)
                ));

        return new RevenueReport(map);
    }
}

@ToString
class RevenueReport {

    private final LocalDateTime localDateTime = LocalDateTime.now();
    private final Map<String, Double> revenue;

    public RevenueReport(Map<String, Double> revenue) {
        this.revenue = revenue;
    }
}

@Getter
@ToString
class BookOrder {

    private final String title;
    private final String author;
    private final String category;
    private final double price;

    public BookOrder() {
        Book book = Util.faker().book();
        this.title = book.title();
        this.author = book.author();
        this.category = book.genre();
        this.price = Double.parseDouble(price(0.0D, 100.0D));
    }

    // Fix to faker 1.0.2 Issue for not respecting setLocale
    private String price(double min, double max) {
        // double price = min + this.faker.random().nextDouble() * (max - min);
        double price = min + Util.faker().random().nextDouble() * (max - min);
        return (new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ENGLISH))).format(price);
    }
}
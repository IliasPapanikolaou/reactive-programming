package com.unipi.reactortest;

import com.github.javafaker.Book;
import com.unipi.reactor.util.Util;
import lombok.Getter;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Duration;
import java.util.Locale;

public class Lec04AssertTest {

    @Test
    public void test1() {

        Mono<BookOrder> mono = Mono.fromSupplier(() -> new BookOrder());

        // Step Verifier accepts a Publisher and subscribes to it internally
        StepVerifier.create(mono)
                .assertNext(b -> Assertions.assertNotNull(b.getAuthor()))
                .verifyComplete();
    }

    // Test delay time of response
    @Test
    public void test2() {

        Mono<BookOrder> mono = Mono.fromSupplier(() -> new BookOrder())
                        .delayElement(Duration.ofSeconds(3));

        // Step Verifier accepts a Publisher and subscribes to it internally
        StepVerifier.create(mono)
                .assertNext(b -> Assertions.assertNotNull(b.getAuthor()))
                .expectComplete()
                .verify(Duration.ofSeconds(4));
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

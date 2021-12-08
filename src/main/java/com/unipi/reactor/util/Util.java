package com.unipi.reactor.util;

import com.github.javafaker.Faker;

import java.time.Duration;
import java.util.function.Consumer;

public class Util {

    private static final Faker FAKER = Faker.instance();

    public static Consumer<Object> onNext() {
        return obj -> System.out.println("Received: " + obj);
    }

    public static Consumer<Throwable> onError() {
        return err -> System.out.println("Received: " + err.getMessage());
    }

    public static Runnable onComplete() {
        return () -> System.out.println("Completed");
    }

    public static Faker faker() {
        return FAKER;
    }

    public static void sleepSeconds(int seconds) {
        try {
            Thread.sleep(Duration.ofSeconds(seconds).toMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

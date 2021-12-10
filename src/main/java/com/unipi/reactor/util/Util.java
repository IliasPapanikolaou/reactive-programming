package com.unipi.reactor.util;

import com.github.javafaker.Faker;
import org.reactivestreams.Subscriber;

import java.time.Duration;
import java.util.Locale;
import java.util.function.Consumer;

public class Util {

    private static final Faker FAKER = Faker.instance(Locale.ENGLISH);

    public static Consumer<Object> onNext() {
        return obj -> System.out.println("Received: " + obj);
    }

    public static Consumer<Throwable> onError() {
        return err -> System.out.println("Error Received: " + err.getMessage());
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

    public static Subscriber<Object> subscriber() {
        return new DefaultSubscriber();
    }

    public static Subscriber<Object> subscriber(String name) {
        return new DefaultSubscriber(name);
    }
}

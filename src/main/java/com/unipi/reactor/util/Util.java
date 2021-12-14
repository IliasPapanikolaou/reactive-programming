package com.unipi.reactor.util;

import com.github.javafaker.Faker;
import org.reactivestreams.Subscriber;

import java.time.Duration;
import java.util.Locale;
import java.util.function.Consumer;

public class Util {

    private static final Faker FAKER = new Faker(new Locale("en"));

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

    public static void sleepMillis(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepSeconds(int seconds) {
        sleepMillis(seconds * 1000);
    }

    public static Subscriber<Object> subscriber() {
        return new DefaultSubscriber();
    }

    public static Subscriber<Object> subscriber(String name) {
        return new DefaultSubscriber(name);
    }
}

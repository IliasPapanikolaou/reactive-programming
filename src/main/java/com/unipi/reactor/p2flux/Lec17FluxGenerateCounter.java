package com.unipi.reactor.p2flux;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

public class Lec17FluxGenerateCounter {

    public static void main(String[] args) {

        // Terminate condition:
        // country == canada or max == 10 countries

        Flux.generate(
                () -> 1, // Provide initial state
                (counter, sink) -> {
                    String country = Util.faker().country().name();
                    System.out.println("Emitting: " + country);
                    sink.next(country);

                    if (counter >= 10 || country.equalsIgnoreCase("canada"))
                        sink.complete();
                    return counter + 1;
                }
        )
                // .take(5) // Take only five
                .subscribe(Util.subscriber());
    }
}

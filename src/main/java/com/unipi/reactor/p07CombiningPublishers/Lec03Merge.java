package com.unipi.reactor.p07CombiningPublishers;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec03Merge {

    public static void main(String[] args) {

        // We want to travel and want to find the best price
        Flux<String> merge = Flux.merge(
                Qatar.getFlights(),
                Emirates.getFlights(),
                AmericanAirlines.getFlights()
        );

        // Subscribe
        merge.subscribe(Util.subscriber());


        Util.sleepSeconds(60);
    }
}

// Three Flight Company Publishers which we will be merged in the above example
class Qatar {

    public static Flux<String> getFlights() {
        return Flux.range(1, Util.faker().random().nextInt(1,5)) // number of flights
                .delayElements(Duration.ofSeconds(1))
                .map(s -> "Qatar-" + Util.faker().random().nextInt(100, 999)) // flight number
                // If true, one then the result will be allowed
                .filter(s -> Util.faker().random().nextBoolean());
    }
}

class Emirates {

    public static Flux<String> getFlights() {
        return Flux.range(1, Util.faker().random().nextInt(1,10)) // number of flights
                .delayElements(Duration.ofSeconds(1))
                .map(s -> "Emirates-" + Util.faker().random().nextInt(100, 999)) // flight number
                // If true, one then the result will be allowed
                .filter(s -> Util.faker().random().nextBoolean());
    }
}

class AmericanAirlines {

    public static Flux<String> getFlights() {
        return Flux.range(1, Util.faker().random().nextInt(1,10)) // number of flights
                .delayElements(Duration.ofSeconds(1))
                .map(s -> "AA-" + Util.faker().random().nextInt(100, 999)) // flight number
                // If true, one then the result will be allowed
                .filter(s -> Util.faker().random().nextBoolean());
    }
}

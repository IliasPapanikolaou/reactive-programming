package com.unipi.reactor.p7CombiningPublishers;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Lec06Assignment {

    public static void main(String[] args) {

        // Initial car price
        final int carPrice = 10000;

        Flux.combineLatest(monthStream(), demandStream(),
                // (carPrice - price reduction by 100 every month) * demand factor
                (month, demand) -> (carPrice - (month * 100)) * demand)
                .subscribe(Util.subscriber());

        Util.sleepSeconds(20);
    }

    // Month Stream
    private static Flux<Long> monthStream() {
        // Every second is a month, Duration.Zero = Start (counting) immediately.
        return Flux.interval(Duration.ZERO, Duration.ofSeconds(1));
    }

    // Demand Stream - every quarter
    private static Flux<Double> demandStream() {
        return Flux.interval(Duration.ofSeconds(3))
                .map(i -> Util.faker().random().nextInt(80, 120) / 100D)
                .startWith(1D); // Initial value
    }
}

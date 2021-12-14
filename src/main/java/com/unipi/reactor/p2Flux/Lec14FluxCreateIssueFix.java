package com.unipi.reactor.p2Flux;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

public class Lec14FluxCreateIssueFix {

    public static void main(String[] args) {

        // Emit while
        // Only one instance of fluxSink
        Flux.create(fluxSink -> {

            String country;

            do {
                country = Util.faker().country().name();
                System.out.println("Emitting: " + country);
                fluxSink.next(country);
            }
            // While fluxSink is NOT cancelled by .take(3) method
            while (!country.equalsIgnoreCase("canada") && !fluxSink.isCancelled());

            fluxSink.complete();

        })
                .take(3) // The subscriber stops to receive data after the 3rd item
                // But the source continues to emit data - This is an issue because publisher keeps sending data.
                // we can fix this by putting fluxSink.isCancelled() in while loop condition.
                .subscribe(Util.subscriber());
    }
}

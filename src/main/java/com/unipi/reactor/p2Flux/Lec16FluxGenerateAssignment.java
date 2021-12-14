package com.unipi.reactor.p2Flux;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

public class Lec16FluxGenerateAssignment {

    public static void main(String[] args) {

        Flux.generate(synchronousSink -> {

            String country = Util.faker().country().name();
            System.out.println("Emitting " + country);
            synchronousSink.next(country);
            // Terminate condition
            if (country.equalsIgnoreCase("canada")) {
                synchronousSink.complete();
            }

        }).subscribe(Util.subscriber());
    }
}

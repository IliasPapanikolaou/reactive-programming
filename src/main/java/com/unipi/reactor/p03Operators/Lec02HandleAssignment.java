package com.unipi.reactor.p03Operators;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

public class Lec02HandleAssignment {

    public static void main(String[] args) {

        Flux.generate(synchronousSink ->
                synchronousSink.next(Util.faker().country().name()))
                .map(Object::toString)
                .handle((s, synchronousSink) -> {
                    // Emit first
                    synchronousSink.next(s);
                    // Then filter
                    if (s.equalsIgnoreCase("Greece"))
                        synchronousSink.complete();
                })
                .subscribe(Util.subscriber());
    }
}

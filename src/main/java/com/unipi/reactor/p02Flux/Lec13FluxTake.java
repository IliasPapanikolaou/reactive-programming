package com.unipi.reactor.p02Flux;

/*
* Take() Operator
* */

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

public class Lec13FluxTake {

    public static void main(String[] args) {

        // Take() after the 3rd item .cancel() the subscription
        Flux.range(1,10)
                .log() // Logging Operator
                .take(3) // Operator that takes 3 items out of 10
                .log()
                .subscribe(Util.subscriber());
    }
}

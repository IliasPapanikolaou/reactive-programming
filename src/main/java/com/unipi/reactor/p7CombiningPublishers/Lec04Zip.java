package com.unipi.reactor.p7CombiningPublishers;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

/*
* Zip: merges one item of each publisher.
* For example if there is a car production, and we have
* three publishers:
* 1. Makes Car bodies
* 2. Makes Engines
* 3. Make Wheels
*
* The zip() will combine 1 Car body with 1 engine and one set of wheels,
* even if the publisher of an individual part produces more
* items than the other two
*/
public class Lec04Zip {

    public static void main(String[] args) {

        Flux.zip(getBody(), getEngine(), getWheels())
                // If we want to access the individual items
                // .doOnNext(tuple3 -> {tuple3.getT1(); tuple3.getT2(); tuple3.getT3();})
                // Returns a tuple of three items
                .subscribe(Util.subscriber());

        // The result will be 2 cars because only 2 engines have created
    }

    // Producer of bodies (5 bodies)
    private static Flux<String> getBody() {
        return Flux.range(1, 5)
                .map(i -> "body");
    }

    // Producer of Engines (2 engines)
    private static Flux<String> getEngine() {
        return Flux.range(1, 2)
                .map(i -> "engine");
    }

    // Producer of Wheels (4 sets of wheels)
    private static Flux<String> getWheels() {
        return Flux.range(1, 4)
                .map(i -> "wheels");
    }
}


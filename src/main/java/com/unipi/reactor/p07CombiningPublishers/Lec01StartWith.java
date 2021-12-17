package com.unipi.reactor.p07CombiningPublishers;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

public class Lec01StartWith {

    public static void main(String[] args) {

        NameGenerator generator = new NameGenerator();
        // Subscriber 1
        generator.generateNames()
                .take(2)
                .subscribe(Util.subscriber("Sam"));

        // Subscriber 2
        generator.generateNames()
                .take(4)
                .subscribe(Util.subscriber("Mike"));

        // Subscriber 3 - Names that start with "A"
        generator.generateNames()
                .filter(n -> n.startsWith("A"))
                .take(2)
                .subscribe(Util.subscriber("Jake"));
    }
}

// Name generator publisher class
class NameGenerator {

    private List<String> list = new ArrayList<>();

    public Flux<String> generateNames() {
        // Publisher
        return Flux.generate(stringSynchronousSink -> {
                    System.out.println("Generated fresh");
                    // Intended delay in order to emulate process
                    Util.sleepSeconds(1);
                    String name = Util.faker().name().firstName();
                    // When a name is generated, cache it in the list
                    list.add(name);
                    stringSynchronousSink.next(name);
                })
                // First look the list (1st publisher) if already has items to emit
                // and then generates more items if needed from (2nd publisher)
                .cast(String.class)
                .startWith(getFromCache());
    }

    // Return the already cached names from the list
    private Flux<String> getFromCache() {
        return Flux.fromIterable(list);
    }
}

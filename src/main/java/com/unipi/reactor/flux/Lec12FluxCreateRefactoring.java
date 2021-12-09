package com.unipi.reactor.flux;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;
import java.util.stream.IntStream;

public class Lec12FluxCreateRefactoring {

    public static void main(String[] args) {

        // Refactoring the above - Best practice
        NameProducer nameProducer = new NameProducer();

        // Publisher
        Flux.create(nameProducer)
                // Subscriber
                .subscribe(Util.subscriber());

        // Invoke producer to produce data
        nameProducer.produce();

        // Different threads - FlexSink() is a thread safe method.
        // Runnable runnable = () -> nameProducer.produce();
        // or
        Runnable runnable = nameProducer::produce;

        // Fire 10 threads
        IntStream.rangeClosed(1, 10).forEach(i ->
                new Thread(runnable).start()
        );

        // Intended delay
        Util.sleepSeconds(2);
    }
}

class NameProducer implements Consumer<FluxSink<String>> {

    private FluxSink<String> fluxSink;

    @Override
    public void accept(FluxSink<String> stringFluxSink) {
        this.fluxSink = stringFluxSink;
    }

    public void produce() {
        String name = Util.faker().name().fullName();
        // Thread information (optional)
        String thread = Thread.currentThread().getName();
        this.fluxSink.next(thread + " : " + name);
    }
}

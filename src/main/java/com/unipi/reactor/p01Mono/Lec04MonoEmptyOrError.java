package com.unipi.reactor.p01Mono;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Mono;

public class Lec04MonoEmptyOrError {

    public static void main(String[] args) {

        // Invoke case 1
        userRepository(1).subscribe(
                Util.onNext(), // This will be invoked
                Util.onError(),
                Util.onComplete()
        );

        // Invoke case 2
        userRepository(2).subscribe(
                Util.onNext(),
                Util.onError(), // This will be invoked
                Util.onComplete()
        );

        // Invoke case 3
        userRepository(3).subscribe(
                Util.onNext(),
                Util.onError(),
                Util.onComplete() // This will be invoked
        );
    }

    // We assume that this is a repository
    // When a method returns a Publisher, the method is also a Publisher
    private static Mono<String> userRepository(int userId) {
        // Case 1
        if (userId == 1) {
            // It is not good practice to use .just(), but it is ok for this specific purpose
            return  Mono.just(Util.faker().name().firstName());
        }
        // Case 2
        else if (userId == 2) {
            return Mono.empty();
        }
        // Case 3: Intended error
        else return Mono.error(new RuntimeException("Not in allowed range"));
    }
}

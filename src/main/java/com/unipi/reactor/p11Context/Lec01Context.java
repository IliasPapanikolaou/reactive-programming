package com.unipi.reactor.p11Context;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/*
 * Context is like Http header (key-value pair)
 * It is visible only in the upstream (from its point up publisher)
 * It is immutable, but it can be changed entirely, this means that one
 * context can't be changed in its values, but it can change entirely
 * by another context.
 *
 * Tip: Used in Authentication
 */
public class Lec01Context {

    public static void main(String[] args) {

        getWelcomeMessage()
                // This updates the latest context with a new context
                .contextWrite(context -> context.put("user", context.get("user").toString().toUpperCase()))
                // Without the context, the request would be unauthenticated
                .contextWrite(Context.of("user", "sam")) // Will always keep the latest context
                .contextWrite(Context.of("user", "Jack"))
                .contextWrite(Context.of("users", "3 users")) // It won't check for this one
                .subscribe(Util.subscriber());
    }

    private static Mono<String> getWelcomeMessage() {
        return Mono
                .deferContextual(contextView -> {
                    if (contextView.hasKey("user")) {
                        return Mono.just("Welcome " + contextView.get("user"));
                    }
                    else {
                        return Mono.error(new RuntimeException("unauthenticated"));
                    }
                });
    }
}

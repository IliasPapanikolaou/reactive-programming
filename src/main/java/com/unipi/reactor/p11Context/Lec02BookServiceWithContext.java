package com.unipi.reactor.p11Context;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Lec02BookServiceWithContext {

    public static void main(String[] args) {

        // Not allowed subscriber
        BookService.getBook()
                .subscribe(Util.subscriber());

        // Allowed standard subscriber - allowed only two time
        BookService.getBook()
                // repeat another 2 times
                .repeat(2)
                .contextWrite(UserService.userCategoryContext())
                .contextWrite(Context.of("user", "sam"))
                .subscribe(Util.subscriber());

        // Allowed premium subscriber - allowed three times
        BookService.getBook()
                .repeat(2)
                .contextWrite(UserService.userCategoryContext())
                .contextWrite(Context.of("user", "mike"))
                .subscribe(Util.subscriber());
    }

}

class UserService {

    private static final Map<String, String> map = Map.of(
            "sam", "std",
            "mike", "prime"
    );

    public static Function<Context, Context> userCategoryContext() {
        return context -> {
            String user = context.get("user").toString();
            String category = map.get(user);
            return context.put("category", category);
        };
    }
}

class BookService {

    private static Map<String, Integer> map = new HashMap<>();

    static {
        map.put("std", 2);
        map.put("prime", 3);
    }

    public static Mono<String> getBook() {
        return Mono.deferContextual(contextView -> {
                    if (contextView.get("allow"))
                        return Mono.just(Util.faker().book().title());
                    else
                        return Mono.error(new RuntimeException("Not allowed"));
                })
                .contextWrite(rateLimiterContext());
    }

    private static Function<Context, Context> rateLimiterContext() {
        return context -> {
            if (context.hasKey("category")) {
                String category = context.get("category").toString();
                Integer attempts = map.get(category);
                if (attempts > 0) {
                    map.put(category, attempts - 1);
                    return context.put("allow", true);
                }
            }
            return context.put("allow", false);
        };
    }
}

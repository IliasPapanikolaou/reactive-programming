package com.unipi.reactor.p3operators;

import com.unipi.reactor.util.Util;
import lombok.Data;
import lombok.ToString;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* Map() -> One to One
* flatMap() -> One to Many
*/
public class Lec12FlatMap {

    public static void main(String[] args) {

        // The below method produces Flux<User>
        UserService.getUsers()
                // The below .map() method produces Flux<Flux<PurchaseOrder>>
                //.map(user -> OrderService.getOrders(user.getUserId()))
                // We use the flatten method flatMap to produce Flux<PurchaseOrder>
                /* If the return type is mono or flux, 99% we need flatMap() */
                .flatMap(user -> OrderService.getOrders(user.getUserId()))
                // Items do not come in order
                .subscribe(Util.subscriber());

        // Intended delay so we can watch the items
        Util.sleepSeconds(5);
    }
}

class OrderService {

    // Database Emulation
    private static Map<Integer, List<PurchaseOrder>> db = new HashMap<>();

    static {
        // Generate Orders for each user
        List<PurchaseOrder> list1 = Arrays.asList(
                new PurchaseOrder(1),
                new PurchaseOrder(1),
                new PurchaseOrder(1)
        );
        db.put(1, list1);
        List<PurchaseOrder> list2 = Arrays.asList(
                new PurchaseOrder(2),
                new PurchaseOrder(2)
        );
        db.put(2, list2);
        List<PurchaseOrder> list3 = Arrays.asList(
                new PurchaseOrder(3),
                new PurchaseOrder(3),
                new PurchaseOrder(3),
                new PurchaseOrder(3)
        );
        db.put(3, list3);
    }

    // Publisher
    public static Flux<PurchaseOrder> getOrders(int userId) {
        return Flux.create((FluxSink<PurchaseOrder> purchaseOrderFluxSink) -> {
            // Each item is emitted via fluxSink
            db.get(userId).forEach(purchaseOrderFluxSink::next);
            purchaseOrderFluxSink.complete();
        }).delayElements(Duration.ofSeconds(1)); //Intended delay
    }
}

class UserService {

    public static Flux<User> getUsers() {
        return Flux.range(1, 3)
                // .map(i -> new User(i));
                // same as above with method reference:
                .map(User::new);
    }
}

@Data
@ToString
class User {

    private int userId;
    private String name;

    public User(int userId) {
        this.userId = userId;
        this.name = Util.faker().name().fullName();
    }
}

@Data
@ToString
class PurchaseOrder {

    private String item;
    private String price;
    private int userId;

    public PurchaseOrder(int userId) {
        this.userId = userId;
        this.item = Util.faker().commerce().productName();
        this.price = Util.faker().commerce().price();
    }
}

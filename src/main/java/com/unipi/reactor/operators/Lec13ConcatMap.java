package com.unipi.reactor.operators;

import com.unipi.reactor.util.Util;

/*
 * Map() -> One to One
 * ConcatMap() -> One to Many
 *
 * concatMap() does what flatMap() but
 * items come in order:
 * - user1's items first,
 * - user2's items second
 * - so on so forth.
 *
 * Waits every user to complete emitting
 */
public class Lec13ConcatMap {

    public static void main(String[] args) {
        // The below method produces Flux<User>
        UserService.getUsers()
                // The below .map() method produces Flux<Flux<PurchaseOrder>>
                //.map(user -> OrderService.getOrders(user.getUserId()))
                // We use the flatten method flatMap to produce Flux<PurchaseOrder>
                .concatMap( user -> OrderService.getOrders(user.getUserId()))
                // Items come in order - waits every user to complete emitting
                .subscribe(Util.subscriber());

        // Intended delay so we can watch the items
        Util.sleepSeconds(10);
    }
}

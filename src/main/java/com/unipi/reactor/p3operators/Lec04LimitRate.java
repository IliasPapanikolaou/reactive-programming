package com.unipi.reactor.p3operators;

import com.unipi.reactor.util.Util;
import reactor.core.publisher.Flux;

/*
 * Limits the rate of the stream:
 * If we have 1000 items to emit and use .limitRate(100),
 * The publisher will push the first 100 items, in 75% of the 100 items
 * the receiver will request the publisher to emit another 100 items so on so forth.
 *
 * Check the items requests in the start, the request after the 75th item, 150th etc.
 */

public class Lec04LimitRate {

    public static void main(String[] args) {

        Flux.range(1, 1000)
                .log() // logging
                //.limitRate(100) // Default: In 75% will request new data to emit
                .limitRate(100, 99) // In 99% will request new data to emit
                // (in lowTide 100% will use default 75%)
                .subscribe(Util.subscriber());
    }
}

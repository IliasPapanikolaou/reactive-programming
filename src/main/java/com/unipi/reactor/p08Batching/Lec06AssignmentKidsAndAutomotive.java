package com.unipi.reactor.p08Batching;

import com.unipi.reactor.util.Util;
import lombok.Data;
import lombok.ToString;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Duration;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class Lec06AssignmentKidsAndAutomotive {

    public static void main(String[] args) {

        // Like Switch Map
        Map<String, Function<Flux<PurchaseOrder>, Flux<PurchaseOrder>>> map = Map.of(
                "Kids", OrderProcessor.kidsProcessing(),
                "Automotive", OrderProcessor.automotiveProcessing()
        );

        // Returns the keys in a set
        Set<String> set = map.keySet();

        OrderService.orderStream()
                // Filter categories for Kids or Automotive only
                .filter(p -> set.contains(p.getCategory()))
                // 2 keys (categories) -> Two separate fluxes
                .groupBy(PurchaseOrder::getCategory)
                // flatMap -> GroupedFlux(two fluxes), get the key of the flux: map.get(gf.key) -> kids or automotive
                // and put the corresponding groupFlux in that key .apply(gf)
                .flatMap(gf -> map.get(gf.key()).apply(gf))
                .subscribe(Util.subscriber());

        Util.sleepSeconds(60);
    }
}

class OrderProcessor {

    // Automotive category has 10% tax
    public static Function<Flux<PurchaseOrder>, Flux<PurchaseOrder>> automotiveProcessing() {
        return flux -> flux
                // apply 10% tax
                .doOnNext(p -> p.setPrice(1.1 * p.getPrice()))
                .doOnNext(p -> p.setItem("{{ " + p.getItem() + " }}"));
    }

    // Kid category has 50% discount
    public static Function<Flux<PurchaseOrder>, Flux<PurchaseOrder>> kidsProcessing() {
        return flux -> flux
                // apply 50% discount
                .doOnNext(p -> p.setPrice(0.5 * p.getPrice()))
                // For every product, add a free kid order
                // Extracting the item with flatMap and create a flux
                // with the additional item
                .flatMap(p -> Flux.concat(Mono.just(p), getFreeKidsOrder()));
    }

    // Free product for the kids
    private static Mono<PurchaseOrder> getFreeKidsOrder() {

        return Mono.fromSupplier(() -> {
            PurchaseOrder purchaseOrder = new PurchaseOrder();
            purchaseOrder.setItem("FREE - " + purchaseOrder.getItem());
            purchaseOrder.setPrice(0);
            purchaseOrder.setCategory("Kids");
            return purchaseOrder;
        });
    }
}

class OrderService {

    // Produces order stream
    public static Flux<PurchaseOrder> orderStream() {
        return Flux.interval(Duration.ofMillis(100))
                .map(i -> new PurchaseOrder());
    }
}


@Data
@ToString
class PurchaseOrder {

    private String item;
    private double price;
    private String category;

    public PurchaseOrder() {
        this.item = Util.faker().commerce().productName();
        this.price = Double.parseDouble(price(0.0D, 100.0D));
        this.category = Util.faker().commerce().department();
    }

    // Fix to faker 1.0.2 Issue for not respecting setLocale
    private String price(double min, double max) {
        // double price = min + this.faker.random().nextDouble() * (max - min);
        double price = min + Util.faker().random().nextDouble() * (max - min);
        return (new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ENGLISH))).format(price);
    }
}
